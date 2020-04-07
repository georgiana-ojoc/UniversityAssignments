#pragma once

#include "Afisaj.h"
#include <string>

using namespace std;

class PanouPublicitarStradal : public Afisaj
{
	string adresa;
	int lungime;
	int inaltime;
public:
	PanouPublicitarStradal(string adresaNoua, int lungimeNoua, int inaltimeNoua);
	~PanouPublicitarStradal();
	void Display();
	vector<Afisaj*>::iterator begin();
	vector<Afisaj*>::iterator end();
};