#include <iostream>
#include <queue>

using namespace std;

class Container {

public:

	virtual bool Add(int value) = 0;

	virtual bool Del(int value) = 0;

	virtual int Count(int value) = 0;

	virtual bool Exists(int value) = 0;

	virtual int* GetSortedArray() = 0;

	virtual int GetCount() = 0;
	
	virtual void Print() = 0;

};

class _List : public Container {

private:

	struct List {
		int info;
		List* next = NULL;
	};

	List* first;
	List* last;
	
public:

	_List() {
		first = NULL;
		last = NULL;
	}

	~_List() { 
	
	}

	bool Add(int value) {
		List* node = new List;
		node->info = value;
		if (first == NULL) {
			first = last = node;
			return true;
		}
		else {
			last->next = node;
			last = node;
			return true;
		}
	}

	bool Del(int value) {
		if (!Exists(value)) {
			return false;
		}
		List* old;
		if (first->info == value) {
			old = first;
			first = first->next;
			if (last == old) {
				last = NULL;
			}
			delete old;
			return true;
		}
		else {
			List* copy = first;
			while (copy->next) {
				if (copy->next->info == value) {
					old = copy->next;
					copy->next = copy->next->next;
					delete old;
					return true;
				}
				copy = copy->next;
			}
		}
	}

	int Count(int value) {
		int count = 0;
		List* copy = first;
		while (copy) {
			if (copy->info == value) {
				count++;
			}
			copy = copy->next;
		}
		return count;
	}

	bool Exists(int value) {
		List* copy = first;
		while (copy) {
			if (copy->info == value) {
				return true;
			}
			copy = copy->next;
		}
		return false;
	}

	int* GetSortedArray() {
		if (first == NULL) {
			return NULL;
		}
		int length = GetCount();
		int* array = new int[length];
		int index1 = 0;
		List* copy;
		for (copy = first; copy; copy = copy->next) {
			array[index1] = copy->info;
			index1++;
		}
		int index2;
		for (index1 = 0; index1 < length - 1; index1++) {
			for (index2 = index1 + 1; index2 < length; index2++) {
				if (array[index1] > array[index2]) {
					array[index1] += array[index2];
					array[index2] = array[index1] - array[index2];
					array[index1] -= array[index2];
				}
			}
		}
		return array;
	}

	int GetCount() {
		int count = 0;
		List* copy;
		for (copy = first; copy; copy = copy->next) {
			count++;
		}
		return count;
	}

	void Print() {
		List* copy;
		for (copy = first; copy; copy = copy->next) {
			cout << copy->info << ' ';
		}
		cout << endl;
	}

};

class _Tree : public Container {

private:

	struct Tree {
		int info;
		Tree* left = NULL;
		Tree* right = NULL;
	};
	
	Tree* root;

public:

	_Tree() {
		root = NULL;
	}

	~_Tree() {

	}

	bool Add(int value) {
		Tree* node = new Tree;
		node->info = value;
		if (root == NULL) {
			root = node;
			return true;
		}
		Tree* copy;
		queue<Tree*> queue;
		queue.push(root);
		while (!queue.empty()) {
			copy = queue.front();
			queue.pop();
			if (copy->left == NULL) {
				copy->left = node;
				return true;
			}
			queue.push(copy->left);
			if (copy->right == NULL) {
				copy->right = node;
				return true;
			}
			queue.push(copy->right);
		}
	}

	bool Del(int value) {
		if (!Exists(value)) {
			return false;
		}
		Tree* old = NULL;
		if (root->left == NULL && root->right == NULL) {
			old = root;
			root = NULL;
			delete old;
			return true;
		}
		bool isFound = false;
		Tree* copy = NULL;
		queue<Tree*> queue;
		queue.push(root);
		while (!queue.empty()) {
			copy = queue.front();
			queue.pop();
			if (copy->info == value && !isFound) {
				old = copy;
				isFound = true;
			}
			if (copy->left) {
				queue.push(copy->left);
			}
			if (copy->right) {
				queue.push(copy->right);
			}
		}
		Tree* last = copy;
		old->info = last->info;
		queue.push(root);
		while (!queue.empty()) {
			copy = queue.front();
			queue.pop();
			if (copy->left) {
				if (copy->left == last) {
					copy->left = NULL;
					delete last;
					return true;
				}
				queue.push(copy->left);
			}
			if (copy->right) {
				if (copy->right == last) {
					copy->right = NULL;
					delete last;
					return true;
				}
				queue.push(copy->right);
			}
		}
	}

