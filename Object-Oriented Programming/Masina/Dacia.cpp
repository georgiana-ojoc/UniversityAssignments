#include "Dacia.h"

Dacia::Dacia()
{

}

Dacia::~Dacia()
{
}

string Dacia::getName()
{
	return "Dacia";
}

void Dacia::setCapacitate(int capacitateNoua)
{
	capacitate = capacitateNoua;
}

void Dacia::setCuloare(string culoareNoua)
{
	culoare = culoareNoua;
}