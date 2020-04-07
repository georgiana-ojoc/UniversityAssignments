#include <iostream>
#include "Multime.h"

using namespace std;

Multime::Multime() {
	Count = 0;
}

Multime::Multime(int * Lista, int Size) {
	int Index;
	for (Index = 0; Index < Size && Index < 100; Index++) {
		Numere[Index] = Lista[Index];
	}
	Count = Index;
}

int Multime::GetSize() {
	return Count;
}

int Multime::Max() {
	if (Count == 0) {
		return 0;
	}
	int Max = Numere[0];
	for (int Index = 1; Index < Count; Index++) {
		if (Numere[Index] > Max) {
			Max = Numere[Index];
		}
	}
	return Max;
}

int Multime::Min() {
	if (Count == 0) {
		return 0;
	}
	int Min = Numere[0];
	for (int Index = 1; Index < Count; Index++) {
		if (Numere[Index] < Min) {
			Min = Numere[Index];
		}
	}
	return Min;
}

int ComparareCrescatoare(int First, int Second) {
	if (First > Second) {
		return 1;
	}
	else {
		if (First < Second) {
			return -1;
		}
	}
	return 0;
}

int ComparareDescrescatoare(int First, int Second) {
	if (First < Second) {
		return 1;
	}
	else {
		if (First > Second) {
			return -1;
		}
	}
	return 0;
}

void Multime::Sort(int(*compare)(int, int)) {
	for (int Index1 = 0; Index1 < Count - 1; Index1++) {
		for (int Index2 = Index1 + 1; Index2 < Count; Index2++) {
			if (compare(Numere[Index1], Numere[Index2]) > 0) {
				Numere[Index1] += Numere[Index2];
				Numere[Index2] = Numere[Index1] - Numere[Index2];
				Numere[Index1] -= Numere[Index2];
			}
		}
	}
}

int Multime::operator [] (int Index) {
	return Numere[Index];
}

Multime::operator double() {
	int Sum = 0;
	for (int Index = 0; Index < Count; Index++) {
		Sum += Numere[Index];
	}
	return (double)Sum;
}

Multime operator & (Multime & First, Multime & Second) {
	Multime Reunion;
	for (int Index = 0; Index < First.Count; Index++) {
		Reunion.Numere[Index] = First[Index];
	}
	Reunion.Count = First.Count;
	if (Reunion.Count == 100) {
		return Reunion;
	}
	bool exists;
	for (int Index1 = Reunion.Count; Index1 < First.Count + Second.Count && Index1 < 100; Index1++) {
		exists = false;
		for (int Index2 = 0; Index2 < Reunion.Count && !exists; Index2++) {
			if (Reunion.Numere[Index2] == Second.Numere[Index1 - Reunion.Count]) {
				exists = true;
			}
		}
		if (!exists) {
			Reunion.Numere[Index1] = Second.Numere[Index1 - Reunion.Count];
		}
	}
	return Reunion;
}

Multime operator | (Multime & First, Multime & Second) {
	Multime Intersection;
	Intersection.Count = 0;
	for (int Index1 = 0; Index1 < First.Count; Index1++) {
		for (int Index2 = 0; Index2 < Second.Count; Index2++) {
			if (First.Numere[Index1] == Second.Numere[Index2]) {
				Intersection.Numere[Intersection.Count] = First.Numere[Index1];
				Intersection.Count++;
				break;
			}
		}
	}
	return Intersection;
}

void Multime::Print() {
	for (int Index = 0; Index < Count; Index++) {
		cout << Numere[Index] << ' ';
	}
	cout << endl;
}