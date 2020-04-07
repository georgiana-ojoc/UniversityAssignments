#include "Propozitie.h"

Propozitie::Propozitie(string propozitie)
{
	int start = 0;
	int position = propozitie.find_first_of(' ', start);
	while (position != propozitie.npos)
	{
		cuvinte.push_back(propozitie.substr(start, position - start));
		start = position + 1;
		position = propozitie.find_first_of(' ', start);
	}
}

Propozitie::~Propozitie()
{
}

void Propozitie::Afiseaza()
{
	for (auto iterator : cuvinte)
	{
		iterator.Afiseaza();
		cout << ' ';
	}
	cout << '.';
}

void Propozitie::AddCuvant(Cuvant cuvantNou)
{
	cuvinte.push_back(cuvantNou);
}