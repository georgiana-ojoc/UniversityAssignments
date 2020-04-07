#pragma once

#include "Figura.h"
#include <string>

using namespace std;

class Cerc : virtual public Figura
{
public:
	Cerc(string culoareNoua);
	~Cerc();
	string GetName();
};