#include <iostream>
#include <exception>

using namespace std;

class ExceptionRange : public exception {
	virtual const char* what() const throw() {
		return "Index out of range.";
	}
};

class ExceptionPosition : public exception {
	virtual const char* what() const throw() {
		return "Invalid position.";
	}
};

class Compare {
public:
	virtual int CompareElements(void*, void*) = 0;
};

class CompareInt : public Compare {
	int CompareElements(void* First, void* Second) {
		int* _First = (int*)First;
		int* _Second = (int*)Second;
		return *_First > *_Second ? 1 : (*_First < *_Second ? -1 : 0);
	}
};

class CompareString : public Compare {
	int CompareElements(void* First, void* Second) {
		string* _First = (string*)First;
		string* _Second = (string*)Second;
		return *_First > * _Second ? 1 : (*_First < *_Second ? -1 : 0);
	}
};

int _CompareInt(const int& First, const int& Second) {
	return First > Second ? 1 : (First < Second ? -1 : 0);
}

int _CompareString(const string& First, const string& Second) {
	return First > Second ? 1 : (First < Second ? -1 : 0);
}

template<class T>
class ArrayIterator {
public:
	T** Current;
	ArrayIterator() {
		Current = 0;
	}
	~ArrayIterator() {
		Current = 0;
	}
	ArrayIterator& operator ++ () {
		++Current;
		return *this;
	}
	ArrayIterator& operator -- () {
		--Current;
		return *this;
	}
	bool operator = (ArrayIterator<T>& Iterator) {
		return Current == Iterator.Current;
	}
	bool operator != (ArrayIterator<T>& Iterator) {
		return Current != Iterator.Current + 1;
	}
	T operator * () {
		return **Current;
	}
	T* GetElement() {
		return Current;
	}
};

template<class T>
class Array {
private:
	T** List;
	int Capacity;
	int Size;
public:
	Array() {
		List = nullptr;
		Capacity = 0;
		Size = 0;
	}

	~Array() {
		for (int Index = 0; Index < Size; ++Index) {
			delete List[Index];
		}
		delete[] List;
	}

	Array(int OtherCapacity) {
		Capacity = OtherCapacity;
		Size = 0;
		List = new T *[Capacity];
	}

	Array(const Array<T>& OtherArray) {
		Capacity = OtherArray.Capacity;
		Size = OtherArray.Size;
		List = new T *[Capacity];
		for (int Index = 0; Index < Size; ++Index) {
			List[Index] = new T;
			*List[Index] = *OtherArray.List[Index];
		}
	}

	T& operator[] (int Index) {
		ExceptionRange IndexOutOfRange;
		try {
			if (Index < 0 || Index >= Size) {
				throw IndexOutOfRange;
			}
			return *List[Index];
		}
		catch (exception& Exception) {
			cout << "Exception: " << Exception.what() << endl;
		}
	}

	const Array<T>& operator += (const T& NewElement) {
		if (Size == Capacity) {
			++Capacity;
			T** NewList = new T *[Capacity];
			for (int Index = 0; Index < Size; ++Index) {
				NewList[Index] = List[Index];
			}
			List = NewList;
		}
		List[Size] = new T;
		*List[Size++] = NewElement;
		return *this;
	}

	const Array<T>& Insert(int NewIndex, const T& NewElement) {
		ExceptionPosition InvalidPosition;
		try {
			if (NewIndex < 0 || NewIndex >= Size + 1) {
				throw InvalidPosition;
			}
			if (Size == Capacity) {
				++Capacity;
				T** NewList = new T *[Capacity];
				for (int Index = 0; Index < Size; ++Index) {
					NewList[Index] = List[Index];
				}
				List = NewList;
			}
			for (int Index = Size - 1; Index >= NewIndex; --Index) {
				List[Index + 1] = List[Index];
			}
			List[NewIndex] = new T;
			*List[NewIndex] = NewElement;
			++Size;
			return *this;
		}
		catch (exception& Exception) {
			cout << "Exception: " << Exception.what() << endl;
		}
	}

	const Array<T>& Insert(int OtherIndex, const Array<T> OtherArray) {
		ExceptionPosition InvalidPosition;
		try {
			if (OtherIndex < 0 || OtherIndex >= Size + 1) {
				throw InvalidPosition;
			}
			if (Size + OtherArray.Size > Capacity) {
				Capacity += Size + OtherArray.Size;
				T** NewList = new T *[Capacity];
				for (int Index = 0; Index < Size; ++Index) {
					NewList[Index] = List[Index];
				}
				List = NewList;
			}
			for (int Index = Size - 1; Index >= OtherIndex; --Index) {
				List[Index + OtherArray.Size] = List[Index];
			}
			for (int Index = OtherIndex; Index < OtherIndex + OtherArray.Size; ++Index) {
				List[Index] = new T;
				*List[Index] = *OtherArray.List[Index - OtherIndex];
			}
			Size += OtherArray.Size;
			return *this;
		}
		catch (exception & Exception) {
			cout << "Exception: " << Exception.what() << endl;
		}
	}

	const Array<T>& Delete(int NewIndex) {
		ExceptionPosition InvalidPosition;
		try {
			if (NewIndex < 0 || NewIndex >= Size) {
				throw InvalidPosition;
			}
			for (int Index = NewIndex; Index < Size - 1; ++Index) {
				List[Index] = List[Index + 1];
			}
			delete List[--Size];
			--Capacity;
			return *this;
		}
		catch (exception & Exception) {
			cout << "Exception: " << Exception.what() << endl;
		}
	}

