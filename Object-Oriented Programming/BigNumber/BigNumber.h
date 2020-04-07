#define _CRT_SECURE_NO_WARNINGS

#include <iostream>
#include <stdio.h>
#include <string.h>

class BigNumber {

private:

    char Number[256];
    int  CharactersCount;

public:

    BigNumber();
    BigNumber(int value);
    BigNumber(const char * number);
    BigNumber(const BigNumber & number);
	~BigNumber();

    bool Set(int value);
    bool Set(const char * number);

    friend bool operator == (const BigNumber & n1, const BigNumber & n2);
    friend bool operator != (const BigNumber & n1, const BigNumber & n2);
    friend bool operator <  (const BigNumber & n1, const BigNumber & n2);
    friend bool operator >  (const BigNumber & n1, const BigNumber & n2);
    friend bool operator <= (const BigNumber & n1, const BigNumber & n2);
	friend bool operator >= (const BigNumber & n1, const BigNumber & n2);

	BigNumber operator + (const BigNumber & number);
	BigNumber operator - (const BigNumber & number);
	BigNumber operator * (const BigNumber & number);
	BigNumber operator / (const BigNumber & number);

    operator int ();
    char operator [] (int index);
    BigNumber operator () (int start, int end);

    void print();
};