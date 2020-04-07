#include "BigNumber.h"

using namespace std;

int main()
{
	BigNumber a, b, c, d;
    BigNumber sum, diff, mul, div;

    a.Set(567);
	cout << "a = ";
	a.print();
	
	b.Set("123");
	cout << "b = ";
	b.print();

    c.Set(54321);
    cout << "c = ";
    c.print();

	d.Set("12345");
	cout << "d = ";
    d.print();
    
	cout << "a == b ? ";
	if (a == b) {
		cout << "da";
	}
	else {
		cout << "nu";
	}
	cout << endl;

	cout << "c < d  ? ";
	if (c < d) {
		cout << "da";
	}
	else {
		cout << "nu";
	}
	cout << endl;

	cout << "a >= b ? ";
	if (a >= b) {
		cout << "da";
	}
	else {
		cout << "nu";
	}
	cout << endl;

    cout << "a + b = ";
	sum = a + b;
    sum.print();

	cout << "c - d = ";
	diff = c - d;
    diff.print();

	cout << "a * c = ";
    mul = a * c;
    mul.print();

	cout << "c / a = ";
    div = c / a;
    div.print();

	cout << "operator int = ";
	cout << (int)a << endl;
	
	cout << "c[0] = ";
	cout << c[0] << endl;
    
	cout << "d(1, 4) = ";
	d(1, 4).print();
    return 0;
}