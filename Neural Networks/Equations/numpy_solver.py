import numpy


def minor(matrix, row_index, column_index):
    return numpy.array([numpy.concatenate((row[:column_index], row[column_index + 1:])) for row in
                        numpy.concatenate((matrix[:row_index], matrix[row_index + 1:]))])


def minors_matrix(matrix):
    return numpy.array([numpy.array([numpy.linalg.det(minor(matrix, row, column))
                                     for column in range(len(matrix[0]))]) for row in range(len(matrix))])


def cofactors_matrix(matrix):
    return numpy.array([numpy.array([(-1) ** (row + column) * matrix[row][column]
                                     for column in range(len(matrix[0]))]) for row in range(len(matrix))])


def invert(matrix):
    determinant = numpy.linalg.det(matrix)
    if determinant == 0:
        return None
    adjunct_matrix = numpy.transpose(cofactors_matrix(minors_matrix(matrix)))
    return adjunct_matrix / determinant


def numpy_solve(coefficients, results):
    inverse = invert(coefficients)
    if inverse is None:
        return None
    return numpy.dot(inverse, results)
