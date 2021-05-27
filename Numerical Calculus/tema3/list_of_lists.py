from helper import *
import numpy as np


def get_formatted_matrix(matrix):
    n = len(matrix)
    result = np.zeros((n, n))
    for i in range(n):
        for (j, value) in matrix[i]:
            result[i][j] = value
    return np.array2string(result, precision=3, separator='\t',
                           floatmode="fixed").replace('[ ', '').replace('[', '').replace(']', '')


def read_sparse_matrix(name):
    with open(name) as file:
        lines = file.read().split('\n')
    n = int(lines[0])
    matrix = [[] for _ in range(n)]

    for line in lines[2:]:
        line = line.split(", ")
        value = float(line[0])
        i = int(line[1])
        j = int(line[2])
        k = 0
        m = len(matrix[i])

        while k < m:
            if matrix[i][k][0] == j:
                break
            k += 1
        if k < m:
            matrix[i][k] = (j, matrix[i][k][1] + value)
        else:
            matrix[i] += [(j, value)]

    return n, matrix


def compute_a_plus_b(p, q, a, b):
    n, a = a
    if n != len(b[0]):
        return None
    a_plus_b = [[] for _ in range(n)]

    for i in range(n):
        exists_inferior = False
        exists_principal = False
        exists_superior = False

        for (j, value) in a[i]:
            if i == j:
                new_value = value + b[0][i]
                exists_principal = True
            elif i + p == j:
                new_value = value + b[1][i]
                exists_superior = True
            elif i - q == j:
                new_value = value + b[2][i - q]
                exists_inferior = True
            else:
                new_value = value
            a_plus_b[i] += [(j, new_value)]

        if not exists_inferior and i >= q:
            a_plus_b[i] += [(i - q, b[2][i - q])]
        if not exists_principal:
            a_plus_b[i] += [(i, b[0][i])]
        if not exists_superior and i < n - p:
            a_plus_b[i] += [(i + p, b[1][i])]

    return n, a_plus_b


def add_product(i, j, value, n, p, q, b, a_ori_b):
    exists_principal = False
    exists_superior = False
    exists_inferior = False

    for index, (column, old_value) in enumerate(a_ori_b[i]):
        if column == j - q:
            a_ori_b[i][index] = (column, old_value + value * b[2][j - q])
            exists_inferior = True
        elif column == j:
            a_ori_b[i][index] = (column, old_value + value * b[0][j])
            exists_principal = True
        elif column == j + p:
            a_ori_b[i][index] = (column, old_value + value * b[1][j])
            exists_superior = True

    if not exists_inferior and j >= q:
        a_ori_b[i] += [(j - q, value * b[2][j - q])]
    if not exists_principal:
        a_ori_b[i] += [(j, value * b[0][j])]
    if not exists_superior and j < n - p:
        a_ori_b[i] += [(j + p, value * b[1][j])]

    return a_ori_b


def compute_a_ori_b(p, q, a, b):
    n, a = a
    if n != len(b[0]):
        return None
    a_ori_b = [[] for _ in range(n)]

    for i in range(n):
        for (j, value) in a[i]:
            a_ori_b = add_product(i, j, value, n, p, q, b, a_ori_b)

    return n, a_ori_b


def compute_bonus_a_ori_b(p_1, q_1, b_1, p_2, q_2, b_2):
    n = len(b_1[0])
    if n != len(b_2[0]):
        return None
    a_ori_b = [[] for _ in range(n)]

    for i in range(n - q_1):
        a_ori_b = add_product(i + q_1, i, b_1[2][i], n, p_2, q_2, b_2, a_ori_b)

    for i in range(n):
        a_ori_b = add_product(i, i, b_1[0][i], n, p_2, q_2, b_2, a_ori_b)

    for i in range(n - p_1):
        a_ori_b = add_product(i, i + p_1, b_1[1][i], n, p_2, q_2, b_2, a_ori_b)

    return a_ori_b


def remove_zeros(n, matrix):
    for i in range(n):
        for (j, value) in matrix[i]:
            if value == 0:
                matrix[i].remove((j, value))
    return matrix


def are_equal_matrices(x, y, epsilon):
    m, x = x
    n, y = y
    if m != n:
        return False

    x = remove_zeros(m, x)
    y = remove_zeros(n, y)

    for i in range(n):
        m = len(x[i])
        if m != len(y[i]):
            return False
        sorted_x_i = sorted(x[i], key=lambda pair: pair[0])
        sorted_y_i = sorted(y[i], key=lambda pair: pair[0])

        for j in range(m):
            if sorted_x_i[j][0] != sorted_y_i[j][0]:
                return False
            if abs(sorted_x_i[j][1] - sorted_y_i[j][1]) >= epsilon:
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
