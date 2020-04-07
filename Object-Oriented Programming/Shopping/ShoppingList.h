#pragma once

#include <iostream>
#include "Item.h"
#include <vector>

using namespace std;

class ShoppingList
{
	vector<Item*> items;
public:
	ShoppingList();
	~ShoppingList();
	void AddItem(Item *itemNew);
	void PrintList();
};