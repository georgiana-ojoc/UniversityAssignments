#include <stdio.h>
#include <windows.h>

#pragma comment(lib, TEXT("ws2_32.lib"))

#define BUFFER_SIZE			2048
#define ERROR_MESSAGE_SIZE	512

#define HOST				TEXT("127.0.0.1")
#define PORT				3000

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

void printCommandArguments() {
	printf(TEXT("Command arguments:\n"));
	printf(TEXT(" - %-19s %-15s         : creates a file with the specified name in the current directory\n"), "create_file", "<name>");
	printf(TEXT(" - %-19s %-15s \"%-6s\": appends the specified text in the file with the specified name from the current directory\n"), "append_file", "<name>", "<text>");
	printf(TEXT(" - %-19s %-15s         : deletes the file with the specified name from the current directory\n"), "delete_file", "<name>");
	printf(TEXT(" - %-19s %-15s         : creates a registry key with the specified absolute path\n"), "create_registry_key", "<absolute path>");
	printf(TEXT(" - %-19s %-15s         : deletes the registry key with the specified absolute path\n"), "delete_registry_key", "<absolute path>");
	printf(TEXT(" - %-19s %-15s         : executes the application from the specified absolute path\n"), "run", "<absolute path>");
	printf(TEXT(" - %-19s %-15s \"%-6s\": downloads the content from the specified link in a file with the specified name\n"), "download", "<name>", "<link>");
	printf(TEXT(" - %-19s %-15s         : lists the directory from the specified absolute path\n"), "list", "<absolute path>");
}

int main(int argumentCount, char* argumentValues[]) {
	if (argumentCount < 2) {
		printCommandArguments();
		return -1;
	}

	char buffer[BUFFER_SIZE];
	strcpy(buffer, argumentValues[1]);
	for (int index = 2; index < argumentCount; index++) {
		strcat(buffer, " ");
		strcat(buffer, argumentValues[index]);
	}

	WSADATA data;
	int result = WSAStartup(MAKEWORD(2, 2), &data);
	if (result) {
		printf(TEXT("\nFailed to initiate use of WinSock DLL.\nError code: %d\nError message: %s"),
			result,
			getErrorMessage(result));
		return -1;
	}
	printf(TEXT("\nInitiated use of WinSock DLL.\n"));

	SOCKET client = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
	if (client == INVALID_SOCKET) {
		int lastError = WSAGetLastError();
		printf(TEXT("\nFailed to create client socket.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
		WSACleanup();
		return -1;
	}
	printf(TEXT("\nCreated client socket.\n"));

	sockaddr_in address;
	address.sin_family = AF_INET;
	address.sin_addr.s_addr = inet_addr(HOST);
	address.sin_port = htons(PORT);
	int sentBytes = sendto(client, buffer, BUFFER_SIZE - 1, 0, (SOCKADDR*)&address, sizeof(address));
	if (sentBytes == SOCKET_ERROR) {
		int lastError = WSAGetLastError();
		printf(TEXT("\nFailed to send datagram.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
	}
	else {
		printf(TEXT("\nSent datagram: %s.\n"), buffer);
	}

	result = closesocket(client);
	if (!result) {
		printf(TEXT("\nClosed client socket.\n"));
	}
	result = WSACleanup();
	if (!result) {
		printf(TEXT("\nTerminated use of WinSock DLL.\n"));
	}

	return 0;
}