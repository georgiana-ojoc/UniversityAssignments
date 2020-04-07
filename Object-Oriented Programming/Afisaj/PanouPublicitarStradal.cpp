#include <iostream>
#include "PanouPublicitarStradal.h"

using namespace std;

PanouPublicitarStradal::PanouPublicitarStradal(string adresaNoua, int lungimeNoua, int inaltimeNoua)
{
	adresa = adresaNoua;
	lungime = lungimeNoua;
	inaltime = inaltimeNoua;
}

PanouPublicitarStradal::~PanouPublicitarStradal()
{
}

void PanouPublicitarStradal::Display()
{
	cout << "PANOU PUBLICITAR: [" << adresa << "] [lungime:" << lungime << "] [inaltime:" << inaltime << "]\n";
	for (auto iterator : *this) {
		iterator->Display();
	}
}

vector<Afisaj*>::iterator PanouPublicitarStradal::begin()
{
	return children.begin();
}

vector<Afisaj*>::iterator PanouPublicitarStradal::end()
{
	return children.end();
}