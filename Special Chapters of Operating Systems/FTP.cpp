#include <list>
#include <map>
#include <sstream>
#include <string>
#include <utility>
#include <windows.h>
#include <wininet.h>

#define BUFFER_SIZE			256
#define ERROR_MESSAGE_SIZE	512
#define FILE				TEXT("~georgiana.ojoc/CSSO/initialization.txt")
#define GET					TEXT("GET")
#define HTTP_SERVER			TEXT("students.info.uaic.ro")
#define PUT					TEXT("PUT")
#define RUN					TEXT("RUN")

#define CLOSE_HANDLE(handle) \
	if (!CloseHandle(handle)) { \
		printf(TEXT("\nCould not close handle.\nError code: %d\nError message: %s\n"), \
			GetLastError(), \
			getErrorMessage(GetLastError())); \
	}

#define INTERNET_CLOSE_HANDLE(handle) \
	if (!InternetCloseHandle(handle)) { \
		printf(TEXT("\nCould not close internet handle.\nError code: %d\nError message: %s\n"), \
			GetLastError(), \
			getErrorMessage(GetLastError())); \
	}

using namespace std;

LPCSTR getErrorMessage(DWORD errorCode) {
	/*
	* Returns the error message from the specified error code.
	* Returns "Error message not found." otherwise.
	*/
	LPSTR errorMessage = new char[ERROR_MESSAGE_SIZE + 1];
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

LPCSTR getInternetErrorMessage() {
	/*
	* Returns the internet error message from the specified error code.
	* Returns "Error message not found." otherwise.
	*/
	DWORD errorCode;
	DWORD length = ERROR_MESSAGE_SIZE;
	LPSTR errorMessage = new char[length + 1];
	if (InternetGetLastResponseInfo(&errorCode, errorMessage, &length)) {
		return errorMessage;
	}
	return TEXT("Error message not found.");
}


BOOL getCommands(map<string, string>& credentials, list<pair<string, string>>& commands) {
	/*
	* Connects to HTTP server.
	* Reads file from server.
	* Gets FTP server name, username, password and command list from file.
	*/
	HINTERNET internetHandle = InternetOpen("", INTERNET_OPEN_TYPE_PRECONFIG, NULL, NULL, 0);
	if (internetHandle == NULL) {
		DWORD errorCode = GetLastError();
		printf(TEXT("\nCould not initialize use of WinInet functions.\nError code: %d\nError message: %s\n"),
			errorCode,
			getInternetErrorMessage());
		return FALSE;
	}
	HINTERNET serverHandle = InternetConnect(internetHandle, HTTP_SERVER, INTERNET_DEFAULT_HTTP_PORT, NULL, NULL, INTERNET_SERVICE_HTTP, 0, NULL);
	if (serverHandle == NULL) {
		DWORD errorCode = GetLastError();
		printf(TEXT("\nCould not connect to \"%s\".\nError code: %d\nError message: %s\n"),
			HTTP_SERVER,
			errorCode,
			getInternetErrorMessage());
		INTERNET_CLOSE_HANDLE(internetHandle);
		return FALSE;
	}
	LPCSTR acceptTypes[] = { TEXT("text/*"), NULL };
	HINTERNET openRequestHandle = HttpOpenRequest(serverHandle, GET, FILE, NULL, NULL, acceptTypes, 0, NULL);
	if (openRequestHandle == NULL) {
		DWORD errorCode = GetLastError();
		printf(TEXT("\nCould not open request.\nError code: %d\nError message: %s\n"),
			errorCode,
			getInternetErrorMessage());
		INTERNET_CLOSE_HANDLE(serverHandle);
		INTERNET_CLOSE_HANDLE(internetHandle);
		return FALSE;
	}
	if (!HttpSendRequest(openRequestHandle, NULL, 0, NULL, 0)) {
		DWORD errorCode = GetLastError();
		printf(TEXT("\nCould not send request.\nError code: %d\nError message: %s\n"),
			errorCode,
			getInternetErrorMessage());
		INTERNET_CLOSE_HANDLE(openRequestHandle);
		INTERNET_CLOSE_HANDLE(serverHandle);
		INTERNET_CLOSE_HANDLE(internetHandle);
		return FALSE;
	}
	string file;
	LPSTR buffer = new CHAR[BUFFER_SIZE];
	DWORD readBytes;
	while (TRUE) {
		if (!InternetReadFile(openRequestHandle, buffer, BUFFER_SIZE, &readBytes)) {
			DWORD errorCode = GetLastError();
			printf(TEXT("\nCould not read file.\nError code: %d\nError message: %s\n"),
				errorCode,
				getInternetErrorMessage());
			INTERNET_CLOSE_HANDLE(openRequestHandle);
			INTERNET_CLOSE_HANDLE(serverHandle);
			INTERNET_CLOSE_HANDLE(internetHandle);
			return FALSE;
		}
		if (readBytes) {
			buffer[readBytes] = '\0';
			file += buffer;
		}
		else {
			break;
		}
	}
	stringstream information(file);
	string line;
	WORD index = 1;
	credentials.clear();
	commands.clear();
	if (!getline(information, line)) {
		printf(TEXT("\nFile is empty.\n"));
		return FALSE;
	}
	while (getline(information, line)) {
		if (index == 1) {
			credentials["server"] = line;
		}
		else if (index == 2) {
			credentials["username"] = line;
		}
		else if (index == 3) {
			credentials["password"] = line;
		}
		else {
			stringstream command(line);
			string name;
			if (getline(command, name, ' ')) {
				string parameter;
				if (getline(command, parameter)) {
					commands.push_back(pair<string, string>(name, parameter));
				}
			}
		}
		index++;
	}
	return TRUE;
}

BOOL executeCommands(map<string, string> credentials, list<pair<string, string>> commands) {
	/*
	* Connects to FTP server.
	* Puts files on server.
	* Gets and runs files from server.
	*/
	HINTERNET internetHandle = InternetOpen("", INTERNET_OPEN_TYPE_PRECONFIG, NULL, NULL, 0);
	if (internetHandle == NULL) {
		DWORD errorCode = GetLastError();
		printf(TEXT("\nCould not initialize use of WinInet functions.\nError code: %d\nError message: %s\n"),
			errorCode,
			getInternetErrorMessage());
		return FALSE;
	}
	HINTERNET serverHandle = InternetConnect(internetHandle, credentials["server"].c_str(), INTERNET_DEFAULT_FTP_PORT, credentials["username"].c_str(), credentials["password"].c_str(), INTERNET_SERVICE_FTP, 0, NULL);
	if (serverHandle == NULL) {
		DWORD errorCode = GetLastError();
		printf(TEXT("\nCould not connect to \"%s\".\nError code: %d\nError message: %s\n"),
			credentials["server"].c_str(),
			errorCode,
			getInternetErrorMessage());
		INTERNET_CLOSE_HANDLE(internetHandle);
		return FALSE;
	}
	for (pair<string, string> command : commands) {
		if (command.first == PUT) {
			if (!FtpPutFile(serverHandle, command.second.c_str(), command.second.substr(command.second.find_last_of('\\') + 1).c_str(), FTP_TRANSFER_TYPE_BINARY, NULL)) {
				DWORD errorCode = GetLastError();
				printf(TEXT("\nCould not put \"%s\" to \"%s\".\nError code: %d\nError message: %s\n"),
					command.second.c_str(),
					credentials["server"].c_str(),
					errorCode,
					getInternetErrorMessage());
			}
		}
		else if (command.first == RUN) {
			if (!FtpGetFile(serverHandle, command.second.c_str(), command.second.c_str(), FALSE, FILE_ATTRIBUTE_NORMAL, FTP_TRANSFER_TYPE_BINARY, NULL)) {
				DWORD errorCode = GetLastError();
				printf(TEXT("\nCould not get \"%s\" from \"%s\".\nError code: %d\nError message: %s\n"),
					command.second.c_str(),
					credentials["server"].c_str(),
					errorCode,
					getInternetErrorMessage());
			}
			else {
				PROCESS_INFORMATION processInformation;
				STARTUPINFO startupInformation;
				ZeroMemory(&startupInformation, sizeof(startupInformation));
				startupInformation.cb = sizeof(startupInformation);
				if (!CreateProcess(command.second.c_str(),
					NULL,
					NULL,
					NULL,
					FALSE,
					CREATE_NEW_CONSOLE,
					NULL,
					NULL,
					&startupInformation,
					&processInformation)) {
					printf(TEXT("\nCould not run \"%s\".\nError code: %d\nError message: %s"),
						command.second.c_str(),
						GetLastError(),
						getErrorMessage(GetLastError()));
				}
				WaitForSingleObject(processInformation.hProcess, INFINITE);
				CLOSE_HANDLE(processInformation.hProcess);
				CLOSE_HANDLE(processInformation.hThread);
			}
		}
	}
	INTERNET_CLOSE_HANDLE(serverHandle);
	INTERNET_CLOSE_HANDLE(internetHandle);
	return TRUE;
}

int main() {
	/*
	* Shows program functionality.
	*/
	map<string, string> credentials;
	list<pair<string, string>> commands;
	if (!getCommands(credentials, commands)) {
		return -1;
	}
	if (!executeCommands(credentials, commands)) {
		return -1;
	}
	return 0;
}
