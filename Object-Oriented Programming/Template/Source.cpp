#include <iostream>
#include <cstring>

using namespace std;

template <class T>
class List {
	T * Elemente;
	int Count, Allocated;
public:
	List<T>();
	~List<T>();
	bool Add(const T & object);
	T & operator[] (int index);
	void Sort();
	void Print();
};

template <class T>
List<T>::List() {
	Count = 0;
	Allocated = 10;
	Elemente = new T[sizeof(T) * Allocated];
}

template <class T>
List<T>::~List() {
	Count = 0;
	Allocated = 0;
	delete[] Elemente;
}

template <class T>
bool List<T>::Add(const T &object) {
	if (Count > Allocated) {
		T* Copie = new T[sizeof(T) * Count];
		for (int i = 0; i < Count; i++) {
			Copie[i] = Elemente[i];
		}
		delete[] Elemente;
		Elemente = new T[sizeof(T) * Count];
		for (int i = 0; i < Count; i++) {
			Elemente[i] = Copie[i];
		}
		Elemente[Count++] = object;
		Allocated++;
	}
	else {
		Elemente[Count++] = object;
	}
	return true;
}

template <class T>
T& List<T>::operator[] (int index) {
	return Elemente[index];
}

template <class T>
void List<T>::Sort() {
	for (int i = 0; i < Count - 1; i++) {
		for (int j = i + 1; j < Count; j++) {
			if (Elemente[i] > Elemente[j]) {
				T aux = Elemente[i];
				Elemente[i] = Elemente[j];
				Elemente[j] = aux;
			}
		}
	}
}

template <class T>
void List<T>::Print() {
	for (int i = 0; i < Count; i++) {
		cout << Elemente[i] << ' ';
	}
	cout << endl;
}

class Names {
	char * firstName;
	char * lastName;
public:
	Names() : firstName(NULL), lastName(NULL) {}
	Names(const char * newFirstName, const char * newLastName) {
		firstName = new char[strlen(newFirstName)+1];
		strcpy(firstName, newFirstName);
		lastName = new char[strlen(newLastName)+1];
		strcpy(lastName, newLastName);
	}
	Names(const Names &object) {
		firstName = new char[strlen(object.firstName)+1];
		strcpy(firstName, object.firstName);
		lastName = new char[strlen(object.lastName)+1];
		strcpy(lastName, object.lastName);
	}
	friend bool operator > (const Names &firstObject, const Names &secondObject) {
		return strcmp(firstObject.lastName, secondObject.lastName) > 0 ? true : false;
	}
	char * GetName() {
		char * name = new char[strlen(firstName)+strlen(lastName)+2];
		strcpy(name, firstName);
		strcat(name, " ");
		strcat(name, lastName);
		return name;
	}
	friend ostream & operator << (ostream & out, const Names& name);
};

ostream & operator << (ostream & out, Names & name) {
	out << name.GetName() << endl << '\b';
	return out;
}

int main() {
	cout << "Vector de numere intregi:" << endl;
	List<int> integers;
	for (int i = 29; i > 0; i -= 2) {
		integers.Add(i);
	}
	integers.Print();
	cout << "Sortare crescatoare:" << endl;
	integers.Sort();
	integers.Print();
	cout << endl << "Vector de nume:" << endl;
	List<Names> names;
	Names name1("Georgiana", "Ojoc");
	names.Add(name1);
	Names name2("Teodora", "Balan");
	names.Add(name2);
	Names name3("Vlad", "Corjuc");
	names.Add(name3);
	names.Print();
	cout << "Sortare dupa numele de familie:" << endl;
	names.Sort();
	names.Print();
}