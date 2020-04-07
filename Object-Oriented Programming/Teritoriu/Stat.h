#pragma once

#include "Munti.h"
#include "Teritoriu.h"
#include <vector>

class Stat : public Teritoriu
{
	vector<Munti> munti;
public:
	Stat(string numeNou, int suprafataNoua);
	~Stat();
	void Add(Munti munte);
	void Afiseaza();
	int CalculeazaSuprafata();
	vector<Munti>::iterator begin();
	vector<Munti>::iterator end();
};