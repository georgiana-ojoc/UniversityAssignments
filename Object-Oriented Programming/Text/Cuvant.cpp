#include "Cuvant.h"

Cuvant::Cuvant(string cuvantNou)
{
	cuvant = cuvantNou;
}

Cuvant::~Cuvant()
{
}

void Cuvant::Afiseaza()
{
	cout << cuvant;
}

void Cuvant::SetCuvant(string cuvantNou)
{
	cuvant = cuvantNou;
}