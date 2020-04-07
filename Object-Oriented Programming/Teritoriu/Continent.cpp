#include "Continent.h"

Continent::Continent(string numeNou)
{
	nume = numeNou;
}

Continent::~Continent()
{
}

void Continent::Add(Stat stat)
{
	state.push_back(stat);
}

void Continent::Afiseaza()
{
	cout << "Continent: " << nume << endl;
	for (auto iterator : state)
	{
		iterator.Afiseaza();
	}
}

int Continent::CalculeazaSuprafata()
{
	int suprafataState = 0;
	for (auto iterator : state)
	{
		suprafataState += iterator.CalculeazaSuprafata();
	}
	return suprafataState;
}

void Continent::AfiseazaStateCuMunti()
{
	int suprafataMunti;
	for (auto iteratorState : state)
	{
		suprafataMunti = 0;
		for (auto iteratorMunti : iteratorState)
		{
			suprafataMunti += iteratorMunti.CalculeazaSuprafata();
		}
		if (suprafataMunti >= 0.3 * iteratorState.CalculeazaSuprafata())
		{
			iteratorState.Afiseaza();
		}
	}
}