#pragma once

#include <vector>

using namespace std;

class Afisaj
{
protected:
	vector<Afisaj*> children;
public:
	Afisaj();
	~Afisaj();
	virtual void Display() = 0;
	void Add(Afisaj*);
};