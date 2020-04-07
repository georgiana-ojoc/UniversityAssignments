#include "Anunt.h"
#include <iostream>

using namespace std;

Anunt::Anunt(string mesajNou)
{
	mesaj = mesajNou;
}

Anunt::~Anunt()
{
}

void Anunt::Display()
{
	cout << "ANUNT: [" << mesaj << "]\n";
	for (auto iterator : *this) {
		iterator->Display();
	}
}

vector<Afisaj*>::iterator Anunt::begin()
{
	return children.begin();
}

vector<Afisaj*>::iterator Anunt::end()
{
	return children.end();
}