#pragma once

#include "SUV.h"

class RangeRover : public SUV
{
	int consum;
public:
	RangeRover();
	~RangeRover();
	string getName();
	void setConsum(int consumNou);
};