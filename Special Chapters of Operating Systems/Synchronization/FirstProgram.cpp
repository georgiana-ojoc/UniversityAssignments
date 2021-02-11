#include <windows.h>
#include <tlhelp32.h>
#include <cstring>
#include <random>
#include <ctime>

#define ERROR_MESSAGE_SIZE	512
#define ITERATIONS			200
#define MAXIMUM_NUMBER		1024
#define MAPPED_FILE_NAME	TEXT("mappedFile")
#define MAPPED_FILE_SIZE	ITERATIONS * 8
#define MUTEX				"mutex"
#define EVENTS				"events"
#define MUTEX_NAME			TEXT("mutex")
#define READ_EVENT_NAME		TEXT("readEvent")
#define WRITE_EVENT_NAME	TEXT("writeEvent")
#define SECOND_PROCESS		TEXT("D:\\Anul III\\Semestrul I\\Capitole speciale de sisteme de operare\\Teme\\Tema 3\\SecondProgram\\x64\\Debug\\SecondProgram.exe")

#define CLOSE_HANDLE(handle) \
	if (!CloseHandle(handle)) { \
		printf(TEXT("\n[process 1] Could not close handle.\nError code: %d\nError message: %s"), \
			GetLastError(), \
			getErrorMessage(GetLastError())); \
	}

using namespace std;

LPCSTR getErrorMessage(LONG errorCode) {
	/*
	* Returns the error message from the specified error code.
	* Returns "Error message not found." otherwise.
	*/
	LPSTR errorMessage = new CHAR[ERROR_MESSAGE_SIZE];
	DWORD characters = FormatMessage(FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_IGNORE_INSERTS,
		nullptr,
		errorCode,
		0,
		errorMessage,
		ERROR_MESSAGE_SIZE,
		0);
	if (characters) {
		return errorMessage;
	}
	return TEXT("Error message not found.");
}

class Synchronization {
protected:
	LPSTR type;
	HANDLE mappedFileHandle;
	LPSTR mappedFileBuffer;
	PROCESS_INFORMATION processInformation;
	default_random_engine engine;
	uniform_int_distribution<DWORD> distribution;
	DWORD number;
	DWORD doubledNumber;
public:
	Synchronization(LPSTR type) : engine(static_cast<unsigned int>(time(nullptr))), distribution(0, MAXIMUM_NUMBER) {
		this->type = type;
		mappedFileHandle = nullptr;
		mappedFileBuffer = nullptr;
		processInformation = {};
		number = -1;
		doubledNumber = -1;
	}

	BOOL createFileMapping() {
		/*
		* Creates the file mapping.
		* Gets the address of the mapped view of the file.
		*/
		mappedFileHandle = CreateFileMapping(INVALID_HANDLE_VALUE,
			nullptr,
			PAGE_READWRITE,
			0,
			MAPPED_FILE_SIZE,
			MAPPED_FILE_NAME);
		if (mappedFileHandle == NULL) {
			printf(TEXT("\n[process 1] Could not create file mapping.\nError code: %d\nError message: %s"),
				GetLastError(),
				getErrorMessage(GetLastError()));
			return FALSE;
		}
		mappedFileBuffer = (LPSTR)MapViewOfFile(mappedFileHandle,
			FILE_MAP_WRITE,
			0,
			0,
			0);
		if (mappedFileBuffer == NULL) {
			printf(TEXT("\n[process 1] Could not map view of file.\nError code: %d\nError message: %s"),
				GetLastError(),
				getErrorMessage(GetLastError()));
			CLOSE_HANDLE(mappedFileHandle);
			return FALSE;
		}
		return TRUE;
	}

	virtual BOOL createPrimitives() = 0;

	BOOL startSecondProcess() {
		/*
		* Starts the second process.
		* Gets the adress of the process information.
		*/
		STARTUPINFO startupInformation;
		ZeroMemory(&startupInformation, sizeof(startupInformation));
		startupInformation.cb = sizeof(startupInformation);
		if (!CreateProcess(SECOND_PROCESS,
			type,
			nullptr,
			nullptr,
			FALSE,
			0,
			nullptr,
			nullptr,
			&startupInformation,
			&processInformation)) {
			printf(TEXT("\n[process 1] Could not start second process.\nError code: %d\nError message: %s"),
				GetLastError(),
				getErrorMessage(GetLastError()));
			return FALSE;
		}
		return TRUE;
	}

	virtual VOID synchronize() = 0;

	VOID finishSecondProcess() {
		/*
		* Waits for the second process to finish.
		* Closes the thread and the process handles.
		*/
		if (WaitForSingleObject(processInformation.hProcess, INFINITE) == WAIT_FAILED) {
			printf(TEXT("\n[process 1] Could not wait for mutex.\nError code: %d\nError message: %s"),
				GetLastError(),
				getErrorMessage(GetLastError()));
		}
		CLOSE_HANDLE(processInformation.hThread);
		CLOSE_HANDLE(processInformation.hProcess);
	}

	virtual VOID destroyPrimitives() = 0;

	VOID destroyFileMapping() {
		/*
		* Unmaps the view of the file.
		* Closes the file mapping handle.
		*/
		if (!UnmapViewOfFile(mappedFileBuffer)) {
			printf(TEXT("\n[process 1] Could not unmap view of file.\nError code: %d\nError message: %s"),
				GetLastError(),
				getErrorMessage(GetLastError()));
		}
		CLOSE_HANDLE(mappedFileHandle);
	}
};

class Mutex : public Synchronization {
private:
	HANDLE mutexHandle;
public:
	Mutex(LPSTR type) : Synchronization(type) {
		mutexHandle = nullptr;
	}

