#include "BigNumber.h"

using namespace std;

BigNumber::BigNumber() {
	memset(Number, NULL, 256);
	CharactersCount = 1;
}

BigNumber::BigNumber(int value) {
	memset(Number, NULL, 256);
	Set(value);
}

BigNumber::BigNumber(const char * number) {
	memset(Number, NULL, 256);
	Set(number);
}

BigNumber::BigNumber(const BigNumber & number) {
	memset(Number, NULL, 256);
	CharactersCount = number.CharactersCount;
	memcpy(Number, number.Number, CharactersCount);
}

BigNumber::~BigNumber() {}

bool BigNumber::Set(int value) {
	CharactersCount = 0;
	if (value == 0) {
		Number[CharactersCount++] = 0;
	}
	while (value) {
		Number[CharactersCount++] = value % 10;
		value /= 10;
	}
	return true;
}

bool BigNumber::Set(const char * number) {
	int length = strlen(number);
	if (length > 256) {
		return false;
	}
	CharactersCount = 0;
	if (number[0] >= '0') {
		while (CharactersCount < length) {
			Number[CharactersCount] = number[length - CharactersCount - 1] - '0';
			CharactersCount++;
		}
	}
	else {
		while (CharactersCount < length) {
			Number[CharactersCount] = number[length - CharactersCount - 1];
			CharactersCount++;
		}
	}
	return true;
}

bool operator == (const BigNumber & n1, const BigNumber & n2) {
	if (n1.CharactersCount != n2.CharactersCount) {
		return false;
	}
	return memcmp(n1.Number, n2.Number, n1.CharactersCount) ? false : true;
}

bool operator != (const BigNumber & n1, const BigNumber & n2) {
	if (n1.CharactersCount != n2.CharactersCount) {
		return true;
	}
	return memcmp(n1.Number, n2.Number, n1.CharactersCount) ? true : false;
}

bool operator < (const BigNumber & n1, const BigNumber & n2) {
	if (n1.CharactersCount < n2.CharactersCount) {
		return true;
	}
	if (n1.CharactersCount > n2.CharactersCount) {
		return false;
	}
	int index = n1.CharactersCount - 1;
	for (; index >= 0 && n1.Number[index] == n2.Number[index]; index--)
		;
	return n1.Number[index] < n2.Number[index];
}

bool operator > (const BigNumber & n1, const BigNumber & n2) {
	if (n1.CharactersCount > n2.CharactersCount) {
		return true;
	}
	if (n1.CharactersCount < n2.CharactersCount) {
		return false;
	}
	int index = n1.CharactersCount - 1;
	for (; index >= 0 && n1.Number[index] == n2.Number[index]; index--)
		;
	return n1.Number[index] > n2.Number[index];
}

bool operator <= (const BigNumber & n1, const BigNumber & n2)
{
	if (n1.CharactersCount < n2.CharactersCount) {
		return true;
	}
	if (n1.CharactersCount > n2.CharactersCount) {
		return false;
	}
	int index = n1.CharactersCount - 1;
	for (; index >= 0 && n1.Number[index] == n2.Number[index]; index--)
		;
	return n1.Number[index] <= n2.Number[index];
}

bool operator >= (const BigNumber & n1, const BigNumber & n2)
{
	if (n1.CharactersCount > n2.CharactersCount) {
		return true;
	}
	if (n1.CharactersCount < n2.CharactersCount) {
		return false;
	}
	int index = n1.CharactersCount - 1;
	for (; index >= 0 && n1.Number[index] == n2.Number[index]; index--)
		;
	return n1.Number[index] >= n2.Number[index];
}

BigNumber BigNumber::operator + (const BigNumber & number) {
	int index1, index2, index3;
	int digit1, digit2, digit3;
	BigNumber result;
	index3 = 0;
	result.CharactersCount = 0;
	for (index1 = 0, index2 = 0; index1 < CharactersCount || index2 < number.CharactersCount || index3; index1++, index2++) {
		digit1 = digit2 = 0;
		if (Number[index1]) {
			digit1 = Number[index1];
		}
		if (number.Number[index2]) {
			digit2 = number.Number[index2];
		}
		digit3 = digit1 + digit2 + index3;
		result.Number[result.CharactersCount++] = digit3 % 10;
		index3 = digit3 / 10;
	}
	return result;
}

BigNumber BigNumber::operator - (const BigNumber & number) {
	int index1, index2, digit1, digit2;
	BigNumber result;
	index2 = 0;
	result.CharactersCount = 0;
	for (index1 = 0; index1 < CharactersCount; index1++) {
		digit1 = digit2 = 0;
		if (Number[index1]) {
			digit1 = Number[index1];
		}
		if (number.Number[index1]) {
			digit2 = number.Number[index1];
		}
		result.Number[result.CharactersCount] = digit1 - digit2 + index2;
		if (result.Number[result.CharactersCount] < 0) {
			result.Number[result.CharactersCount] += 10;
			index2 = -1;
		}
		else {
			index2 = 0;
		}
		result.Number[result.CharactersCount] = result.Number[result.CharactersCount];
		result.CharactersCount++;
	}
	index1--;
	while (index1 && result.Number[index1] == 0) {
		index1--;
	}
	result.CharactersCount = index1 + 1;
	return result;
}

BigNumber BigNumber::operator * (const BigNumber & number) {
	BigNumber result(*this);
	BigNumber index(1);
	BigNumber unu(1);
	while (index < number)
	{
		result = result + (*this);
		index = index + unu;
	}
	return result;
}

BigNumber BigNumber::operator / (const BigNumber & number)
{
	BigNumber result(*this);
	if (result < number) {
		return result;
	}
	BigNumber cat;
	BigNumber unu(1);
	while (result > number)
	{
		result = result - number;
		cat = cat + unu;
	}
	return cat;
}

BigNumber::operator int () {
	int result = 0;
	for (int i = CharactersCount - 1; i >= 0; i--) {
		result = result * 10 + Number[i];
	}
	return result;
}

char BigNumber::operator [] (int index) {
	if (index < 0 || index > CharactersCount) {
		return 0;
	}
	return Number[CharactersCount - index - 1] + '0';
}

BigNumber BigNumber::operator () (int start, int end) {
	BigNumber result;
	if (start < 1 || start > CharactersCount || end < 1 || end > CharactersCount || start > end) {
		return result;
	}
	char number[256];
	memset(number, NULL, 256);
	_strrev(Number);
	memcpy(number, Number + start - 1, end - start + 1);
	_strrev(Number);
	result.Set(number);
	return result;
}

void BigNumber::print() {
	for (int index = CharactersCount - 1; index >= 0; index--) {
		cout << (int)Number[index];
	}
	cout << endl;
}