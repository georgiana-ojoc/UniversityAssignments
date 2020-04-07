#include "Header.h"

using namespace std;

int main()
{
	Video video1("Skateboarding", 24);
	Video video2("Driving", 10);
	Video video3("Biking", 19);
	EditorVideo editor = { video1, video2 };
	editor += video3;
	editor.afiseaza_intervale_video();
	editor = ((video1 | video3) | video2);
	for (auto it : editor) {
		cout << it << endl;
	}
	return 0;
}