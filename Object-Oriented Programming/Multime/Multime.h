#pragma once

class Multime {
	int Numere[100];
	int Count;
public:
	Multime();
	Multime(int * Lista, int Size);
	int GetSize();
	int Max();
	int Min();
	void Sort(int(*compare)(int, int));
	int operator [] (int Index);
	operator double();
	friend Multime operator & (Multime & First, Multime & Second);
	friend Multime operator | (Multime & First, Multime & Second);
	void Print();
};

int ComparareCrescatoare(int First, int Second);

int ComparareDescrescatoare(int First, int Second);