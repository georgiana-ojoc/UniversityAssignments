#include "Patrat.h"

Patrat::Patrat(string culoareNoua)
{
	culoare = culoareNoua;
}

Patrat::~Patrat()
{
}

string Patrat::GetNume()
{
	return "Patrat";
}