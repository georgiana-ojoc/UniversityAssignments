#include <functional>
#include <iostream>
#include <string>
#include <vector>

using namespace std;

template<class T>
int partition(vector<T> &list, int first, int last, int(*Compare)(T&, T&)) {
	T pivot = list[first];
	while (first < last) {
		if (Compare(list[first], list[last]) == 1) {
			swap(list[first], list[last]);
		}
		if (pivot == list[first]) {
			last--;
		}
		else {
			first++;
		}
	}
	return first;
}

template<class T>
int partition(vector<T> &list, int first, int last, function<int(T&, T&)> Compare) {
	T pivot = list[first];
	while (first < last) {
		if (Compare(list[first], list[last]) == 1) {
			swap(list[first], list[last]);
		}
		if (pivot == list[first]) {
			last--;
		}
		else {
			first++;
		}
	}
	return first;
}

template<class T>
void QuickSort(vector<T> &list, int first, int last, int(*Compare)(T&, T&)) {
	int index;
	if (first < last) {
		index = partition(list, first, last, Compare);
		QuickSort(list, first, index - 1, Compare);
		QuickSort(list, index + 1, last, Compare);
	}
}

template<class T>
void QuickSort(vector<T> &list, int first, int last, function<int(T&, T&)> Compare) {
	int index;
	if (first < last) {
		index = partition(list, first, last, Compare);
		QuickSort(list, first, index - 1, Compare);
		QuickSort(list, index + 1, last, Compare);
	}
}

template<class T>
void Sort(vector<T> &list, int(*Compare)(T&, T&)) {
	QuickSort(list, 0, list.size() - 1, Compare);
}

template<class T>
void Sort(vector<T> &list, function<int(T&, T&)> Compare) {
	QuickSort(list, 0, list.size() - 1, Compare);
}

int main() {
	vector<int> listInt;
	listInt.reserve(5);
	for (int index = 5; index > 0; index--) {
		listInt.push_back(index);
	}
	function<int(int&, int&)> CompareInt = [](int &first, int &second)->int { return first > second ? 1 : (first < second ? -1 : 0); };
	Sort(listInt, CompareInt);
	for (auto index : listInt) {
		cout << index << ' ';
	}
	cout << endl;
	vector<string> listString;
	listString.reserve(5);
	listString.push_back("verde");
	listString.push_back("galben");
	listString.push_back("albastru");
	listString.push_back("rosu");
	listString.push_back("maro");
	// auto CompareString = [](string &first, string &second)->int { return first > second ? 1 : (first < second ? -1 : 0); };
	function<int(string&, string&)> CompareString = [](string &first, string &second)->int { return first > second ? 1 : (first < second ? -1 : 0); };
	Sort(listString, CompareString);
	for (auto index : listString) {
		cout << index << ' ';
	}
	return 0;
}