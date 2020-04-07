#include <fstream>
#include <iostream>
#include <set>
#include <string>

using namespace std;

unsigned int stringToUnsignedInt(string number) {
	unsigned int value = 0;
	unsigned int numberIndex;
	for (numberIndex = 0; numberIndex < number.size(); numberIndex++) {
		value = value * 10 + number[numberIndex] - '0';
	}
	return value;
}

int main() {
	ifstream file("numbers.txt");
	// string content { istreambuf_iterator<char>(file), istreambuf_iterator<char>() };
	string content;
	file >> content;
	unsigned int contentIndex;
	file.close();
	set<unsigned int> numbers;
	set<unsigned int>::iterator numbersIterator;
	unsigned int value;
	string number;
	unsigned int start = 0;
	int position = content.find(",", start);
	while (position != content.npos) {
		number = content.substr(start, position - start);
		value = stringToUnsignedInt(number);
		numbers.insert(value);
		start = position + 1;
		position = content.find(",", start);
	}
	number = content.substr(start, content.length());
	value = stringToUnsignedInt(number);
	numbers.insert(value);
	/*
	for (contentIndex = 0; contentIndex < content.size(); contentIndex++) {
		if (content[contentIndex] != ',') {
			number += content[contentIndex];
		}
		else {
			value = stringToUnsignedInt(number);
			numbers.insert(value);
			number.clear();
		}
	}
	*/
	for (numbersIterator = numbers.begin(); numbersIterator != numbers.end(); numbersIterator++) {
		cout << *numbersIterator << ' ';
	}
	cout << endl;
}