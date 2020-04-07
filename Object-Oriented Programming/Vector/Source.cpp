#include <iostream>

using namespace std;

void heapify(int* vector, int total, int index) {
	int largest = index;
	int left = 2 * index + 1;
	int right = 2 * index + 2;
	if (left < total && vector[left] > vector[largest]) {
		largest = left;
	}
	if (right < total && vector[right] > vector[largest]) {
		largest = right;
	}
	if (largest != index) {
		swap(vector[index], vector[largest]);
		heapify(vector, total, largest);
	}
}

void heapSort(int* vector, int total) {
	int index;
	for (index = total / 2 - 1; index >= 0; index--) {
		heapify(vector, total, index);
	}
	for (index = total - 1; index >= 0; index--) {
		swap(vector[index], vector[0]);
		heapify(vector, index, 0);
	}
}

int main() {
	FILE *f = fopen("numbers.txt", "r");
	char* string = (char*)malloc(8 * sizeof(char));
	int length;
	int* vector = (int*)malloc(1000 * sizeof(int));
	int index = 0;
	while (fgets(string, 8, f)) {
		length = strlen(string);
		if (string[length - 1] == '\n') {
			string[length - 1] = '\0';
		}
		vector[index++] = atoi(string);
	}
	fclose(f);
	free(string);
	int total = index;
	heapSort(vector, total);
	for (index = 0; index < total; index++) {
		printf("%d ", vector[index]);
	}
	free(vector);
}