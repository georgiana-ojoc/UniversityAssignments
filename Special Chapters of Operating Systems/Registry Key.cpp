#include <cstring>
#include <stdio.h>
#include <windows.h>

#define ERROR_MESSAGE_SIZE	512
#define KEY_SIZE			512
#define PARTITION			"D:"

BOOL parseArguments(int argumentNumber, char* arguments[], HKEY& hive, LPSTR& directoryPath) {
	/*
	* Extracts the hive type and builds the root path, starting from the D partition.
	* Returns FALSE if no arguments were specified, TRUE otherwise.
	*/
	if (argumentNumber < 2) {
		printf(TEXT("Specify the registry key."));
		return FALSE;
	}
	LPSTR input = new CHAR[KEY_SIZE];
	strcpy(input, arguments[1]);
	for (WORD index = 2; index < argumentNumber; index++) {
		strcat(input, " ");
		strcat(input, arguments[index]);
	}
	SIZE_T inputSize = strlen(input);
	SIZE_T firstBackSlash = 0;
	while (firstBackSlash < inputSize && input[firstBackSlash] != '\\') {
		firstBackSlash++;
	}
	LPSTR hiveName = new CHAR[firstBackSlash + 1];
	if (firstBackSlash == inputSize) {
		strcpy(hiveName, input);
		printf(TEXT("Hive: %s\n"), hiveName);
	}
	else {
		strncpy(hiveName, input, firstBackSlash);
		hiveName[firstBackSlash] = '\0';
		printf(TEXT("Hive: %s\n"), hiveName);
		strcat(directoryPath, "\\");
		strcat(directoryPath, input + firstBackSlash + 1);
		printf(TEXT("Key: %s\n"), directoryPath + 3);
	}
	delete[] input;
	hive = HKEY_CLASSES_ROOT;
	if (strcmp(hiveName, TEXT("HKEY_CURRENT_USER")) == 0) {
		hive = HKEY_CURRENT_USER;
	}
	else if (strcmp(hiveName, TEXT("HKEY_LOCAL_MACHINE")) == 0) {
		hive = HKEY_LOCAL_MACHINE;
	}
	else if (strcmp(hiveName, TEXT("HKEY_USERS")) == 0) {
		hive = HKEY_USERS;
	}
	else if (strcmp(hiveName, TEXT("HKEY_CURRENT_CONFIG")) == 0) {
		hive = HKEY_CURRENT_CONFIG;
	}
	delete[] hiveName;
	return TRUE;
}

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

BOOL createDirectories(LPCSTR path) {
	/*
	* Recursively creates, if not exist, the directories from the specified path.
	* Returns FALSE if a directory could not be created, TRUE otherwise.
	*/
	SIZE_T pathSize = strlen(path);
	SIZE_T previousBackSlash = 2;
	LPSTR newPath = new CHAR[strlen(path) + 3];
	strcpy(newPath, PARTITION);
	for (SIZE_T index = strlen(PARTITION) + 1; index <= pathSize; index++) {
		if (index == pathSize || path[index] == '\\') {
			if (previousBackSlash + 1 < index) {
				strcat(newPath, "\\");
				strncat(newPath, path + previousBackSlash + 1, index - previousBackSlash - 1);
				if (!CreateDirectory(newPath, nullptr)) {
					DWORD errorCode = GetLastError();
					if (errorCode != ERROR_ALREADY_EXISTS) {
						printf(TEXT("\n(%d) %s"), errorCode, getErrorMessage(errorCode));
						return FALSE;
					}
					printf(TEXT("The directory %s was created.\n"), newPath);
				}
			}
			previousBackSlash = index;
		}
	}
	delete[] newPath;
	return TRUE;
}

VOID printLastWriteTime(FILETIME lastWriteTime) {
	/*
	* Prints the system time derived from the specified file time.
	*/
	SYSTEMTIME systemTime;
	if (FileTimeToSystemTime(&lastWriteTime, &systemTime)) {
		printf(TEXT("The last write time is: %02d.%02d.%d, %02d:%02d:%02d.\n"),
			systemTime.wDay,
			systemTime.wMonth,
			systemTime.wYear,
			systemTime.wHour,
			systemTime.wMinute,
			systemTime.wSecond);
	}
}

BOOL openKey(HKEY& parentKey, LPCSTR keyName) {
	/*
	* Opens the specified key with the help of the specified parent key.
	* Returns TRUE if successful, FALSE otherwise.
	*/
	HKEY key;
	LONG errorCode = RegOpenKeyEx(parentKey,
		keyName,
		0,
		KEY_READ,
		&key);
	if (errorCode != ERROR_SUCCESS) {
		printf(TEXT("\n(%d) %s"), errorCode, getErrorMessage(errorCode));
		return FALSE;
	}
	parentKey = key;
	return TRUE;
}

VOID closeKey(HKEY key) {
	/*
	* Closes the specified key.
	* Returns TRUE if successful, FALSE otherwise.
	*/
	LONG errorCode = RegCloseKey(key);
	if (errorCode != ERROR_SUCCESS) {
		printf(TEXT("\n(%d) %s"), errorCode, getErrorMessage(errorCode));
	}
}

