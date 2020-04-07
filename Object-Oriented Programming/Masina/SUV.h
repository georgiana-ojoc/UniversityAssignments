#pragma once

#include "Masina.h"

class SUV
{
public:
	SUV();
	virtual ~SUV();
	virtual int getConsum() = 0;
};