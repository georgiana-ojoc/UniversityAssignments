#pragma once
#include <iostream>
#include <string>
#include <vector>

using namespace std;

class Propozitie {
	string prop;
public:
	Propozitie(string str);
	void Set(string str);
	int operator[](string str);
	string operator[](int val);
	int operator()(int val);
};