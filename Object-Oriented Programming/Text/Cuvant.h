#pragma once

#include "Text.h"

class Cuvant : public Text
{
	string cuvant;
public:
	Cuvant(string cuvantNou);
	~Cuvant();
	void Afiseaza();
	void SetCuvant(string cuvantNou);
};