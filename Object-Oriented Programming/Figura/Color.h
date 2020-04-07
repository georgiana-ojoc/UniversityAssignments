#pragma once

#include <string>

using namespace std;

class Color
{
	string culoare;
public:
	Color(string culoareNoua);
	~Color();
	bool HasColor();
	string GetColor();
};