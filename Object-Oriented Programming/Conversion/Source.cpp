#include <iostream>

using namespace std;

bool Convert(unsigned int number, unsigned int toBase, char*& result, unsigned int resultMaxSize) {
	if (toBase < 2 || toBase > 16) {
		return false;
	}
	int index = 0;
	if (toBase < 11) {
		while (number) {
			if (index == resultMaxSize) {
				result = NULL;
				return false;
			}
			*(result + index) = number % toBase + '0';
			number /= toBase;
			index++;
		}
	}
	else {
		while (number) {
			if (index == resultMaxSize) {
				result = NULL;
				return false;
			}
			if (number % toBase < 10) {
				*(result + index) = number % toBase + '0';
			}
			else {
				*(result + index) = number % toBase + 'A' - 10;
			}
			number /= toBase;
			index++;
		}
	}
	*(result + index) = '\0';
	_strrev(result);
	if (result == NULL) {
		return false;
	}
	return true;
}

int main() {
	unsigned int resultMaxSize = 8;
	char* result = (char*)malloc((resultMaxSize + 1) * sizeof(char));
	if (result == NULL) {
		return 0;
	}
	printf("%s %s", Convert(255, 2, result, resultMaxSize) ? "true" : "false", result);
	free(result);
}