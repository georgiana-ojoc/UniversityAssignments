#include "MyString.h"

using namespace std;

int main() {
	MyString string;
	string.Set("Acesta este un test.");
	cout << "Set: "<< string.GetText() << endl;

	string.Add(" Acesta este un alt test.");
	cout << "Add: " << string.GetText() << endl;

	MyString * substring = string.SubString(8, 4);
	cout << "Substring: "<< substring->GetText() << endl;

	if (string.Delete(7, 5)) {
		cout << "Delete: "<< string.GetText() << endl;
	}
	else {
		cout << "Stergere incorecta." << endl;
	}

	cout << "Compare: " << string.Compare("Acesta este un test.") << endl;

	cout << "GetChar: "<< string.GetChar(0) << endl;

	if (string.Insert(7, " este")) {
		cout << "Insert: "<< string.GetText() << endl;
	}
	else {
		cout << "Inserare incorecta." << endl;
	}

	cout << "Find: "<< string.Find("este") << endl;

	cout << "FindLast: "<< string.FindLast("este") << endl;
	return 0;
}