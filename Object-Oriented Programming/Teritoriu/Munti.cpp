#include "Munti.h"

Munti::Munti(string numeNou, int suprafataNoua, int altitudineNoua)
{
	nume = numeNou;
	suprafata = suprafataNoua;
	altitudine = altitudineNoua;
}

Munti::~Munti()
{
}

void Munti::Afiseaza()
{
	cout << "\t\tMunte: " << nume << " (" << altitudine << " m)\n";
}

int Munti::CalculeazaSuprafata()
{
	return suprafata;
}