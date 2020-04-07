#pragma once

#include <string>

using namespace std;

class Item
{
	string name;
public:
	Item();
	virtual ~Item();
	void SetName(string nameNew);
	string GetName();
	virtual string GetInfo() = 0;
};