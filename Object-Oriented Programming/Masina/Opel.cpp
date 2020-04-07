#include "Opel.h"

Opel::Opel()
{
}

Opel::~Opel()
{
}

string Opel::getName() {
	return "Opel";
}

void Opel::setCapacitate(int capacitateNoua)
{
	capacitate = capacitateNoua;
}

void Opel::setCuloare(string culoareNoua)
{
	culoare = culoareNoua;
}

void Opel::setAnFabricatie(int anFabricatieNou)
{
	anFabricatie = anFabricatieNou;
}

int Opel::getCapacitate()
{
	return capacitate;
}

string Opel::getCuloare()
{
	return culoare;
}

int Opel::getAnFabricatie()
{
	return anFabricatie;
}