#include <windows.h>
#include <vector>
#include <sstream>
#include <string>
#include <stdio.h>
#include <map>
#include <iostream>

#define ERROR_MESSAGE_SIZE	512
#define MAPPED_FILE_NAME	TEXT("processes")
#define PRIVILEGE			TEXT("SE_DEBUG_NAME")

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

void printChildren(DWORD process, map<DWORD, vector<DWORD>> children, map<DWORD, string> executables, BYTE level) {
	/*
	* Prints the executable name and the PID of each child of the specified process.
	*/
	for (auto child : children[process]) {
		for (BYTE index = 0; index < level; index++) {
			printf("\t");
		}
		printf("%s (%d)\n", executables[child].c_str(), child);
		printChildren(child, children, executables, level + 1);
	}
}

void printProcesses(vector<DWORD>& trees, map<DWORD, DWORD> parents, map<DWORD, string> executables, map<DWORD, vector<DWORD>> children) {
	/*
	* Prints the executable name and the PID of each process tree.
	* Stores in a vector the PID of the root of each process tree.
	*/
	BYTE tree = 0;
	trees.clear();
	for (auto item : parents) {
		if (item.first == item.second || parents.find(item.second) == parents.end()) {
			printf("[process tree number %d]\n%s (%d)\n", ++tree, executables[item.first].c_str(), item.first);
			printChildren(item.first, children, executables, 1);
			trees.push_back(item.first);
		}
	}
}

