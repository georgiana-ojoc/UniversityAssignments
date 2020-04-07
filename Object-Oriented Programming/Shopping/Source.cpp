#include "Food.h"
#include "Miscellaneous.h"
#include "ShoppingList.h"

int main()
{
	Food carne;
	carne.SetName("carne");
	carne.SetQuantity(1.5f);
	Miscellaneous servetele;
	servetele.SetName("servetele");
	servetele.SetCount(3);
	ShoppingList shoppingList;
	shoppingList.AddItem(&carne);
	shoppingList.AddItem(&servetele);
	shoppingList.PrintList();
	return 0;
}