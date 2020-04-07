#include "Header.h"

using namespace std;

Video::Video(string s, int l) {
	nume = s;
	lungime = l;
}

string Video::GetNume() {
	return nume;
}

int Video::GetLungime() {
	return lungime;
}

ostream& operator << (ostream& out, Video v) {
	out << "Film: " << v.nume << " (" << v.lungime << ')';
	return out;
}

Video operator | (Video v1, Video v2) {
	Video v(v1.nume + v2.nume, v1.lungime + v2.lungime);
	return v;
}

EditorVideo::EditorVideo() {};

EditorVideo::EditorVideo(initializer_list<Video> l) {
	initializer_list<Video>::iterator it;
	for (it = l.begin(); it != l.end(); it++) {
		continut.push_back(*it);
	}
}

vector<Video>::iterator EditorVideo::begin() {
	return continut.begin();
}

vector<Video>::iterator EditorVideo::end() {
	return continut.end();
}

EditorVideo EditorVideo::operator += (Video v) {
	continut.push_back(v);
	return *this;
}

EditorVideo EditorVideo::operator = (Video v) {
	continut.clear();
	continut.push_back(v);
	return *this;
}

void EditorVideo::afiseaza_intervale_video() {
	int total = 0;
	for (auto it : continut) {
		cout << it.GetNume() << ' ' << it.GetLungime() << endl;
		total += it.GetLungime();
	}
	cout << total << endl;
}