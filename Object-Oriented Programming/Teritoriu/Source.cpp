#include <iostream>
#include "Continent.h"
#include "Munti.h"
#include "Stat.h"

using namespace std;

int main() {
	Continent europa("Europa");
	Stat elvetia("Elvetia", 1000);
	Stat italia("Italia", 6000);
	Stat romania("Romania", 3000);
	Munti alpi("Alpi", 400, 3000);
	elvetia.Add(alpi);
	Munti carpati("Carpati", 200, 2500);
	romania.Add(carpati);
	europa.Add(elvetia);
	europa.Add(italia);
	europa.Add(romania);
	Teritoriu* teritoriu = &europa;
	teritoriu->Afiseaza();
	cout << "Suprafata Europei: " << teritoriu->CalculeazaSuprafata() << " km2" << endl;
	cout << "Statele din Europa cu peste 30% relief montan:" << endl;
	europa.AfiseazaStateCuMunti();
}