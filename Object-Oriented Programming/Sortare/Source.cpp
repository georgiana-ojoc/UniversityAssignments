#include <iostream>
#include <vector>

using namespace std;

template<typename T>
int partition(vector<T>& list, int left, int right) {
	T pivot = list[(left + right) / 2];
	while (left <= right) {
		while (left < right && pivot > list[left]) left++;
		while (right > left && pivot < list[right]) right--;
		if (left <= right) {
			T aux = list[left];
			list[left] = list[right];
			list[right] = aux;
			left++;
			right--;
		}
	}
	return left;
}

template<typename T>
void quick(vector<T> & list, int left, int right) {
	int index = partition(list, left, right);
	if (index - 1 > left)
		quick(list, left, index - 1);
	if (index < right)
		quick(list, index, right);
}

template<typename T>
void sort(vector<T> & list) {
	quick(list, 0, list.size() - 1);
}

void print(vector<int> lista) {
	for (auto i : lista)
		cout << i << " ";
	cout << endl;
}

bool esteOrdonat(vector<int> lista) {
	for (int i = 0; i < lista.size() - 1; i++)
		if (lista[i] > lista[i + 1])
			return false;
	return true;
}

void verificare(vector<int> lista) {
	sort(lista);
	print(lista);
	cout << "ordonat = " << boolalpha << esteOrdonat(lista) << endl;
}

int main()
{
	vector<int> lista1 = { 1, 2, 3, 4, 5 };
	verificare(lista1);
	vector<int> lista2 = { 5, 4, 3, 2, 1 };
	verificare(lista2);
	vector<int> lista3 = { -20000, -30000, -40000 };
	verificare(lista3);
	vector<int> lista4 = { 28, 22, 28, 26, 24 };
	verificare(lista4);
}