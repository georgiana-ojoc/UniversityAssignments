def minor(matrix, row_index, column_index):
    return [row[:column_index] + row[column_index + 1:] for row in matrix[:row_index] + matrix[row_index + 1:]]


def two_dimensional_determinant(matrix):
    return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]


def minors_matrix(matrix):
    return [[two_dimensional_determinant(minor(matrix, row, column))
             for column in range(len(matrix[0]))] for row in range(len(matrix))]


def cofactors_matrix(matrix):
    return [[(-1) ** (row + column) * matrix[row][column]
             for column in range(len(matrix[0]))] for row in range(len(matrix))]


def transpose(matrix):
    return [[matrix[row][column] for row in range(len(matrix))]
            for column in range(len(matrix[0]))]


def three_dimensional_determinant(matrix):
    determinant = 0
    for column in range(len(matrix[0])):
        determinant += (-1) ** column * matrix[0][column] * two_dimensional_determinant(minor(matrix, 0, column))
    return determinant


def invert(matrix):
    determinant = three_dimensional_determinant(matrix)
    if determinant == 0:
        return None
    adjunct_matrix = transpose(cofactors_matrix(minors_matrix(matrix)))
    return [[adjunct_matrix[row][column] / determinant for column in range(len(adjunct_matrix))]
            for row in range(len(adjunct_matrix))]


def scalar_product(matrix, array):
    solutions = []
    for row in range(len(matrix)):
        solutions.append(sum([matrix[row][column] * array[column] for column in range(len(array))]))
    return solutions


def python_solve(coefficients, results):
    inverse = invert(coefficients)
    if inverse is None:
        return None
    return scalar_product(inverse, results)
