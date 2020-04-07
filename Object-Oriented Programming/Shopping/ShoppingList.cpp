#include "ShoppingList.h"

ShoppingList::ShoppingList()
{
}

ShoppingList::~ShoppingList()
{
}

void ShoppingList::AddItem(Item *itemNew)
{
	items.push_back(itemNew);
}

void ShoppingList::PrintList()
{
	for (auto iterator : items)
	{
		cout << iterator->GetName() << ": " << iterator->GetInfo() << endl;
	}
}