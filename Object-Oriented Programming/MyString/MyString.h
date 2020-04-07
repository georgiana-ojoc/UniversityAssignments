#pragma once

#define _CRT_SECURE_NO_WARNINGS

#include <iostream>

class MyString
{
	char * sir;
	unsigned int AllocatedSize;
	unsigned int Size;
public:
	MyString();
	MyString(const char * text);
	~MyString();

	unsigned int GetSize();

	void Set(const char *text);
	void Set(MyString &m);

	void Add(const char *text);
	void Add(MyString &m);

	const char* GetText();

	MyString* SubString(unsigned int start, unsigned int size);

	bool Delete(unsigned int start, unsigned int size);

	int Compare(const char * text);
	int Compare(MyString &m);

	char GetChar(unsigned int index);

	bool Insert(unsigned int index, const char* text);

	bool Insert(unsigned int index, MyString &m);

	int Find(const char * text);

	int FindLast(const char * text);
};