#include "Food.h"

Food::Food()
{
}

Food::~Food()
{
}

void Food::SetQuantity(float quantityNew)
{
	quantity = quantityNew;
}

string Food::GetInfo()
{
	return to_string(quantity) + " kg";
}