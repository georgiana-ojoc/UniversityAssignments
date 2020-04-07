#include "Header.h"

using namespace std;

int main() {
	// Propozitie p;
	Propozitie p1("Azi am cumparat morcovi de 10 ron");
	cout << "Cuvinte in propozitie: " << p1["count"] << endl;
	cout << "Numarul total de caractere in propozitie: " << p1["total_chars"] << endl;
	cout << "Numarul total de vocale in propozitie: " << p1["vowels"] << endl;
	cout << "Numarul total de numere in propozitie: " << p1["numbers"] << endl;
	cout << "Primul cuvant: " << p1[0] << endl;
	cout << "Ultimul cuvant: " << p1[-1] << endl;
	cout << "Penultimul cuvant: " << p1[-2] << endl;
	p1.Set("Am cumparat 10 morcovi, 12 castraveti si 14 rosii.");
	cout << "Numarul total de numere in propozitie: " << p1["numbers"] << endl;
	cout << "Numarul total de caractere in propozitie: " << p1["total_chars"] << endl;
	cout << "Al patrulea cuvant: " << p1[3] << endl;
	cout << "Numarul de cuvinte formate doar din doua caractere: " << p1(2) << endl;
	return 0;
}