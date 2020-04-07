#include "Header.h"

Propozitie::Propozitie(string str) {
	Propozitie::Set(str);
}

void Propozitie::Set(string str) {
	prop = str;
}

int Propozitie::operator[] (string str) {
	int nrCuvinte = 0;
	int nrCaract = 0;
	int nrVocale = 0;
	int nrNumere = 0;
	for (int i = 0; i < prop.size(); i++) {
		if (prop[i] == ' ') {
			nrCuvinte++;
			if (i + 1 == prop.find_first_of("0123456789", i + 1))
				nrNumere++;
		}
		if (i != prop.find_first_of(" ,.", i))
			nrCaract++;
		if (i == prop.find_first_of("aeiouAEIOU", i))
			nrVocale++;
	}
	nrCuvinte++;
	if (str == "count")
		return nrCuvinte;
	if (str == "total_chars")
		return nrCaract;
	if (str == "vowels")
		return nrVocale;
	if (str == "numbers")
		return nrNumere;
}

string Propozitie::operator[] (int val) {
	vector<string> cuv;
	int poz = 0;
	for (int i = 0; i < prop.size(); i++) {
		if (prop[i] == ' ' || prop[i] == ',') {
			cuv.push_back(prop.substr(poz, i - poz));
			poz = i + 1;
		}
	}
	cuv.push_back(prop.substr(poz, prop.size() - poz));
	if (val >= 0)
		return cuv[val];
	if (val < 0)
		return cuv[cuv.size() + val];
}

int Propozitie::operator() (int val) {
	int count = 0;
	vector<string> cuv;
	int poz = 0;
	for (int i = 0; i < prop.size(); i++) {
		if (prop[i] == ' ' || prop[i] == ',')
		{
			cuv.push_back(prop.substr(poz, i - poz));
			poz = i + 1;
		}
	}
	cuv.push_back(prop.substr(poz, prop.size() - poz));
	for (int i = 0; i < cuv.size(); i++) {
		if (cuv[i].size() == val)
			count++;
	}
	return count;
}