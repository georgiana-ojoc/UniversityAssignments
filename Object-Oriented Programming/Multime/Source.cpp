#include "Multime.h"
#include <iostream>

using namespace std;

int main() {
	int Size1 = 5;
	int * Lista1 = new int[Size1];
	Lista1[0] = 3;
	Lista1[1] = 1;
	Lista1[2] = 2;
	Lista1[3] = 4;
	Lista1[4] = 5;
	Multime Multime1(Lista1, Size1);
	cout << "Multime1.GetSize(): " << Multime1.GetSize() << endl;
	cout << "Multime1.Print(): ";
	Multime1.Print();
	int Size2 = 3;
	int * Lista2 = new int[Size2];
	Lista2[0] = 2;
	Lista2[1] = 4;
	Lista2[2] = 1;
	Multime Multime2(Lista2, Size2);
	cout << "Multime2.GetSize(): " << Multime2.GetSize() << endl;
	cout << "Multime2.Print(): ";
	Multime2.Print();
	Multime Reunion;
	Reunion = Multime1 & Multime2;
	Reunion.Sort(ComparareCrescatoare);
	cout << "Reunion.Print(): ";
	Reunion.Print();
	cout << "Reunion.Max(): " << Reunion.Max() << endl;
	Multime Intersection;
	Intersection = Multime1 | Multime2;
	Intersection.Sort(ComparareDescrescatoare);
	cout << "Intersection.Print(): ";
	Intersection.Print();
	cout << "Intersection.Min(): " << Intersection.Min() << endl;
	cout << "(double)Intersection: " << (double)Intersection << endl;
}