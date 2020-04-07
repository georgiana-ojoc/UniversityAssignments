#include "FiguriContainer.h"
#include <iostream>

using namespace std;

FiguriContainer::FiguriContainer()
{
}

FiguriContainer::~FiguriContainer()
{
}

void FiguriContainer::Add(Figura *figura)
{
	figuri.push_back(figura);
}

void FiguriContainer::ShowAll()
{
	for (auto iterator : figuri)
	{
		cout << iterator->GetName() << ' ' << iterator->GetCuloare() << endl;
	}
}

void FiguriContainer::ShowByColor(Color* culoare)
{
	if (culoare->HasColor()) {
		for (auto iterator : figuri)
		{
			if (iterator->GetCuloare() == culoare->GetColor())
			{
				cout << iterator->GetName() << endl;
			}
		}
	}
	else
	{
		cout << "Nu ati specificat nicio culoare." << endl;
	}
}