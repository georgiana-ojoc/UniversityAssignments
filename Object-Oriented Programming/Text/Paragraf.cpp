#include "Paragraf.h"

Paragraf::Paragraf()
{
}

Paragraf::~Paragraf()
{
}

void Paragraf::Afiseaza()
{
	for (auto iterator : texte)
	{
		iterator->Afiseaza();
	}
}

void Paragraf::Add(Text *text)
{
	texte.push_back(text);
}