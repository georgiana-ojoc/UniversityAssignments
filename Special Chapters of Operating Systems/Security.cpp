#include <aclapi.h>
#include <lmcons.h>
#include <sddl.h>
#include <stdio.h>
#include <windows.h>

#define ERROR_MESSAGE_SIZE	512
#define KEY_NAME			"SOFTWARE\\CSSO"
#define SID_SIZE			256

LPCSTR getErrorMessage(LONG errorCode) {
	/*
	* Returns the error message from the specified error code.
	* Returns "Error message not found." otherwise.
	*/
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

int main() {
	/*
	* Creates the Everyone group SID as a Well-known SID.
	*/
	DWORD everyoneSIDSize = 0;
	CreateWellKnownSid(WinWorldSid,
		NULL,
		0,
		&everyoneSIDSize);
	PSID everyoneSID = (PSID)malloc((size_t(everyoneSIDSize) + 1) * sizeof(BYTE));
	BOOL returnValue = CreateWellKnownSid(WinWorldSid,
		NULL,
		everyoneSID,
		&everyoneSIDSize);
	if (!returnValue) {
		free(everyoneSID);
		DWORD lastError = GetLastError();
		printf(TEXT("\nFailed to create Everyone group SID.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
		return -1;
	}
	printf(TEXT("\nCreated Everyone group SID.\n"));

	EXPLICIT_ACCESS explicitAccess[2];
	memset(explicitAccess, NULL, sizeof(explicitAccess));

	/*
	* Creates the Access Control Entry for the Everyone group with Read access rights.
	*/
	explicitAccess[0].grfAccessPermissions = KEY_READ;
	explicitAccess[0].grfAccessMode = SET_ACCESS;
	explicitAccess[0].grfInheritance = NO_INHERITANCE;
	explicitAccess[0].Trustee.TrusteeForm = TRUSTEE_IS_SID;
	explicitAccess[0].Trustee.TrusteeType = TRUSTEE_IS_WELL_KNOWN_GROUP;
	explicitAccess[0].Trustee.ptstrName = (LPCH)everyoneSID;
	printf(TEXT("\nCreated Access Control Entry for Everyone group.\n"));

	/*
	* Gets current user name.
	*/
	DWORD userNameSize = 0;
	GetUserName(NULL,
		&userNameSize);
	LPSTR userName = (LPSTR)malloc((size_t(userNameSize) + 1) * sizeof(BYTE));
	returnValue = GetUserName(userName,
		&userNameSize);
	if (!returnValue) {
		free(everyoneSID);
		free(userName);
		DWORD lastError = GetLastError();
		printf(TEXT("\nFailed to get current user name.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
		return -1;
	}
	printf(TEXT("\nGot current user name: \"%s\".\n"), userName);

	/*
	* Gets the current user SID and domain name.
	*/
	DWORD userSIDSize = 0;
	SID_NAME_USE SIDType;
	DWORD domainNameSize = 0;
	LookupAccountName(NULL,
		userName,
		NULL,
		&userSIDSize,
		NULL,
		&domainNameSize,
		&SIDType);
	PSID userSID = (PSID)malloc((size_t(userSIDSize) + 1) * sizeof(BYTE));
	LPSTR domainName = (LPSTR)malloc((size_t(domainNameSize) + 1) * sizeof(BYTE));
	returnValue = LookupAccountName(NULL,
		userName,
		userSID,
		&userSIDSize,
		domainName,
		&domainNameSize,
		&SIDType);
	free(userName);
	if (!returnValue || !userSID) {
		free(everyoneSID);
		free(userSID);
		free(domainName);
		DWORD lastError = GetLastError();
		printf(TEXT("\nFailed to get current user SID.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
		return -1;
	}
	LPSTR userStringSID = (LPSTR)malloc((size_t(SID_SIZE) + 1) * sizeof(BYTE));
	returnValue = ConvertSidToStringSid(userSID,
		&userStringSID);
	if (!returnValue) {
		free(everyoneSID);
		free(userSID);
		free(domainName);
		DWORD lastError = GetLastError();
		printf(TEXT("\nFailed to get current user string SID.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
		return -1;
	}
	printf(TEXT("\nGot current user SID: \"%s\" and domain name: \"%s\".\n"), userStringSID, domainName);
	free(domainName);

	/*
	* Creates the Access Control Entry for the current user with Full Control access rights.
	*/
	explicitAccess[1].grfAccessPermissions = KEY_ALL_ACCESS;
	explicitAccess[1].grfAccessMode = SET_ACCESS;
	explicitAccess[1].grfInheritance = NO_INHERITANCE;
	explicitAccess[1].Trustee.TrusteeForm = TRUSTEE_IS_SID;
	explicitAccess[1].Trustee.TrusteeType = TRUSTEE_IS_USER;
	explicitAccess[1].Trustee.ptstrName = (LPCH)userSID;
	printf(TEXT("\nCreated Access Control Entry for current user.\n"));

	/*
	* Creates a new Access Control List with only the two Access Control Entries previously created.
	*/
	PACL newACL;
	LONG errorCode = SetEntriesInAcl(2,
		explicitAccess,
		NULL,
		&newACL);
	if (errorCode != ERROR_SUCCESS) {
		free(everyoneSID);
		free(userSID);
		free(newACL);
		printf(TEXT("\nFailed to create Access Control List.\nError code: %d\nError message: %s"),
			errorCode,
			getErrorMessage(errorCode));
		return -1;
	}
	printf(TEXT("\nCreated Access Control List.\n"));

	/*
	* Initializes a security descriptor.
	*/
	PSECURITY_DESCRIPTOR securityDescriptor = (PSECURITY_DESCRIPTOR)malloc((SECURITY_DESCRIPTOR_MIN_LENGTH + 1) * sizeof(BYTE));
	returnValue = InitializeSecurityDescriptor(securityDescriptor,
		SECURITY_DESCRIPTOR_REVISION);
	if (!returnValue || !securityDescriptor) {
		free(securityDescriptor);
		DWORD lastError = GetLastError();
		printf(TEXT("\nFailed to initialize security descriptor.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
		return -1;
	}
	printf(TEXT("\nInitialized security descriptor.\n"));

	/*
	* Sets the owner of the security descriptor as the current user previously got.
	*/
	returnValue = SetSecurityDescriptorOwner(securityDescriptor,
		userSID,
		FALSE);
	if (!returnValue) {
		free(securityDescriptor);
		DWORD lastError = GetLastError();
		printf(TEXT("\nFailed to set security descriptor owner.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
		return -1;
	}
	printf(TEXT("\nSet security descriptor owner.\n"));

	/*
	* Sets the Discretionary Access Control List of the security descriptor by adding the Access Control List previously created.
	*/
	returnValue = SetSecurityDescriptorDacl(securityDescriptor,
		TRUE,
		newACL,
		FALSE);
	if (!returnValue) {
		free(securityDescriptor);
		DWORD lastError = GetLastError();
		printf(TEXT("\nFailed to set security descriptor Discretionary Access Control List.\nError code: %d\nError message: %s"),
			lastError,
			getErrorMessage(lastError));
		return -1;
	}
	printf(TEXT("\nSet security descriptor Discretionary Access Control List.\n"));

	/*
	* Sets the security attributes.
	*/
	SECURITY_ATTRIBUTES securityAttributes;
	memset(&securityAttributes, NULL, sizeof(securityAttributes));
	securityAttributes.nLength = sizeof(SECURITY_ATTRIBUTES);
	securityAttributes.lpSecurityDescriptor = securityDescriptor;
	securityAttributes.bInheritHandle = FALSE;
	printf(TEXT("\nSet security attributes.\n"));

	/*
	* Creates a registry key with the security attributes previously set.
	*/
	HKEY key;
	DWORD disposition;
	errorCode = RegCreateKeyEx(HKEY_LOCAL_MACHINE,
		KEY_NAME,
		0,
		NULL,
		REG_OPTION_NON_VOLATILE,
		KEY_ALL_ACCESS,
		&securityAttributes,
		&key,
		&disposition);
	if (errorCode != ERROR_SUCCESS) {
		printf(TEXT("\nFailed to create registry key.\nError code: %d\nError message: %s"),
			errorCode,
			getErrorMessage(errorCode));
		return -1;
	}
	if (disposition == REG_CREATED_NEW_KEY) {
		printf(TEXT("\nCreated registry key.\n"));
	}
	else {
		printf(TEXT("\nOpened registry key.\n"));
	}

	free(securityAttributes.lpSecurityDescriptor);

	/*
	* Closes the registry key previously created.
	*/
	errorCode = RegCloseKey(key);
	if (errorCode != ERROR_SUCCESS) {
		printf(TEXT("\nFailed to close registry key.\nError code: %d\nError message: %s"),
			errorCode,
			getErrorMessage(errorCode));
	}
	else {
		printf(TEXT("\nClosed registry key.\n"));
	}

	return 0;
}