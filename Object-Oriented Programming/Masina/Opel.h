#pragma once

#include "MasinaOras.h"

class Opel : public MasinaOras
{
	int capacitate;
	string culoare;
	int anFabricatie;
public:
	Opel();
	~Opel();
	string getName();
	void setCapacitate(int capacitateNoua);
	void setCuloare(string culoareNoua);
	void setAnFabricatie(int anFabricatieNou);
	int getCapacitate();
	string getCuloare();
	int getAnFabricatie();
};