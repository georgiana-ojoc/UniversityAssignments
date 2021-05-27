from helper import *
import numpy as np


def get_formatted_matrix(matrix):
    n, matrix = matrix
    result = np.zeros((n, n))
    for (i, j) in matrix:
        result[i][j] = matrix[(i, j)]
    return np.array2string(result, precision=3, separator='\t',
                           floatmode="fixed").replace('[ ', '').replace('[', '').replace(']', '')


def read_sparse_matrix(name):
    with open(name) as file:
        lines = file.read().split('\n')
    n = int(lines[0])
    matrix = {}
    for line in lines[2:]:
        line = line.split(", ")
        value = float(line[0])
        i = int(line[1])
        j = int(line[2])

        if (i, j) in matrix:
            matrix[(i, j)] = matrix[(i, j)] + value
        else:
            matrix[(i, j)] = value
    return n, matrix


def compute_a_plus_b(p, q, a, b):
    n, a = a
    if n != len(b[0]):
        return None
    a_plus_b = {}

    for (i, j) in a:
        value = a[(i, j)]
        if i == j:
            new_value = value + b[0][i]
        elif i + p == j:
            new_value = value + b[1][i]
        elif i - q == j:
            new_value = value + b[2][i - q]
        else:
            new_value = value
        a_plus_b[(i, j)] = new_value

    for i in range(n):
        if i >= q:
            if (i, i - 1) not in a_plus_b:
                a_plus_b[(i, i - 1)] = b[2][i - q]
        if (i, i) not in a_plus_b:
            a_plus_b[(i, i)] = b[0][i]
        if i < n - p:
            if (i, i + 1) not in a_plus_b:
                a_plus_b[(i, i + 1)] = b[1][i]

    return n, a_plus_b


def add_product(i, j, value, n, p, q, b, a_ori_b):
    if (i, j - q) in a_ori_b:
        a_ori_b[(i, j - q)] += value * b[2][j - q]
    if (i, j) in a_ori_b:
        a_ori_b[(i, j)] += value * b[0][j]
    if (i, j + p) in a_ori_b:
        a_ori_b[(i, j + p)] += + value * b[1][j]

    if not (i, j - q) in a_ori_b and j >= q:
        a_ori_b[(i, j - q)] = value * b[2][j - q]
    if not (i, j) in a_ori_b:
        a_ori_b[(i, j)] = value * b[0][j]
    if not (i, j + p) in a_ori_b and j < n - p:
        a_ori_b[(i, j + p)] = value * b[1][j]

    return a_ori_b


def compute_a_ori_b(p, q, a, b):
    n, a = a
    if n != len(b[0]):
        return None
    a_ori_b = {}

    for (i, j) in a:
        value = a[(i, j)]
        a_ori_b = add_product(i, j, value, n, p, q, b, a_ori_b)

    return n, a_ori_b


def compute_bonus_a_ori_b(p_1, q_1, b_1, p_2, q_2, b_2):
    n = len(b_1[0])
    if n != len(b_2[0]):
        return None
    a_ori_b = {}

    for i in range(n - q_1):
        a_ori_b = add_product(i + q_1, i, b_1[2][i], n, p_2, q_2, b_2, a_ori_b)

    for i in range(n):
        a_ori_b = add_product(i, i, b_1[0][i], n, p_2, q_2, b_2, a_ori_b)

    for i in range(n - p_1):
        a_ori_b = add_product(i, i + p_1, b_1[1][i], n, p_2, q_2, b_2, a_ori_b)

    return n, a_ori_b


def remove_zeros(matrix):
    for key in matrix.keys():
        if matrix[key] == 0:
            del matrix[key]
    return matrix


def are_equal_matrices(x, y, epsilon):
    m, x = x
    n, y = y
    if m != n:
        return False

    x = remove_zeros(x)
    y = remove_zeros(y)

    if len(x) != len(y):
        return False

    for (i, j) in x:
        if (i, j) not in y:
            return False

        if abs(x[(i, j)] - y[(i, j)]) >= epsilon:
            return False

    return True


def main():
    epsilon = addition_precision()

    a = read_sparse_matrix("a.txt")

    b = read_tridiagonal_matrix("b.txt")
    p = b[0]
    q = b[1]
    b = b[2:]

    b_1 = read_tridiagonal_matrix("b_1.txt")
    p_1 = b_1[0]
    q_1 = b_1[1]
    b_1 = b_1[2:]

    b_2 = read_tridiagonal_matrix("b_2.txt")
    p_2 = b_2[0]
    q_2 = b_2[1]
    b_2 = b_2[2:]

    read_a_plus_b = read_sparse_matrix("a_plus_b.txt")
    read_a_ori_b = read_sparse_matrix("a_ori_b.txt")

    computed_a_plus_b = compute_a_plus_b(p, q, a, b)
    computed_a_ori_b = compute_a_ori_b(p, q, a, b)
    computed_bonus_a_ori_b = compute_bonus_a_ori_b(p_1, q_1, b_1, p_2, q_2, b_2)

    print("Matrix addition: ", are_equal_matrices(read_a_plus_b, computed_a_plus_b, epsilon))
    print("Matrix multiplication: ", are_equal_matrices(read_a_ori_b, computed_a_ori_b, epsilon))
    print(get_formatted_matrix(computed_bonus_a_ori_b))


if __name__ == '__main__':
    main()
