#include "Color.h"

Color::Color(string culoareNoua)
{
	culoare = culoareNoua;
}

Color::~Color()
{
}

bool Color::HasColor()
{
	return culoare != "";
}

string Color::GetColor()
{
	return culoare;
}