#pragma once
#include <iostream>
#include <string>
#include <vector>

using namespace std;

class Video {
	string nume;
	int lungime;
public:
	Video(string, int);
	string GetNume();
	int GetLungime();
	friend ostream& operator << (ostream& out, Video v);
	friend Video operator | (Video v1, Video v2);
};

class EditorVideo
{
	vector<Video> continut;
public:
	EditorVideo();
	EditorVideo(initializer_list<Video>);
	vector<Video>::iterator begin();
	vector<Video>::iterator end();
	EditorVideo operator += (Video v);
	EditorVideo operator = (Video v);
	void afiseaza_intervale_video();
};