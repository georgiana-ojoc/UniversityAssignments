#pragma once

#include "Figura.h"
#include <string>

using namespace std;

class Dreptunghi : virtual public Figura
{
public:
	Dreptunghi();
	Dreptunghi(string culoareNoua);
	~Dreptunghi();
	string GetName();
};