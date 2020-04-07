#pragma once

#include "Afisaj.h"
#include <string>

using namespace std;

class Anunt : public Afisaj
{
	string mesaj;
public:
	Anunt(string mesajNou);
	~Anunt();
	void Display();
	vector<Afisaj*>::iterator begin();
	vector<Afisaj*>::iterator end();
};