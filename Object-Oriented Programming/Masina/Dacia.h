#pragma once

#include "MasinaOras.h"

class Dacia : public MasinaOras
{
	int capacitate;
	string culoare;
public:
	Dacia();
	~Dacia();
	string getName();
	void setCapacitate(int capacitateNoua);
	void setCuloare(string culoareNoua);
};