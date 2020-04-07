#include "Afisaj.h"

Afisaj::Afisaj()
{
}

Afisaj::~Afisaj()
{
}

void Afisaj::Add(Afisaj *child)
{
	children.push_back(child);
}