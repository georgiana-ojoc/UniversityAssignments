#include "Stat.h"

Stat::Stat(string numeNou, int suprafataNoua)
{
	nume = numeNou;
	suprafata = suprafataNoua;
}

Stat::~Stat()
{
}

void Stat::Add(Munti munte)
{
	munti.push_back(munte);
}

void Stat::Afiseaza()
{
	cout << "\tStat: " << nume << " (" << suprafata << " km2)\n";
	for (auto iterator : munti)
	{
		iterator.Afiseaza();
	}
}

int Stat::CalculeazaSuprafata()
{
	return suprafata;
}

vector<Munti>::iterator Stat::begin()
{
	return munti.begin();
}

vector<Munti>::iterator Stat::end()
{
	return munti.end();
}