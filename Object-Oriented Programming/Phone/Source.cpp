#include <iostream>
#include <string>
#include <vector>

using namespace std;

class Contact {
private:
	string Nume;
	int Tip;
protected:
	Contact() {}
public:
	string GetNume() {
		return Nume;
	}
	void SetNume(string NumeNou) {
		Nume = NumeNou;
	}
	int GetTip() {
		return Tip;
	}
	void SetTip(int TipNou) {
		Tip = TipNou;
	}
};

class Cunoscut : public Contact {
private:
	string NumarTelefon;
public:
	Cunoscut(string NumeNou, string NumarTelefonNou) {
		SetNume(NumeNou);
		NumarTelefon = NumarTelefonNou;
		SetTip(1);
	}
	string GetNumarTelefon() {
		return NumarTelefon;
	}
};

class Coleg : public Contact {
private:
	string NumarTelefon, Adresa, Firma;
public:
	Coleg(string NumeNou, string NumarTelefonNou, string AdresaNoua, string FirmaNoua) {
		SetNume(NumeNou);
		NumarTelefon = NumarTelefonNou;
		Adresa = AdresaNoua;
		Firma = FirmaNoua;
		SetTip(2);
	}
	string GetNumarTelefon() {
		return NumarTelefon;
	}
	string GetAdresa() {
		return Adresa;
	}
	string GetFirma() {
		return Firma;
	}
};

class Prieten : public Contact {
private:
	string NumarTelefon, DataNastere, Adresa;
public:
	Prieten(string NumeNou, string NumarTelefonNou, string AdresaNoua, string DataNastereNoua) {
		SetNume(NumeNou);
		NumarTelefon = NumarTelefonNou;
		Adresa = AdresaNoua;
		DataNastere = DataNastereNoua;
		SetTip(3);
	}
	string GetNumarTelefon() {
		return NumarTelefon;
	}
	string GetAdresa() {
		return Adresa;
	}
	string GetDataNastere() {
		return DataNastere;
	}
};

class Agenda {
private:
	vector<Contact*> Contacte;
public:
	bool Find(string Nume) {
		for (auto Iterator : Contacte) {
			if (Iterator->GetNume() == Nume) {
				return true;
			}
		}
		return false;
	}
	vector<Prieten*> GetListaPrieteni() {
		vector<Prieten*> Lista;
		for (auto Iterator : Contacte) {
			if (Iterator->GetTip() == 3) {
				Lista.push_back((Prieten*)Iterator);
			}
		}
		return Lista;
	}
	void DeleteContact(string Nume) {
		int Count = 0;
		for (vector<Contact*>::iterator Iterator = Contacte.begin(); Iterator < Contacte.end(); Iterator++) {
			if ((*Iterator)->GetNume() == Nume) {
				Contacte.erase(Contacte.begin() + Count);
				return;
			}
			Count++;
		}
	}
	void AddContact(Contact* Contact) {
		Contacte.push_back(Contact);
	}
	void Print() {
		for (auto Iterator : Contacte) {
			if (Iterator->GetTip() == 1) {
				Cunoscut* Persoana = (Cunoscut*)Iterator;
				cout << Persoana->GetNume() << ' ' << Persoana->GetNumarTelefon() << endl;
			}
			else if (Iterator->GetTip() == 2) {
				Coleg* Persoana = (Coleg*)Iterator;
				cout << Persoana->GetNume() << ' ' << Persoana->GetNumarTelefon() << ' ' << Persoana->GetAdresa() << ' ' << Persoana->GetFirma() << endl;
			}
			else {
				Prieten* Persoana = (Prieten*)Iterator;
				cout << Persoana->GetNume() << ' ' << Persoana->GetNumarTelefon() << ' ' << Persoana->GetAdresa() << ' ' << Persoana->GetDataNastere() << endl;
			}
		}
	}
};

int main() {
	Agenda Agenda;
	cout << "Add Prieten:" << endl;
	Prieten Teodora("Teodora", "0726825452", "Tecuci", "01.10.1999");
	Agenda.AddContact((Contact*)&Teodora);
	Agenda.Print();
	cout << endl << "Add Prieten:" << endl;
	Prieten Vlad("Vlad", "0753246587", "Ipotesti", "15.06.2000");
	Agenda.AddContact((Contact*)&Vlad);
	Agenda.Print();
	cout << endl << "Add Coleg:" << endl;
	Coleg Robert("Robert", "0765215479", "Vaslui", "Continental");
	Agenda.AddContact((Contact*)&Robert);
	Agenda.Print();
	cout << endl << "Add Cunoscut:" << endl;
	Cunoscut Magda("Magda", "0739624857");
	Agenda.AddContact((Contact*)&Magda);
	Agenda.Print();
	cout << endl << "Delete Magda:" << endl;
	Agenda.DeleteContact(Magda.GetNume());
	Agenda.Print();
	cout << endl << "Get Lista Prieteni:" << endl;
	vector<Prieten*> Lista = Agenda.GetListaPrieteni();
	for (auto Iterator : Lista) {
		cout << Iterator->GetNume() << ' ' << Iterator->GetNumarTelefon() << ' ' << Iterator->GetAdresa() << ' ' << Iterator->GetDataNastere() << endl;
	}
	return 0;
}