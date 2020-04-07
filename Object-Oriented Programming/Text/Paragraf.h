#pragma once

#include "Text.h"

class Paragraf : public Text
{
	vector<Text*> texte;
public:
	Paragraf();
	~Paragraf();
	void Afiseaza();
	void Add(Text *text);
};