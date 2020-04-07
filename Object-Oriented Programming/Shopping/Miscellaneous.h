#pragma once

#include "Item.h"

class Miscellaneous : public Item
{
	int count;
public:
	Miscellaneous();
	~Miscellaneous();
	void SetCount(int countNew);
	string GetInfo();
};