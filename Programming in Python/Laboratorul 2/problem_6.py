def put_zero_under_diagonal(matrix):
    for row in range(len(matrix)):
        for column in range(row):
            matrix[row][column] = 0
    return matrix


def main():
    print(put_zero_under_diagonal([[1, 2, 3], [4, 5, 6], [7, 8, 9]]))


if __name__ == '__main__':
    main()