BOOL openFileMapping(HANDLE& mappedFileHandle, LPSTR& mappedFileBuffer) {
	/*
	* Opens the file mapping.
	* Gets the address of the mapped view of the file.
	*/
	mappedFileHandle = OpenFileMapping(FILE_MAP_READ,
		FALSE,
		MAPPED_FILE_NAME);
	if (mappedFileHandle == NULL) {
		printf(TEXT("\nCould not open file mapping.\nError code: %d\nError message: %s"),
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
		printf(TEXT("\nCould not map view of file.\nError code: %d\nError message: %s"),
			GetLastError(),
			getErrorMessage(GetLastError()));
		CLOSE_HANDLE(mappedFileHandle);
		return FALSE;
	}
	return TRUE;
}

BOOL getProcesses(LPSTR mappedFileBuffer, map<DWORD, DWORD>& parents, map<DWORD, string>& executables, map<DWORD, vector<DWORD>>& children) {
	/*
	* Reads the process information from the specified buffer.
	* Stores in a map the parent PID of each process.
	* Stores in a map the executable name of each process.
	* Stores in a map a child PID vector of each process.
	*/
	stringstream processes(mappedFileBuffer);
	string line;
	parents.clear();
	executables.clear();
	children.clear();
	while (getline(processes, line, '\n')) {
		stringstream process(line);
		string token;
		getline(process, token, '#');
		DWORD PID = stoi(token);
		getline(process, token, '#');
		DWORD parentPID = stoi(token);
		parents[PID] = parentPID;
		getline(process, token, '#');
		executables[PID] = token;
		if (PID != parentPID) {
			children[parentPID].push_back(PID);
		}
	}
	return TRUE;
}

BOOL setPrivilege(LPCSTR name, BOOL enable) {
	HANDLE tokenHandle;
	if (!OpenProcessToken(GetCurrentProcess(), TOKEN_ADJUST_PRIVILEGES, &tokenHandle)) {
		printf(TEXT("\nCould not open current process token.\nError code: %d\nError message: %s"),
			GetLastError(),
			getErrorMessage(GetLastError()));
		return FALSE;
	}
	LUID ID;
	if (!LookupPrivilegeValue(nullptr, SE_DEBUG_NAME, &ID)) {
		printf(TEXT("\nCould not identify privilege.\nError code: %d\nError message: %s"),
			GetLastError(),
			getErrorMessage(GetLastError()));
		CLOSE_HANDLE(tokenHandle);
		return FALSE;
	}
	TOKEN_PRIVILEGES tokenPrivileges;
	tokenPrivileges.PrivilegeCount = 1;
	tokenPrivileges.Privileges[0].Luid = ID;
	if (enable) {
		tokenPrivileges.Privileges[0].Attributes = SE_PRIVILEGE_ENABLED;
	}
	else {
		tokenPrivileges.Privileges[0].Attributes = SE_PRIVILEGE_REMOVED;
	}
	if (!AdjustTokenPrivileges(tokenHandle,
		FALSE,
		&tokenPrivileges,
		sizeof(tokenPrivileges),
		nullptr, nullptr)) {
		printf(TEXT("\nCould not identify privilege.\nError code: %d\nError message: %s"),
			GetLastError(),
			getErrorMessage(GetLastError()));
		CLOSE_HANDLE(tokenHandle);
		return FALSE;
	}
	CLOSE_HANDLE(tokenHandle);
	return TRUE;
}

BOOL terminateProcess(DWORD process) {
	HANDLE handle = OpenProcess(PROCESS_TERMINATE, FALSE, process);
	if (handle == INVALID_HANDLE_VALUE) {
		printf(TEXT("\nCould not open process %d.\nError code: %d\nError message: %s"),
			process,
			GetLastError(),
			getErrorMessage(GetLastError()));
		return FALSE;
	}
	if (!TerminateProcess(handle, 1)) {
		printf(TEXT("\nCould not terminate process %d.\nError code: %d\nError message: %s"),
			process,
			GetLastError(),
			getErrorMessage(GetLastError()));
		CLOSE_HANDLE(handle);
		return FALSE;
	}
	printf("Terminated process %d.\n", process);
	CLOSE_HANDLE(handle);
	return TRUE;
}

BOOL terminateChildren(DWORD process, map<DWORD, vector<DWORD>> children) {
	for (auto child : children[process]) {
		terminateChildren(child, children);
		if (!terminateProcess(child)) {
			return FALSE;
		}
	}
	return TRUE;
}

BOOL terminateProcessTree(DWORD process, map<DWORD, vector<DWORD>> children) {
	if (!terminateChildren(process, children)) {
		return FALSE;
	}
	if (!terminateProcess(process)) {
		return FALSE;
	}
	return TRUE;
}

int main() {
	/*
	* Reads the processes from the mapped view of the file.
	* Prints the process trees.
	*/
	HANDLE mappedFileHandle;
	LPSTR mappedFileBuffer;
	if (!openFileMapping(mappedFileHandle, mappedFileBuffer)) {
		return -1;
	}
	map<DWORD, DWORD> parents;
	map<DWORD, string> executables;
	map<DWORD, vector<DWORD>> children;
	if (!getProcesses(mappedFileBuffer, parents, executables, children)) {
		CLOSE_HANDLE(mappedFileHandle);
		return -1;
	}
	CLOSE_HANDLE(mappedFileHandle);
	vector<DWORD> trees;
	printProcesses(trees, parents, executables, children);
	/*
	* Adds the privilege to debug the system.
	*/
	if (!setPrivilege(PRIVILEGE, TRUE)) {
		return -1;
	}
	printf("Enabled debug system privilege.\n");
	/*
	* Reads the process tree number from the standard input.
	* Terminates the specified process tree.
	*/
	printf(TEXT("Enter the process tree number to terminate: "));
	DWORD tree;
	cin >> tree;
	if (tree < 0 || tree >= trees.size()) {
		printf(TEXT("Invalid process tree number."));
		return -1;
	}
	if (!terminateProcessTree(trees[tree - 1], children)) {
		return -1;
	}
	/*
	* Removes the privilege to debug the system.
	*/
	if (!setPrivilege(PRIVILEGE, FALSE)) {
		return -1;
	}
	printf("Disabled debug system privilege.\n");
	return 0;
}
