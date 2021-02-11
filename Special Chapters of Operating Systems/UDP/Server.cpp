#include <stdio.h>
#include <sstream>
#include <string>
#include <windows.h>
#include <wininet.h>

#pragma comment(lib, "wininet.lib")
#pragma comment(lib, TEXT("ws2_32.lib"))

#define BUFFER_SIZE			2048
#define ERROR_MESSAGE_SIZE	512

#define HOST				TEXT("127.0.0.1")
#define PORT				3000

#define CREATE_FILE			TEXT("create_file")
#define APPEND_FILE			TEXT("append_file")
#define DELETE_FILE			TEXT("delete_file")
#define CREATE_REGISTRY_KEY	TEXT("create_registry_key")
#define DELETE_REGISTRY_KEY	TEXT("delete_registry_key")
#define RUN					TEXT("run")
#define DOWNLOAD			TEXT("download")
#define LIST				TEXT("list")

using namespace std;

LPCSTR getErrorMessage(LONG errorCode) {
	LPSTR errorMessage = new CHAR[ERROR_MESSAGE_SIZE];
	DWORD characters = FormatMessage(FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_IGNORE_INSERTS,
		NULL,
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

LPCSTR getInternetErrorMessage() {
	DWORD errorCode;
	DWORD length = ERROR_MESSAGE_SIZE;
	LPSTR errorMessage = new char[SIZE_T(length) + 1];
	if (InternetGetLastResponseInfo(&errorCode, errorMessage, &length)) {
		return errorMessage;
	}
	return TEXT("Error message not found.");
}

BOOL createFile(string name) {
	HANDLE file = CreateFile(name.c_str(), 0, 0, NULL, CREATE_NEW, FILE_ATTRIBUTE_NORMAL, NULL);
	if (file == INVALID_HANDLE_VALUE) {
		DWORD lastError = GetLastError();
		printf(TEXT("\nFailed to create file.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
		return FALSE;
	}
	printf(TEXT("\nCreated file: %s.\n"), name.c_str());
	BOOL result = CloseHandle(file);
	if (result) {
		printf(TEXT("\nClosed file.\n"));
	}
	return TRUE;
}

BOOL appendFile(string name, string content) {
	HANDLE file = CreateFile(name.c_str(), FILE_APPEND_DATA, 0, NULL, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, NULL);
	if (file == INVALID_HANDLE_VALUE) {
		DWORD lastError = GetLastError();
		printf(TEXT("\nFailed to open file.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
		return FALSE;
	}
	printf(TEXT("\nOpened file: %s.\n"), name.c_str());
	DWORD writtenBytes;
	BOOL result = WriteFile(file, content.c_str(), content.length(), &writtenBytes, NULL);
	if (!result) {
		DWORD lastError = GetLastError();
		printf(TEXT("\nFailed to write in file.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
		CloseHandle(file);
		return FALSE;
	}
	printf(TEXT("\nWrote in file: %s.\n"), name.c_str());
	result = CloseHandle(file);
	if (result) {
		printf(TEXT("\nClosed file.\n"));
	}
	return TRUE;
}

BOOL deleteFile(string name) {
	BOOL result = DeleteFile(name.c_str());
	if (!result) {
		DWORD lastError = GetLastError();
		printf(TEXT("\nFailed to delete file.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
		return FALSE;
	}
	printf(TEXT("\nDeleted file: %s.\n"), name.c_str());
	return TRUE;
}

BOOL parseRegistryKey(string absolutePath, HKEY& hive, string& path) {
	if (absolutePath.find('\\') == string::npos) {
		printf(TEXT("\nInvalid path.\n"));
		return FALSE;
	}
	stringstream tokens(absolutePath);
	string hiveName;
	getline(tokens, hiveName, '\\');
	if (hiveName == TEXT("HKEY_CLASSES_ROOT")) {
		hive = HKEY_CLASSES_ROOT;
	}
	else if (hiveName == TEXT("HKEY_CURRENT_USER")) {
		hive = HKEY_CURRENT_USER;
	}
	else if (hiveName == TEXT("HKEY_LOCAL_MACHINE")) {
		hive = HKEY_LOCAL_MACHINE;
	}
	else if (hiveName == TEXT("HKEY_USERS")) {
		hive = HKEY_USERS;
	}
	else if (hiveName == TEXT("HKEY_CURRENT_CONFIG")) {
		hive = HKEY_CURRENT_CONFIG;
	}
	else {
		printf(TEXT("\nInvalid hive.\n"));
		return FALSE;
	}
	getline(tokens, path);
	if (!path.length()) {
		printf(TEXT("\nInvalid path.\n"));
		return FALSE;
	}
	return TRUE;
}

BOOL createRegistryKey(string absolutePath) {
	HKEY hive;
	string path;
	BOOL pathResult = parseRegistryKey(absolutePath, hive, path);
	if (!pathResult) {
		return FALSE;
	}
	HKEY key;
	DWORD disposition;
	LSTATUS keyResult = RegCreateKeyEx(hive, path.c_str(), 0, NULL, REG_OPTION_NON_VOLATILE, KEY_ALL_ACCESS, NULL, &key, &disposition);
	if (keyResult != ERROR_SUCCESS) {
		printf(TEXT("\nFailed to create registry key.\nError code: %d\nError message: %s"),
			keyResult,
			getErrorMessage(keyResult));
		return FALSE;
	}
	if (disposition == REG_CREATED_NEW_KEY) {
		printf(TEXT("\nCreated registry key.\n"));
	}
	else {
		printf(TEXT("\nOpened registry key.\n"));
	}
	keyResult = RegCloseKey(key);
	if (keyResult == ERROR_SUCCESS) {
		printf(TEXT("\nClosed registry key.\n"));
	}
	return TRUE;
}

BOOL deleteRegistryKey(string absolutePath) {
	HKEY hive;
	string path;
	BOOL pathResult = parseRegistryKey(absolutePath, hive, path);
	if (!pathResult) {
		return FALSE;
	}
	LSTATUS keyResult = RegDeleteTreeA(hive, path.c_str());
	if (keyResult != ERROR_SUCCESS) {
		printf(TEXT("\nFailed to delete registry key.\nError code: %d\nError message: %s"),
			keyResult,
			getErrorMessage(keyResult));
		return FALSE;
	}
	printf(TEXT("\nDeleted registry key.\n"));
	return TRUE;
}

BOOL run(string absolutePath) {
	PROCESS_INFORMATION processInformation;
	STARTUPINFO startupInformation;
	ZeroMemory(&startupInformation, sizeof(startupInformation));
	startupInformation.cb = sizeof(startupInformation);
	if (!CreateProcess(absolutePath.c_str(), NULL, NULL, NULL, FALSE, CREATE_NEW_CONSOLE, NULL, NULL, &startupInformation, &processInformation)) {
		DWORD lastError = GetLastError();
		printf(TEXT("\nCould not create process.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
		return FALSE;
	}
	printf(TEXT("\nCreated process: %s.\n"), absolutePath.c_str());
	int waitResult = WaitForSingleObject(processInformation.hProcess, INFINITE);
	if (waitResult == WAIT_OBJECT_0) {
		printf(TEXT("\nWaited process.\n"));
	}
	BOOL closeResult = CloseHandle(processInformation.hProcess);
	if (closeResult) {
		printf(TEXT("\nClosed process.\n"));
	}
	closeResult = CloseHandle(processInformation.hThread);
	if (closeResult) {
		printf(TEXT("\nClosed primary thread.\n"));
	}
	return TRUE;
}

BOOL download(string name, string link) {
	HINTERNET internet = InternetOpen("", INTERNET_OPEN_TYPE_PRECONFIG, NULL, NULL, 0);
	if (internet == NULL) {
		DWORD lastError = GetLastError();
		printf(TEXT("\nCould not initialize use of WinInet functions.\nError code: %d\nError message: %s\n"),
			lastError,
			getInternetErrorMessage());
		return FALSE;
	}
	printf(TEXT("\nInitialized use of WinInet functions.\n"));
	HINTERNET URL = InternetOpenUrl(internet, link.c_str(), NULL, 0, 0, 0);
	if (URL == NULL) {
		DWORD lastError = GetLastError();
		printf(TEXT("\nCould not open resource.\nError code: %d\nError message: %s\n"),
			lastError,
			getInternetErrorMessage());
		InternetCloseHandle(internet);
		return FALSE;
	}
	printf(TEXT("\nOpened resource: %s.\n"), link.c_str());
	string content;
	LPSTR buffer = new CHAR[BUFFER_SIZE];
	DWORD readBytes;
	while (TRUE) {
		if (!InternetReadFile(URL, buffer, BUFFER_SIZE, &readBytes)) {
			DWORD lastError = GetLastError();
			printf(TEXT("\nCould not read file.\nError code: %d\nError message: %s\n"),
				lastError,
				getInternetErrorMessage());
			InternetCloseHandle(URL);
			InternetCloseHandle(internet);
			return FALSE;
		}
		if (readBytes) {
			buffer[readBytes] = NULL;
			content += buffer;
		}
		else {
			break;
		}
	}
	HANDLE file = CreateFile(name.c_str(), GENERIC_WRITE, 0, NULL, CREATE_NEW, FILE_ATTRIBUTE_NORMAL, NULL);
	if (file == INVALID_HANDLE_VALUE) {
		DWORD lastError = GetLastError();
		printf(TEXT("\nFailed to create file.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
		return FALSE;
	}
	printf(TEXT("\nCreated file: %s.\n"), name.c_str());
	DWORD writtenBytes;
	BOOL result = WriteFile(file, content.c_str(), content.length(), &writtenBytes, NULL);
	if (!result) {
		DWORD lastError = GetLastError();
		printf(TEXT("\nFailed to write in file.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
		CloseHandle(file);
		return FALSE;
	}
	printf(TEXT("\nWrote in file: %s.\n"), name.c_str());
	result = CloseHandle(file);
	if (result) {
		printf(TEXT("\nClosed file.\n"));
	}
	result = InternetCloseHandle(URL);
	if (result) {
		printf(TEXT("\nClosed resource.\n"));
	}
	result = InternetCloseHandle(internet);
	if (result) {
		printf(TEXT("\nTerminated use of WinInet functions.\n"));
	}
	return TRUE;
}

BOOL list(string absolutePath, int level) {
	WIN32_FIND_DATA data;
	HANDLE file = FindFirstFile((absolutePath + "\\*").c_str(), &data);
	if (file == INVALID_HANDLE_VALUE) {
		DWORD lastError = GetLastError();
		printf(TEXT("\nFailed to find first file.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
		return FALSE;
	}
	do {
		string name(data.cFileName);
		if (name == "." || name == "..") {
			continue;
		}
		for (int index = 0; index < level; index++) {
			printf(TEXT("  "));
		}
		printf(TEXT("%s\n"), name.c_str());
		if (data.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) {
			list(absolutePath + '\\' + name, level + 1);
		}
	} while (FindNextFile(file, &data));
	FindClose(file);
	return TRUE;
}

DWORD WINAPI function(LPVOID parameter) {
	char* arguments = static_cast<char*>(parameter);
	char* token = strtok(arguments, " ");
	if (token == NULL) {
		printf(TEXT("\nInvalid command.\n"));
		return -1;
	}
	string command(_strlwr(token));
	if (command == APPEND_FILE || command == DOWNLOAD) {
		token = strtok(NULL, "\"");
		if (token == NULL) {
			printf(TEXT("\nInvalid command.\n"));
			return -1;
		}
		string firstArgument(token);
		if (firstArgument[firstArgument.length() - 1] == ' ') {
			firstArgument.pop_back();
		}
		token = strtok(NULL, "\"");
		if (token == NULL) {
			printf(TEXT("\nInvalid command.\n"));
			return -1;
		}
		string secondArgument(token);
		if (command == APPEND_FILE) {
			BOOL result = appendFile(firstArgument, secondArgument);
			if (!result) {
				return -1;
			}
			return 0;
		}
		if (command == DOWNLOAD) {
			BOOL result = download(firstArgument, secondArgument);
			if (!result) {
				return -1;
			}
			return 0;
		}
	}
	token = strtok(NULL, "");
	if (token == NULL) {
		printf(TEXT("\nInvalid command.\n"));
		return -1;
	}
	string argument(token);
	if (command == CREATE_FILE) {
		BOOL result = createFile(argument);
		if (!result) {
			return -1;
		}
	}
	else if (command == DELETE_FILE) {
		BOOL result = deleteFile(argument);
		if (!result) {
			return -1;
		}
	}
	else if (command == CREATE_REGISTRY_KEY) {
		BOOL result = createRegistryKey(argument);
		if (!result) {
			return -1;
		}
	}
	else if (command == DELETE_REGISTRY_KEY) {
		BOOL result = deleteRegistryKey(argument);
		if (!result) {
			return -1;
		}
	}
	else if (command == RUN) {
		BOOL result = run(argument);
		if (!result) {
			return -1;
		}
	}
	else if (command == LIST) {
		DWORD attributes = GetFileAttributes(argument.c_str());
		if (attributes == INVALID_FILE_ATTRIBUTES) {
			DWORD lastError = GetLastError();
			printf(TEXT("\nFailed to get file attributes.\nError code: %d\nError message: %s"),
				lastError,
				getErrorMessage(lastError));
			return -1;
		}
		printf("\n%s\n", argument.c_str());
		if (attributes & FILE_ATTRIBUTE_DIRECTORY) {
			BOOL result = list(argument, 1);
			if (!result) {
				return -1;
			}
		}
	}
	else {
		printf(TEXT("\nInvalid command.\n"));
		return -1;
	}
	return 0;
}

int main() {
	WSADATA data;
	int result = WSAStartup(MAKEWORD(2, 2), &data);
	if (result) {
		printf(TEXT("\nFailed to initiate use of WinSock DLL.\nError code: %d\nError message: %s"),
			result,
			getErrorMessage(result));
		return -1;
	}
	printf(TEXT("\nInitiated use of WinSock DLL.\n"));

	SOCKET server = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
	if (server == INVALID_SOCKET) {
		DWORD lastError = WSAGetLastError();
		printf(TEXT("\nFailed to create server socket.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
		WSACleanup();
		return -1;
	}
	printf(TEXT("\nCreated server socket.\n"));

	sockaddr_in serverAddress;
	serverAddress.sin_family = AF_INET;
	serverAddress.sin_addr.s_addr = inet_addr(HOST);
	serverAddress.sin_port = htons(PORT);
	result = bind(server, (SOCKADDR*)&serverAddress, sizeof(serverAddress));
	if (result) {
		DWORD lastError = WSAGetLastError();
		printf(TEXT("\nFailed to associate local address to server socket.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
		closesocket(server);
		WSACleanup();
		return -1;
	}
	printf(TEXT("\nAssociated local address to server socket.\n"));

	char buffer[BUFFER_SIZE];
	sockaddr_in clientAddress;
	while (TRUE) {
		memset(buffer, NULL, sizeof(buffer));
		memset(&clientAddress, NULL, sizeof(clientAddress));
		int clientAddressSize = sizeof(clientAddress);
		printf(TEXT("\nWaiting to receive datagram on host %s at port %d...\n"), HOST, PORT);
		int receivedBytes = recvfrom(server, buffer, BUFFER_SIZE - 1, 0, (SOCKADDR*)&clientAddress, &clientAddressSize);
		if (receivedBytes == SOCKET_ERROR) {
			DWORD lastError = WSAGetLastError();
			printf(TEXT("\nFailed to receive datagram.\nError code: %d\nError message: %s"),
				lastError,
				getErrorMessage(lastError));
		}
		else {
			if (receivedBytes < BUFFER_SIZE - 1) {
				buffer[receivedBytes] = NULL;
			}
			else {
				buffer[BUFFER_SIZE - 1] = NULL;
			}
			printf(TEXT("\nReceived datagram: %s.\n"), buffer);
		}
		HANDLE thread = CreateThread(NULL, 0, function, buffer, 0, NULL);
		if (thread == NULL) {
			DWORD lastError = GetLastError();
			printf(TEXT("\nFailed to create thread.\nError code: %d\nError message: %s"),
				lastError,
				getErrorMessage(lastError));
		}
		else {
			printf(TEXT("\nCreated thread.\n"));
			int waitResult = WaitForSingleObject(thread, INFINITE);
			if (waitResult == WAIT_OBJECT_0) {
				printf(TEXT("\nWaited thread.\n"));
			}

			BOOL closeResult = CloseHandle(thread);
			if (closeResult) {
				printf(TEXT("\nClosed thread.\n"));
			}
		}
	}

	result = closesocket(server);
	if (!result) {
		printf(TEXT("\nClosed server socket.\n"));
	}
	result = WSACleanup();
	if (!result) {
		printf(TEXT("\nTerminated use of WinSock DLL.\n"));
	}

	return 0;
}