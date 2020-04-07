#include <iostream>
#include "Opel.h"

using namespace std;

int main()
{
	Opel opel;
	opel.setCapacitate(100);
	opel.setCuloare("rosu");
	opel.setAnFabricatie(2000);
	MasinaOras *m = &opel;
	cout << m->getName() << ", " << m->getCuloare() << ", " << m->getCapacitate() << ", " << opel.getAnFabricatie() << endl;
	return 1;
}