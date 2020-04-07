#include "MyString.h"

using namespace std;

MyString::MyString() {
	sir = new char[16];
	AllocatedSize = 16;
	Size = 0;
}

MyString::MyString(const char * text) {
	unsigned int length = strlen(text);
	if (length >= AllocatedSize) {
		delete[] sir;
		char * aux = new char[length + 1];
		sir = aux;
	}
	AllocatedSize = length + 1;
	Size = length;
}

MyString::~MyString() {
	delete[] sir;
}

unsigned int MyString::GetSize() {
	return Size;
}

void MyString::Set(const char * text) {
	unsigned int length = strlen(text);
	if (length < AllocatedSize) {
		strcpy(sir, text);
	}
	else {
		delete[] sir;
		char * aux = new char[length + 1];
		strcpy(aux, text);
		sir = aux;
	}
	AllocatedSize = length + 1;
	Size = length;
}

void MyString::Set(MyString &m) {
	Set(m.sir);
}

void MyString::Add(const char *text) {
	unsigned int length = strlen(text);
	if (length < AllocatedSize - Size) {
		strcat(sir, text);
	}
	else {
		char * copy;
		copy = _strdup(sir);
		delete[] sir;
		char * aux = new char[Size + length + 1];
		strcpy(aux, copy);
		delete[] copy;
		strcat(aux, text);
		sir = _strdup(aux);
		delete[] aux;
		AllocatedSize += length;
	}
	Size += length;
}

void MyString::Add(MyString &m) {
	Add(m.sir);
}

const char * MyString::GetText() {
	return sir;
}

MyString * MyString::SubString(unsigned int start, unsigned int size) {
	start--;
	if (start < 0 || start >= Size || size < 1 || start + size > Size) {
		return NULL;
	}
	char * aux = new char[size + 1];
	strncpy(aux, sir + start, size);
	aux[size] = '\0';
	MyString * substring = new MyString;
	substring->Set(aux);
	return substring;
}

bool MyString::Delete(unsigned int start, unsigned int size) {
	start--;
	if (start < 0 || start >= Size || size < 1 || start + size > Size) {
		return false;
	}
	strcpy(sir + start, sir + start + size);
	AllocatedSize -= size;
	Size -= size;
	return true;
}

int MyString::Compare(const char * text) {
	return strcmp(sir, text);
}

int MyString::Compare(MyString &m) {
	return Compare(m.sir);
}

char MyString::GetChar(unsigned int index) {
	if (index >= Size) {
		return 0;
	}
	return sir[index];
}

bool MyString::Insert(unsigned int index, const char* text) {
	index--;
	if (index >= Size) {
		return false;
	}
	unsigned int length = strlen(text);
	if (length < AllocatedSize - Size) {
		char * aux = new char[Size - index];
		aux = _strdup(sir + index);
		sir[index] = '\0';
		strcat(sir, text);
		strcat(sir, aux);
		delete[] aux;
	}
	else {
		char * aux1 = new char[Size + length + 1];
		aux1 = sir;
		char * aux2 = new char[Size - index];
		aux2 = _strdup(aux1 + index);
		aux1[index] = '\0';
		strcat(aux1, text);
		strcat(aux1, aux2);
		delete[] aux2;
		sir = aux1;
		AllocatedSize = Size + length + 1;
	}
	Size += length;
	return true;
}

bool MyString::Insert(unsigned int index, MyString &m) {
	return Insert(index, m.sir);
}

int MyString::Find(const char * text) {
	char * aux = strstr(sir, text);
	if (aux) {
		return aux - sir + 1;
	}
	return -1;
}

int MyString::FindLast(const char * text) {
	if (Find(text) == -1) {
		return -1;
	}
	char * aux1 = strstr(sir, text);
	char * aux2 = strstr(aux1 + 1, text);
	while (aux2) {
		aux1 = aux2;
		aux2 = strstr(aux1 + 1, text);
	}
	return aux1 - sir + 1;
}