#include "Item.h"

Item::Item()
{
}

Item::~Item()
{
}

void Item::SetName(string nameNew)
{
	name = nameNew;
}

string Item::GetName()
{
	return name;
}