#include <windows.h>
#include <tlhelp32.h>
#include <string>

#define ERROR_MESSAGE_SIZE	512
#define MAPPED_FILE_NAME	TEXT("processes")
#define MAPPED_FILE_SIZE	1024 * 1024
#define SECOND_PROGRAM		TEXT("D:\\Anul III\\Semestrul I\\Capitole speciale de sisteme de operare\\Teme\\Tema 2\\SecondProgram\\x64\\Debug\\SecondProgram.exe")

#define CLOSE_HANDLE(handle) \
	if (!CloseHandle(handle)) { \
		printf(TEXT("\nCould not close handle.\nError code: %d\nError message: %s"), \
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

BOOL createFileMapping(HANDLE& mappedFileHandle, LPSTR& mappedFileBuffer) {
	/*
	* Creates a file mapping.
	* Gets the address of the mapped view of the file.
	*/
	mappedFileHandle = CreateFileMapping(INVALID_HANDLE_VALUE,
		nullptr,
		PAGE_READWRITE,
		0,
		MAPPED_FILE_SIZE,
		MAPPED_FILE_NAME);
	if (mappedFileHandle == NULL) {
		printf(TEXT("\nCould not create file mapping.\nError code: %d\nError message: %s"),
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
		printf(TEXT("\nCould not map view of file.\nError code: %d\nError message: %s"),
			GetLastError(),
			getErrorMessage(GetLastError()));
		CLOSE_HANDLE(mappedFileHandle);
		return FALSE;
	}
	return TRUE;
}

BOOL getProcesses(string& processesBuffer) {
	/*
	* Writes to a buffer with the PID, parent PID and executable name of all the current running processes.
	*/
	HANDLE processSnapshotHandle;
	PROCESSENTRY32 processEntry;
	processSnapshotHandle = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
	if (processSnapshotHandle == INVALID_HANDLE_VALUE) {
		printf(TEXT("\nCould not create snapshot of processes.\nError code: %d\nError message: %s"),
			GetLastError(),
			getErrorMessage(GetLastError()));
		return FALSE;
	}
	processEntry.dwSize = sizeof(PROCESSENTRY32);
	if (!Process32First(processSnapshotHandle, &processEntry)) {
		printf(TEXT("\nCould not get information about first process.\nError code: %d\nError message: %s"),
			GetLastError(),
			getErrorMessage(GetLastError()));
		CLOSE_HANDLE(processSnapshotHandle);
		return FALSE;
	}
	processesBuffer.clear();
	do {
		processesBuffer += to_string(processEntry.th32ProcessID);
		processesBuffer += TEXT("#");
		processesBuffer += to_string(processEntry.th32ParentProcessID);
		processesBuffer += TEXT("#");
		processesBuffer += processEntry.szExeFile;
		processesBuffer += TEXT("\n");
	} while (Process32Next(processSnapshotHandle, &processEntry));
	CLOSE_HANDLE(processSnapshotHandle);
	return TRUE;
}

int main() {
	/*
	* Writes the processes to the mapped view of the file.
	*/
	HANDLE mappedFileHandle;
	LPSTR mappedFileBuffer;
	if (!createFileMapping(mappedFileHandle, mappedFileBuffer)) {
		return -1;
	}
	string processesBuffer;
	if (!getProcesses(processesBuffer)) {
		CLOSE_HANDLE(mappedFileHandle);
		return -1;
	}
	CopyMemory((LPVOID)mappedFileBuffer, processesBuffer.c_str(), processesBuffer.size() * sizeof(CHAR));
	if (!UnmapViewOfFile(mappedFileBuffer)) {
		printf(TEXT("\nCould not unmap view of file.\nError code: %d\nError message: %s"),
			GetLastError(),
			getErrorMessage(GetLastError()));
		CLOSE_HANDLE(mappedFileHandle);
		return -1;
	}
	CLOSE_HANDLE(mappedFileHandle);
	/*
	* Starts the second program.
	* Waits for the second program to finish.
	*/
	PROCESS_INFORMATION processInformation;
	STARTUPINFO startupInformation;
	ZeroMemory(&startupInformation, sizeof(startupInformation));
	startupInformation.cb = sizeof(startupInformation);
	if (!CreateProcess(SECOND_PROGRAM,
		nullptr,
		nullptr,
		nullptr,
		FALSE,
		0,
		nullptr,
		nullptr,
		&startupInformation,
		&processInformation)) {
		printf(TEXT("\nCould not start second program.\nError code: %d\nError message: %s"),
			GetLastError(),
			getErrorMessage(GetLastError()));
		return -1;
	}
	WaitForSingleObject(processInformation.hProcess, INFINITE);
	CLOSE_HANDLE(processInformation.hThread);
	CLOSE_HANDLE(processInformation.hProcess);
	
	return 0;
}