BOOL queryKey(HKEY key, LPSTR directoryPath, LPCSTR keyName, WORD level, BOOL isRoot = FALSE) {
	/*
	* Creates the root directories, if not exist.
	* Creates the directory corresponding to the specified key, if not exist.
	* Gets the values of the specified key and creates corresponding files, if not exists.
	* Gets the sub keys of the specified key and recursively calls the current function for each sub key.
	* Returns TRUE if successful, FALSE otherwise.
	*/
	if (!openKey(key, keyName)) {
		return FALSE;
	}
	LONG errorCode;
	if (isRoot) {
		if (!createDirectories(directoryPath)) {
			return FALSE;
		}
	}
	else {
		if (!CreateDirectory(directoryPath, nullptr)) {
			DWORD errorCode = GetLastError();
			if (errorCode != ERROR_ALREADY_EXISTS) {
				printf(TEXT("\n(%d) %s"), errorCode, getErrorMessage(errorCode));
				return FALSE;
			}
			printf(TEXT("The directory %s was created.\n"), directoryPath);
		}
	}
	DWORD subKeyNumber;
	DWORD subKeyMaxLength;
	DWORD valueNumber;
	DWORD valueNameMaxLength;
	DWORD valueMaxLength;
	FILETIME lastWriteTime;
	errorCode = RegQueryInfoKey(key,
		nullptr,
		nullptr,
		nullptr,
		&subKeyNumber,
		&subKeyMaxLength,
		nullptr,
		&valueNumber,
		&valueNameMaxLength,
		&valueMaxLength,
		nullptr,
		&lastWriteTime);
	if (errorCode != ERROR_SUCCESS) {
		printf(TEXT("\n(%d) %s"), errorCode, getErrorMessage(errorCode));
		return FALSE;
	}
	printLastWriteTime(lastWriteTime);
	LPSTR valueName = (LPSTR)malloc((SIZE_T(valueNameMaxLength) + 1) * sizeof(CHAR));
	LPBYTE value = (LPBYTE)malloc((SIZE_T(valueMaxLength) + 1) * sizeof(CHAR));
	for (WORD index = 0; index < valueNumber; index++) {
		DWORD valueNameLength = valueNameMaxLength + 1;
		DWORD valueType;
		DWORD valueLength = valueMaxLength;
		errorCode = RegEnumValue(key,
			index,
			valueName,
			&valueNameLength,
			nullptr,
			&valueType,
			value,
			&valueLength);
		if (errorCode == ERROR_SUCCESS) {
			for (WORD index = 0; index < level; index++) {
				printf(TEXT("   "));
			}
			printf(TEXT("Value %d: %s"), index + 1, valueName);
			switch (valueType) {
			case REG_SZ: {
				printf(TEXT(" = %s\n"), (LPCSTR)value);
				break;
			}
			case REG_DWORD: {
				printf(TEXT(" = %d\n"), *(LPDWORD)value);
				break;
			}
			default: {
				printf(TEXT("\n"));
			}
			}
			LPSTR filePath = new CHAR[KEY_SIZE];
			strcpy(filePath, directoryPath);
			strcat(filePath, "\\");
			strcat(filePath, valueName);
			HANDLE file = CreateFile(filePath,
				GENERIC_READ,
				0,
				nullptr,
				OPEN_ALWAYS,
				FILE_ATTRIBUTE_NORMAL,
				nullptr);
			if (file == INVALID_HANDLE_VALUE) {
				DWORD errorCode = GetLastError();
				printf(TEXT("\n(%d) %s"), errorCode, getErrorMessage(errorCode));
			}
			else {
				printf(TEXT("The file %s was created.\n"), filePath);
			}
			if (!CloseHandle(file)) {
				DWORD errorCode = GetLastError();
				printf(TEXT("\n(%d) %s"), errorCode, getErrorMessage(errorCode));
			}
		}
		else {
			printf(TEXT("\n(%d) %s"), errorCode, getErrorMessage(errorCode));
		}
	}
	free(value);
	free(valueName);
	for (WORD index = 0; index < subKeyNumber; index++) {
		LPSTR subKeyName = new CHAR[SIZE_T(subKeyMaxLength) + 1];
		DWORD subKeyLength = subKeyMaxLength + 1;
		errorCode = RegEnumKeyEx(key,
			index,
			subKeyName,
			&subKeyLength,
			nullptr,
			nullptr,
			nullptr,
			&lastWriteTime);
		if (errorCode == ERROR_SUCCESS) {
			for (WORD index = 0; index < level; index++) {
				printf(TEXT("   "));
			}
			printf(TEXT("Key %d: %s\n"), index + 1, subKeyName);
			LPSTR directoryPathCopy = new CHAR[KEY_SIZE];
			strcpy(directoryPathCopy, directoryPath);
			strcat(directoryPathCopy, "\\");
			strcat(directoryPathCopy, subKeyName);
			queryKey(key,
				directoryPathCopy,
				subKeyName,
				level + 1);
			delete[] directoryPathCopy;
			delete[] subKeyName;
		}
		else {
			printf(TEXT("\n(%d) %s"), errorCode, getErrorMessage(errorCode));
		}
	}
	closeKey(key);
	return TRUE;
}

int main(int argumentCount, char* argumentValues[]) {
	/*
	* Exemplifies the functionality of the program.
	*/
	HKEY hive;
	LPSTR directoryPath = new CHAR[KEY_SIZE];
	strcpy(directoryPath, PARTITION);
	if (!parseArguments(argumentCount, argumentValues, hive, directoryPath)) {
		return -1;
	}
	if (!queryKey(hive, directoryPath, directoryPath + strlen(PARTITION) + 1, 1, TRUE)) {
		return -1;
	}
	delete[] directoryPath;
	return 0;
}
