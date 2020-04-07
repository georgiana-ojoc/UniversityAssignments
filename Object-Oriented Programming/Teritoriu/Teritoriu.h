#pragma once

#include <iostream>
#include <string>

using namespace std;

class Teritoriu
{
protected:
	string nume;
	int suprafata;
public:
	Teritoriu();
	~Teritoriu();
	virtual void Afiseaza() = 0;
	virtual int CalculeazaSuprafata() = 0;
};