	BOOL createPrimitives() {
		/*
		* Creates the mutex.
		*/
		mutexHandle = CreateMutex(nullptr, FALSE, MUTEX_NAME);
		if (mutexHandle == NULL) {
			printf(TEXT("\n[process 1] Could not create mutex.\nError code: %d\nError message: %s"),
				GetLastError(),
				getErrorMessage(GetLastError()));
			return FALSE;
		}
		return TRUE;
	}

	VOID synchronize() {
		/*
		* Generates and sends numbers using mutex synchronization.
		*/
		for (WORD iteration = 0; iteration < ITERATIONS; iteration++) {
			number = distribution(engine);
			doubledNumber = number * 2;
			if (WaitForSingleObject(mutexHandle, INFINITE) == WAIT_FAILED) {
				printf(TEXT("\n[process 1] Could not wait for mutex.\nError code: %d\nError message: %s"),
					GetLastError(),
					getErrorMessage(GetLastError()));
				return;
			}
			CopyMemory((LPVOID)mappedFileBuffer, &number, sizeof(DWORD));
			mappedFileBuffer += sizeof(DWORD);
			CopyMemory((LPVOID)mappedFileBuffer, &doubledNumber, sizeof(DWORD));
			mappedFileBuffer += sizeof(DWORD);
			printf("[process 1] %4d: a = %4d b = %4d\n", iteration + 1, number, doubledNumber);
			if (!ReleaseMutex(mutexHandle)) {
				printf(TEXT("\n[process 1] Could not release mutex.\nError code: %d\nError message: %s"),
					GetLastError(),
					getErrorMessage(GetLastError()));
				return;
			}
		}
	}

	VOID destroyPrimitives() {
		/*
		* Closes the mutex handle.
		*/
		CLOSE_HANDLE(mutexHandle);
	}
};

class Events : public Synchronization {
private:
	HANDLE readEventHandle;
	HANDLE writeEventHandle;
public:
	Events(LPSTR type) : Synchronization(type) {
		readEventHandle = nullptr;
		writeEventHandle = nullptr;
	}

	BOOL createPrimitives() {
		/*
		* Creates the write and the read event.
		*/
		writeEventHandle = CreateEvent(nullptr, TRUE, FALSE, WRITE_EVENT_NAME);
		if (writeEventHandle == NULL) {
			printf(TEXT("\n[process 1] Could not create write event.\nError code: %d\nError message: %s"),
				GetLastError(),
				getErrorMessage(GetLastError()));
			return FALSE;
		}
		readEventHandle = CreateEvent(nullptr, TRUE, TRUE, READ_EVENT_NAME);
		if (readEventHandle == NULL) {
			printf(TEXT("\n[process 1] Could not create read event.\nError code: %d\nError message: %s"),
				GetLastError(),
				getErrorMessage(GetLastError()));
			return FALSE;
		}
		return TRUE;
	}

	VOID synchronize() {
		/*
		* Generates and sends numbers using mutex synchronization.
		*/
		for (WORD iteration = 0; iteration < ITERATIONS; iteration++) {
			number = distribution(engine);
			doubledNumber = number * 2;
			if (WaitForSingleObject(readEventHandle, INFINITE) == WAIT_FAILED) {
				printf(TEXT("\n[process 1] Could not wait for read event.\nError code: %d\nError message: %s"),
					GetLastError(),
					getErrorMessage(GetLastError()));
				return;
			}
			CopyMemory((LPVOID)mappedFileBuffer, &number, sizeof(DWORD));
			mappedFileBuffer += sizeof(DWORD);
			CopyMemory((LPVOID)mappedFileBuffer, &doubledNumber, sizeof(DWORD));
			mappedFileBuffer += sizeof(DWORD);
			printf("[process 1] %4d: a = %4d b = %4d\n", iteration + 1, number, doubledNumber);
			if (!ResetEvent(readEventHandle)) {
				printf(TEXT("\n[process 1] Could not reset read event.\nError code: %d\nError message: %s"),
					GetLastError(),
					getErrorMessage(GetLastError()));
				return;
			}
			if (!SetEvent(writeEventHandle)) {
				printf(TEXT("\n[process 1] Could not set write event.\nError code: %d\nError message: %s"),
					GetLastError(),
					getErrorMessage(GetLastError()));
				return;
			}
		}
	}

	VOID destroyPrimitives() {
		/*
		* Closes the read and the write event handle.
		*/
		CLOSE_HANDLE(readEventHandle);
		CLOSE_HANDLE(writeEventHandle);
	}
};

int main(int argumentCount, char* argumentValues[]) {
	Synchronization* synchronization;
	if (argumentCount > 1 && strcmp(argumentValues[1], MUTEX) == 0) {
		printf("Synchronization: %s\n", MUTEX);
		synchronization = new Mutex(const_cast<LPSTR>(MUTEX));
	}
	else {
		printf("Synchronization: %s\n", EVENTS);
		synchronization = new Events(const_cast<LPSTR>(EVENTS));
	}
	if (!synchronization->createFileMapping()) {
		return -1;
	}
	if (!synchronization->createPrimitives()) {
		synchronization->destroyFileMapping();
		return -1;
	}
	if (!synchronization->startSecondProcess()) {
		synchronization->destroyPrimitives();
		synchronization->destroyFileMapping();
		return -1;
	}
	synchronization->synchronize();
	synchronization->finishSecondProcess();
	synchronization->destroyPrimitives();
	synchronization->destroyFileMapping();
	return 0;
}
