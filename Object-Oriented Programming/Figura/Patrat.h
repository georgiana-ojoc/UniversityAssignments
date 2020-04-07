#pragma once

#include "Dreptunghi.h"
#include <string>

using namespace std;

class Patrat : public Dreptunghi
{
public:
	Patrat(string culoareNoua);
	~Patrat();
	string GetNume();
};