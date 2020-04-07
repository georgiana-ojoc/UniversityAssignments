#include "Miscellaneous.h"

Miscellaneous::Miscellaneous()
{
}

Miscellaneous::~Miscellaneous()
{
}

void Miscellaneous::SetCount(int countNew)
{
	count = countNew;
}

string Miscellaneous::GetInfo()
{
	return to_string(count) + " items";
}