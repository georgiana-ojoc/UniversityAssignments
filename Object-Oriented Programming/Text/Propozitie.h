#pragma once

#include "Cuvant.h"
#include "Text.h"

class Propozitie : public Text
{
	vector<Cuvant> cuvinte;
public:
	Propozitie(string propozitie);
	~Propozitie();
	void Afiseaza();
	void AddCuvant(Cuvant cuvantNou);
};