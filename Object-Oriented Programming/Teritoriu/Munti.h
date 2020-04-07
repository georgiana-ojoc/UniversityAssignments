#pragma once

#include "Teritoriu.h"

class Munti : public Teritoriu
{
	int altitudine;
public:
	Munti(string numeNou, int suprafataNoua, int altitudineNoua);
	~Munti();
	void Afiseaza();
	int CalculeazaSuprafata();
};