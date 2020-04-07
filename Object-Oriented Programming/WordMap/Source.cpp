#include <algorithm>
#include <fstream>
#include <iostream>
#include <map>
#include <string>
#include <vector>

using namespace std;

bool compare(pair<string, unsigned int> & iterator1, pair<string, unsigned int> & iterator2) {
	return iterator1.second > iterator2.second;
}

int main() {
	ifstream file("text.txt");
	// string content { istreambuf_iterator<char>(file), istreambuf_iterator<char>() };
	string content;
	getline(file, content);
	unsigned int contentIndex;
	file.close();
	map<string, unsigned int> _map;
	map<string, unsigned int>::iterator mapIterator;
	string word;
	unsigned int start = 0;
	int position = content.find_first_of(" .?!", start);
	while (position != content.npos) {
		word = content.substr(start, position - start);
		if (_map.find(word) != _map.end()) {
			_map[word]++;
		}
		else {
			_map[word] = 1;
		}
		start = position + 1;
		position = content.find_first_of(" .!?", start);
	}
	/*bool exists;
	for (contentIndex = 0; contentIndex < content.size(); contentIndex++) {
		if (isalpha(content[contentIndex])) {
			word += content[contentIndex];
		}
		else {
			exists = false;
			for (mapIterator = _map.begin(); mapIterator != _map.end() && !exists; mapIterator++) {
				if (mapIterator->first == word) {
					_map[word]++;
					exists = true;
				}
			}
			if (!exists) {
				_map[word] = 1;
			}
			word.clear();
		}
	}*/
	vector<pair<string, unsigned int>> _vector(_map.begin(), _map.end());
	sort(_vector.begin(), _vector.end(), compare);
	unsigned int index = 1;
	for (vector<pair<string, unsigned int>>::iterator vectorIterator = _vector.begin();
		index <= 10 && vectorIterator != _vector.end();
		index++, vectorIterator++) {
		cout << vectorIterator->second << ' ' << vectorIterator->first << endl;
	}
}