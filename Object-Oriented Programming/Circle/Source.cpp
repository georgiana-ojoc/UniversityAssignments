#include <iostream>

#define MATRIX_HEIGHT 6
#define MATRIX_WIDTH 5

using namespace std;

int Matrix[MATRIX_HEIGHT][MATRIX_WIDTH];

void Circle(int* ptr, int cx, int cy, int ray) {
	for (int i = 0; i < MATRIX_HEIGHT; i++) {
		for (int j = 0; j < MATRIX_WIDTH; j++) {
			if (ray == ceil(sqrt((cx - i) * (cx - i) + (cy - j) * (cy - j)))) {
				*(ptr + MATRIX_WIDTH * i + j) = 1;
			}
		}
	}
}

void main() {
	Circle(&Matrix[0][0], 2, 2, 2);
	for (int i = 0; i < MATRIX_HEIGHT; i++) {
		for (int j = 0; j < MATRIX_WIDTH; j++) {
			printf("%d ", Matrix[i][j]);
		}
		printf("\n");
	}
}