	bool operator = (const Array<T>& OtherArray) {
		if (Size != OtherArray.Size) {
			return false;
		}
		for (int Index = 0; Index < Size; ++Index) {
			if (List[Index] != OtherArray.List[Index]) {
				return false;
			}
		}
		return true;
	}

	void Sort() {
		T* Aux;
		for (int Index1 = 0; Index1 < Size - 1; ++Index1) {
			for (int Index2 = Index1 + 1; Index2 < Size; ++Index2) {
				if (List[Index1] > List[Index2]) {
					Aux = List[Index1];
					List[Index1] = List[Index2];
					List[Index2] = Aux;
				}
			}
		}
	}

	void Sort(int(*Compare)(const T&, const T&)) {
		T* Aux;
		for (int Index1 = 0; Index1 < Size - 1; ++Index1) {
			for (int Index2 = Index1 + 1; Index2 < Size; ++Index2) {
				if (Compare(*List[Index1], *List[Index2]) == 1) {
					Aux = List[Index1];
					List[Index1] = List[Index2];
					List[Index2] = Aux;
				}
			}
		}
	}

	void Sort(Compare* Comparator) {
		T* Aux;
		for (int Index1 = 0; Index1 < Size - 1; ++Index1) {
			for (int Index2 = Index1 + 1; Index2 < Size; ++Index2) {
				if (Comparator->CompareElements(List[Index1], List[Index2]) == 1) {
					Aux = List[Index1];
					List[Index1] = List[Index2];
					List[Index2] = Aux;
				}
			}
		}
	}

	int BinarySearch(const T& Element) {
		int Left = 0;
		int Right = Size - 1;
		int Middle;
		while (Left <= Right) {
			Middle = (Left + Right) / 2;
			if (Element == List[Middle]) {
				return Middle;
			}
			if (Element < List[Middle]) {
				Right = Middle - 1;
			}
			else {
				Left = Middle + 1;
			}
		}
		return -1;
	}

	int BinarySearch(const T& Element, int(*Compare)(const T&, const T&)) {
		int Left = 0;
		int Right = Size - 1;
		int Middle;
		while (Left <= Right) {
			Middle = (Left + Right) / 2;
			if (Compare(Element, *List[Middle]) == 0) {
				return Middle;
			}
			if (Compare(Element, *List[Middle]) == -1) {
				Right = Middle - 1;
			}
			else {
				Left = Middle + 1;
			}
		}
		return -1;
	}

	int BinarySearch(const T& Element, Compare* Comparator) {
		T* _Element = (T*)& Element;
		int Left = 0;
		int Right = Size - 1;
		int Middle;
		while (Left <= Right) {
			Middle = (Left + Right) / 2;
			if (Comparator->CompareElements(_Element, List[Middle]) == 0) {
				return Middle;
			}
			if (Comparator->CompareElements(_Element, List[Middle]) == -1) {
				Right = Middle - 1;
			}
			else {
				Left = Middle + 1;
			}
		}
		return -1;
	}

	int Find(const T& Element) {
		for (int Index = 0; Index < Size; ++Index) {
			if (Element == List[Index]) {
				return Index;
			}
		}
		return -1;
	}

	int Find(const T& Element, int(*Compare)(const T&, const T&)) {
		for (int Index = 0; Index < Size; ++Index) {
			if (Compare(Element, List[Index]) == 0) {
				return Index;
			}
		}
		return -1;
	}

	int Find(const T& Element, Compare* Comparator) {
		for (int Index = 0; Index < Size; ++Index) {
			if (Comparator->CompareElements(Element, List[Index]) == 0) {
				return Index;
			}
		}
		return -1;
	}

	int GetSize() {
		return Size;
	}

	int GetCapacity() {
		return Capacity;
	}

	ArrayIterator<T> begin() {
		ArrayIterator<T> Iterator;
		Iterator.Current = &List[0];
		return Iterator;
	}

	ArrayIterator<T> end() {
		ArrayIterator<T> Iterator;
		Iterator.Current = &List[Size - 1];
		return Iterator;
	}

	void Print() {
		for (int Index = 0; Index < Size; ++Index) {
			cout << *List[Index] << ' ';
		}
		cout << endl;
	}
};

int main() {
	cout << "Array of integers: " << endl;
	Array<int> IntArray(4);
	for (int Position = 0, Value = 4; Value > 0; ++Position, --Value) {
		cout << "Insert value " << Value << " on position " << Position << ": ";
		IntArray.Insert(Position, Value);
		IntArray.Print();
	}
	cout << "Insert value 5 on position 5: ";
	IntArray.Insert(5, 5);
	Array<int> OtherIntArray = IntArray;
	cout << "Insert array ";
	for (auto Iterator : OtherIntArray) {
		cout << Iterator << ' ';
	}
	cout << "on position 2: ";
	IntArray.Insert(2, OtherIntArray);
	IntArray.Print();
	cout << "Insert value 5 on last position: ";
	IntArray += 5;
	IntArray.Print();
	cout << "Delete element from last position: ";
	IntArray.Delete(IntArray.GetSize() - 1);
	IntArray.Print();
	cout << "Get element from position 10: ";
	cout << IntArray[10];
	cout << "\bSort with comparison function: ";
	IntArray.Sort(_CompareInt);
	IntArray.Print();
	CompareInt ComparatorInt;
	cout << "Position of value 2 using binary search with comparison object: " << IntArray.BinarySearch(2, &ComparatorInt);
	return 0;
}