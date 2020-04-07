#include <iostream>

using namespace std;

#define ABC class
#define AAA public
#define INIT(value) value(__COUNTER__)
#define CREATE(value) \
	void Set_##value(int number) \
	{\
		value = number; \
	} \
	int Get_##value() \
	{ \
		return value; \
	}
#define AFISEAZA(value); cout << value << ' ';
#define CAT_TIMP(conditie) while (conditie) {
#define X_DIN_T t.Get_x()
#define Y_DIN_T	t.Get_y()
#define	ESTE_DIFERIT_DE !=
#define DACA(conditie, yes, no); conditie ? yes : no;
#define MAI_MARE_DECAT >
#define MINUS -
#define SFARSIT_CAT_TIMP }

ABC Test
{
	int x, y, z;
AAA:
	Test() : INIT(x), INIT(y), INIT(z) {}
	CREATE(x);
	CREATE(y);
	CREATE(z);
};

int main()
{
	Test t;
	t.Set_x(18);
	t.Set_y(24);
	AFISEAZA(t.Get_x() + t.Get_y());
	CAT_TIMP(X_DIN_T ESTE_DIFERIT_DE Y_DIN_T)
		DACA(X_DIN_T MAI_MARE_DECAT Y_DIN_T, t.Set_x(X_DIN_T MINUS Y_DIN_T), t.Set_y(Y_DIN_T MINUS X_DIN_T));
	SFARSIT_CAT_TIMP
	AFISEAZA(X_DIN_T);
	return 0;
}