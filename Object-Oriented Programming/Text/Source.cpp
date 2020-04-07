#include "Cuvant.h"
#include "Paragraf.h"
#include "Propozitie.h"

int main()
{
	Propozitie propozitie1("Ana are mere");
	Propozitie propozitie2("Ionel are cirese");
	Cuvant si("si");
	Cuvant mere("mere");
	Cuvant endl1("\n");
	Cuvant tab("\t");
	Propozitie propozitie3("Vara se coc fructele");
	propozitie2.AddCuvant(si);
	propozitie2.AddCuvant(mere);
	Paragraf p;
	p.Add(&propozitie1);
	p.Add(&endl1);
	p.Add(&tab);
	p.Add(&propozitie2);
	p.Add(&endl1);
	p.Add(&propozitie3);
	p.Add(&endl1);
	p.Afiseaza();
	return 0;
}