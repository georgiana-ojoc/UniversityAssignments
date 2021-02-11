#include <windows.h>
#include <tlhelp32.h>
#include <string>
#include <random>
#include <ctime>

#define ERROR_MESSAGE_SIZE	512
#define ITERATIONS			200
#define MAXIMUM_NUMBER		1024
#define MAPPED_FILE_NAME	TEXT("mappedFile")
#define MUTEX				"mutex"
#define EVENTS				"events"
#define MUTEX_NAME			TEXT("mutex")
#define READ_EVENT_NAME		TEXT("readEvent")
#define WRITE_EVENT_NAME	TEXT("writeEvent")

#define CLOSE_HANDLE(handle) \
	if (!CloseHandle(handle)) { \
		printf(TEXT("\n[process 2] Could not close handle.\nError code: %d\nError message: %s"), \
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
	HANDLE mappedFileHandle;
	LPSTR mappedFileBuffer;
	DWORD number;
	DWORD doubledNumber;
public:
	Synchronization() {
		mappedFileHandle = nullptr;
		mappedFileBuffer = nullptr;
		number = -1;
		doubledNumber = -1;
	}

	BOOL openFileMapping() {
		/*
		* Opens the file mapping.
		* Gets the address of the mapped view of the file.
		*/
		mappedFileHandle = OpenFileMapping(FILE_MAP_READ,
			FALSE,
			MAPPED_FILE_NAME);
		if (mappedFileHandle == NULL) {
			printf(TEXT("\n[process 2] Could not open file mapping.\nError code: %d\nError message: %s"),
				GetLastError(),
				getErrorMessage(GetLastError()));
			return FALSE;
		}
		mappedFileBuffer = (LPSTR)MapViewOfFile(mappedFileHandle,
			FILE_MAP_READ,
			0,
			0,
			0);
		if (mappedFileBuffer == NULL) {
			printf(TEXT("\n[process 2] Could not map view of file.\nError code: %d\nError message: %s"),
				GetLastError(),
				getErrorMessage(GetLastError()));
			CLOSE_HANDLE(mappedFileHandle);
			return FALSE;
		}
		return TRUE;
	}

	virtual BOOL openPrimitives() = 0;

	virtual VOID synchronize() = 0;

	virtual VOID closePrimitives() = 0;

	VOID closeFileMapping() {
		/*
		* Unmaps the view of the file.
		* Closes the file mapping handle.
		*/
		if (!UnmapViewOfFile(mappedFileBuffer)) {
			printf(TEXT("\n[process 2] Could not unmap view of file.\nError code: %d\nError message: %s"),
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
	Mutex() : Synchronization() {
		mutexHandle = nullptr;
	}

	BOOL openPrimitives() {
		/*
		* Opens the mutex.
		*/
		mutexHandle = OpenMutex(SYNCHRONIZE, FALSE, MUTEX_NAME);
		if (mutexHandle == NULL) {
			printf(TEXT("\n[process 2] Could not open mutex.\nError code: %d\nError message: %s"),
				GetLastError(),
				getErrorMessage(GetLastError()));
			return FALSE;
		}
		return TRUE;
	}

	VOID synchronize() {
		/*
		* Receives and prints numbers using mutex synchronization.
		*/
		for (WORD iteration = 0; iteration < ITERATIONS; iteration++) {
			if (WaitForSingleObject(mutexHandle, INFINITE) == WAIT_FAILED) {
				printf(TEXT("\n[process 2] Could not wait for mutex.\nError code: %d\nError message: %s"),
					GetLastError(),
					getErrorMessage(GetLastError()));
				return;
			}
			CopyMemory((LPVOID)&number, mappedFileBuffer, sizeof(DWORD));
			mappedFileBuffer += sizeof(DWORD);
			CopyMemory((LPVOID)&doubledNumber, mappedFileBuffer, sizeof(DWORD));
			mappedFileBuffer += sizeof(DWORD);
			printf("[process 2] %4d: a = %4d b = %4d (%s)\n", iteration + 1, number, doubledNumber,
				number * 2 == doubledNumber ? "correct" : "incorrect");
			if (!ReleaseMutex(mutexHandle)) {
				printf(TEXT("\n[process 2] Could not release mutex.\nError code: %d\nError message: %s"),
					GetLastError(),
					getErrorMessage(GetLastError()));
				return;
			}
		}
	}

	VOID closePrimitives() {
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
	Events() : Synchronization() {
		readEventHandle = nullptr;
		writeEventHandle = nullptr;
	}

	BOOL openPrimitives() {
		/*
		* Opens the read and the write event.
		*/
		readEventHandle = OpenEvent(EVENT_MODIFY_STATE, TRUE, READ_EVENT_NAME);
		if (readEventHandle == NULL) {
			printf(TEXT("\n[process 2] Could not open read event.\nError code: %d\nError message: %s"),
				GetLastError(),
				getErrorMessage(GetLastError()));
			return FALSE;
		}
		writeEventHandle = OpenEvent(EVENT_MODIFY_STATE | SYNCHRONIZE, TRUE, WRITE_EVENT_NAME);
		if (writeEventHandle == NULL) {
			printf(TEXT("\n[process 2] Could not open write event.\nError code: %d\nError message: %s"),
				GetLastError(),
				getErrorMessage(GetLastError()));
			return FALSE;
		}
		return TRUE;
	}

	VOID synchronize() {
		/*
		* Receives and prints numbers using mutex synchronization.
		*/
		for (WORD iteration = 0; iteration < ITERATIONS; iteration++) {
			if (WaitForSingleObject(writeEventHandle, INFINITE) == WAIT_FAILED) {
				printf(TEXT("\n[process 2] Could not wait for write event.\nError code: %d\nError message: %s"),
					GetLastError(),
					getErrorMessage(GetLastError()));
				return;
			}
			CopyMemory((LPVOID)&number, mappedFileBuffer, sizeof(DWORD));
			mappedFileBuffer += sizeof(DWORD);
			CopyMemory((LPVOID)&doubledNumber, mappedFileBuffer, sizeof(DWORD));
			mappedFileBuffer += sizeof(DWORD);
			printf("[process 2] %4d: a = %4d b = %4d (%s)\n", iteration + 1, number, doubledNumber,
				number * 2 == doubledNumber ? "correct" : "incorrect");
			if (!ResetEvent(writeEventHandle)) {
				printf(TEXT("\n[process 2] Could not reset write event.\nError code: %d\nError message: %s"),
					GetLastError(),
					getErrorMessage(GetLastError()));
				return;
			}
			if (!SetEvent(readEventHandle)) {
				printf(TEXT("\n[process 2] Could not set read event.\nError code: %d\nError message: %s"),
					GetLastError(),
					getErrorMessage(GetLastError()));
				return;
			}
		}
	}

	VOID closePrimitives() {
		/*
		* Closes the write and the read event handle.
		*/
		CLOSE_HANDLE(writeEventHandle);
		CLOSE_HANDLE(readEventHandle);
	}
};

int main(int argumentCount, char* argumentValues[]) {
	Synchronization* synchronization;
	if (argumentCount > 0 && strcmp(argumentValues[0], MUTEX) == 0) {
		synchronization = new Mutex();
	}
	else {
		synchronization = new Events();
	}
	if (!synchronization->openFileMapping()) {
		return -1;
	}
	if (!synchronization->openPrimitives()) {
		synchronization->closeFileMapping();
		return -1;
	}
	synchronization->synchronize();
	synchronization->closePrimitives();
	synchronization->closeFileMapping();
	return 0;
}