	int Count(int value) {
		if (root == NULL) {
			return 0;
		}
		Tree* copy;
		int count = 0;
		queue<Tree*> queue;
		queue.push(root);
		while (!queue.empty()) {
			copy = queue.front();
			queue.pop();
			if (copy->info == value) {
				count++;
			}
			if (copy->left) {
				queue.push(copy->left);
			}
			if (copy->right) {
				queue.push(copy->right);
			}
		}
		return count;
	}

	bool Exists(int value) {
		if (root == NULL) {
			return false;
		}
		Tree* copy;
		queue<Tree*> queue;
		queue.push(root);
		while (!queue.empty()) {
			copy = queue.front();
			queue.pop();
			if (copy->info == value) {
				return true;
			}
			if (copy->left) {
				queue.push(copy->left);
			}
			if (copy->right) {
				queue.push(copy->right);
			}
		}
		return false;
	}

	int* GetSortedArray() {
		if (root == NULL) {
			return NULL;
		}
		int length = GetCount();
		int* array = new int[length];
		int index1 = 0;
		Tree* copy;
		queue<Tree*> queue;
		queue.push(root);
		while (!queue.empty()) {
			copy = queue.front();
			queue.pop();
			array[index1] = copy->info;
			index1++;
			if (copy->left) {
				queue.push(copy->left);
			}
			if (copy->right) {
				queue.push(copy->right);
			}
		}
		int index2;
		for (index1 = 0; index1 < length - 1; index1++) {
			for (index2 = index1 + 1; index2 < length; index2++) {
				if (array[index1] > array[index2]) {
					array[index1] += array[index2];
					array[index2] = array[index1] - array[index2];
					array[index1] -= array[index2];
				}
			}
		}
		return array;
	}

	int GetCount() {
		if (root == NULL) {
			return 0;
		}
		int count = 0;
		Tree* copy;
		queue<Tree*> queue;
		queue.push(root);
		while (!queue.empty()) {
			copy = queue.front();
			queue.pop();
			count++;
			if (copy->left) {
				queue.push(copy->left);
			}
			if (copy->right) {
				queue.push(copy->right);
			}
		}
		return count;
	}

	void Print() {
		if (root == NULL) {
			return;
		}
		Tree* copy;
		queue<Tree*> queue;
		queue.push(root);
		while (!queue.empty()) {
			copy = queue.front();
			queue.pop();
			cout << copy->info << ' ';
			if (copy->left) {
				queue.push(copy->left);
			}
			if (copy->right) {
				queue.push(copy->right);
			}
		}
		cout << endl;
	}

};

int main() {

	cout << '\t' << "List:" << endl;

	_List list;

	for (int value = 10; value >= 1; value--) {
		list.Add(value);
	}
	cout << "Add: ";
	list.Print();


	for (int value = 1; value <= 9; value += 2) {
		list.Del(value);
	}
	cout << "Del: ";
	list.Print();

	cout << "Count: " << list.Count(5) << endl;

	int* array1 = list.GetSortedArray();
	int length1 = list.GetCount();
	cout << "GetSortedArray: ";
	for (int index = 0; index < length1; index++) {
		cout << array1[index] << ' ';
	}
	delete[] array1;

	cout << endl << endl;
	
	cout << '\t' << "Tree:" << endl;

	_Tree tree;

	for (int value = 10; value >= 1; value--) {
		tree.Add(value);
	}
	cout << "Add: ";
	tree.Print();
	
	for (int value = 1; value <= 9; value += 2) {
		tree.Del(value);
		cout << "Del(" << value << "): ";
		tree.Print();
	}
	
	cout << "Count: " << tree.Count(5) << endl;
	
	int* array2 = tree.GetSortedArray();
	int length2 = tree.GetCount();
	cout << "GetSortedArray: ";
	for (int index = 0; index < length2; index++) {
		cout << array2[index] << ' ';
	}
	delete[] array2;

	cout << endl;
	
}