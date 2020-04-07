#pragma once

#include <string>

using namespace std;

class Figura
{
protected:
	string culoare;
public:
	Figura();
	virtual ~Figura();
	string GetCuloare();
	virtual string GetName() = 0;
};