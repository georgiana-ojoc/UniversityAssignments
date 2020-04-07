#pragma once

#include <string>

using namespace std;

class Masina
{
public:
	Masina();
	virtual ~Masina();
	virtual string getName() = 0;
};