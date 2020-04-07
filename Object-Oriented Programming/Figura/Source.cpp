#include "Cerc.h"
#include "Patrat.h"
#include "FiguriContainer.h"

int main()
{
	FiguriContainer figuri;
	figuri.Add(new Cerc("rosu"));
	figuri.Add(new Patrat("verde"));
	figuri.Add(new Dreptunghi("rosu"));
	figuri.ShowAll();
	figuri.ShowByColor(new Color("rosu"));
	return 0;
}