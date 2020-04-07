#pragma once

#include "Munti.h"
#include "Stat.h"
#include "Teritoriu.h"
#include <vector>

class Continent : public Teritoriu
{
	vector<Stat> state;
public:
	Continent(string numeNou);
	~Continent();
	void Add(Stat stat);
	void Afiseaza();
	int CalculeazaSuprafata();
	void AfiseazaStateCuMunti();
};