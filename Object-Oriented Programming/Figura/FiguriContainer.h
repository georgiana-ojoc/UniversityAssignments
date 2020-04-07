#pragma once

#include "Color.h"
#include "Figura.h"
#include <vector>

using namespace std;

class FiguriContainer
{
	vector<Figura*> figuri;
public:
	FiguriContainer();
	~FiguriContainer();
	void Add(Figura *figura);
	void ShowAll();
	void ShowByColor(Color* culoare);
};