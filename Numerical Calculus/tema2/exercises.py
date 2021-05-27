from scipy.linalg import lu, lu_factor, lu_solve

import copy
import math
import numpy as np


def get_n():
    while True:
        try:
            n = int(input("Enter n (integer): "))
        except ValueError:
            print("n must be integer.")
        else:
            print("n = {}".format(n))
            return n


def addition_precision():
    m = 1
    while 1.0 + 10 ** -m != 1.0:
        m += 1
    return 10 ** (1 - m)


def get_epsilon():
    print("Addition precision is: {}".format(addition_precision()))
    while True:
        try:
            epsilon = float(input("Enter epsilon (number): "))
        except ValueError:
            print("epsilon must be number.")
        else:
            print("epsilon = {}".format(epsilon))
            return epsilon


def get_option(collection):
    while True:
        try:
            option = int(input("Enter 1 to get {} from console, 2 to get {} from file or 3 to get random {}: "
                               .format(collection, collection, collection)))
        except ValueError:
            print("option must be integer.")
        else:
            if option in (1, 2, 3):
                print("option = {}".format(option))
                return option
            print("option must be 1, 2 or 3.")


def get_A_from_console(n):
    A = np.zeros(shape=(n, n))
    for i in range(n):
        for j in range(n):
            while True:
                try:
                    A[i, j] = float(input("Enter A[{}, {}] (number): ".format(i, j)))
                except ValueError:
                    print("A[{} {}] must be number.".format(i, j))
                else:
                    break
    return A


def is_symmetric(A, n, epsilon):
    if A.shape != (n, n):
        return False
    return np.all(np.abs(A - A.T) < epsilon)


def is_positive_definite(A, n, epsilon):
    if not is_symmetric(A, n, epsilon):
        return False

    for i in range(1, n + 1):
        if np.linalg.det(A[:i, :i]) <= 0:
            return False

    return True


def get_b_from_console(n):
    b = np.zeros(shape=n)
    for i in range(n):
        while True:
            try:
                b[i] = float(input("Enter b[{}] (number): ".format(i)))
            except ValueError:
                print("b[{}] must be number.".format(i))
            else:
                break
    return b


def get_from_file(file):
    return np.loadtxt(file, delimiter=' ')


def get_random(low, high, shape):
    return np.random.uniform(low=low, high=high, size=shape)


def get_formatted_matrix(matrix):
    return np.array2string(matrix, precision=3, separator='\t',
                           floatmode="fixed").replace('[ ', '').replace('[', '').replace(']', '')


def get_formatted_array(array):
    return np.array2string(array, precision=3, separator=' ',
                           floatmode="fixed").replace('[', '').replace(']', '').lstrip()


def print_matrix_to_file(file, matrix):
    np.savetxt(file, matrix, fmt="%.3f")


def print_array_to_file(file, array):
    np.savetxt(file, array, fmt="%.3f", newline=' ')


def get_A(n):
    option = get_option('A')
    if option == 1:
        A = get_A_from_console(n)
    elif option == 2:
        while True:
            try:
                file = input("Enter file name: ")
                A = get_from_file(file)
                A = A.reshape((n, n))
            except:
                print("The file is not valid.")
            else:
                break
    else:
        A = get_random(low=-100, high=100, shape=(n, n))
    print("A =")
    print(get_formatted_matrix(A))
    return A


def get_b(n):
    option = get_option('b')
    if option == 1:
        b = get_b_from_console(n)
    elif option == 2:
        while True:
            try:
                file = input("Enter file name: ")
                b = get_from_file(file)
                b = b.reshape((n,))
            except:
                print("The file is not valid.")
            else:
                break
    else:
        b = get_random(low=-100, high=100, shape=n)
    print("b = {}".format(get_formatted_array(b)))
    return b


def get_bonus_A(A, n):
    return A[np.tril_indices(n)]


def get_input():
    """
    Exercise 7
    """
    n = get_n()
    # n = 3
    epsilon = get_epsilon()
    # epsilon = 1e-15
    while True:
        A = get_A(n)
        # A = np.array([[4., 2., 4.], [2., 2., 2.], [4., 2., 5.]])
        # A = np.array([[6., 15., 55.], [15., 55., 225.], [55., 225., 979.]])
        if is_positive_definite(A, n, epsilon):
            break
        else:
            print("A is not positive definite.")
    d = copy.deepcopy(A.diagonal())
    bonus_A = get_bonus_A(A, n)
    copy_of_A = copy.deepcopy(A)
    b = get_b(n)
    # b = np.array([12, 6, 13])
    # b = np.array([76., 295., 1259.])
    return A, b, n, epsilon, bonus_A, copy_of_A, d


def Cholesky_decomposition(A, n, epsilon):
    """
    Exercise 1
    """
    for p in range(n):
        x = A[p, p] - sum([A[p, j] ** 2 for j in range(p)])
        if x < 0:
            return None
        A[p, p] = np.sqrt(x)
        if abs(A[p, p]) <= epsilon:
            return None
        for i in range(p + 1, n):
            x = sum([A[i, j] * A[p, j] for j in range(p)])
            A[i, p] = (A[i, p] - x) / A[p, p]
    return A


def index(i, j):
    return i * (i + 1) // 2 + j


def bonus_Cholesky_decomposition(bonus_A, n, epsilon):
    """
    Exercise 1 (bonus)
    """
    L = np.zeros(shape=(n * (n + 1) // 2,))
    for p in range(n):
        x = bonus_A[index(p, p)] - sum([L[index(p, j)] ** 2 for j in range(p)])
        if x < 0:
            return None
        L[index(p, p)] = np.sqrt(x)
        if abs(L[index(p, p)]) <= epsilon:
            return None
        for i in range(p + 1, n):
            x = sum([L[index(i, j)] * L[index(p, j)] for j in range(p)])
            L[index(i, p)] = (bonus_A[index(i, p)] - x) / L[index(p, p)]
    return L


def determinant(A):
    """
    Exercise 2
    """
    return A.diagonal().prod() ** 2


def direct_substitution(A, b, n, epsilon):
    y = np.zeros(shape=(n,))
    for i in range(n):
        if abs(A[i, i]) <= epsilon:
            return None
        s = sum([A[i, j] * y[j] for j in range(i)])
        y[i] = (b[i] - s) / A[i, i]
    return y


def inverse_substitution(A, b, n, epsilon):
    x = np.zeros(shape=(n,))
    for i in reversed(range(n)):
        if abs(A[i, i]) <= epsilon:
            return None
        s = sum([A[j, i] * x[j] for j in range(i + 1, n)])
        x[i] = (b[i] - s) / A[i, i]
    return x


def system_solution(A, b, n, epsilon):
    """
    Exercise 3
    """
    y = direct_substitution(A, b, n, epsilon)
    x = inverse_substitution(A, y, n, epsilon)
    return x


def bonus_direct_substitution(L, b, n, epsilon):
    y = np.zeros(shape=(n,))
    for i in range(n):
        if abs(L[index(i, i)]) <= epsilon:
            return None
        s = sum([L[index(i, j)] * y[j] for j in range(i)])
        y[i] = (b[i] - s) / L[index(i, i)]
    return y


def bonus_inverse_substitution(L, b, n, epsilon):
    x = np.zeros(shape=(n,))
    for i in reversed(range(n)):
        if abs(L[index(i, i)]) <= epsilon:
            return None
        s = sum([L[index(j, i)] * x[j] for j in range(i + 1, n)])
        x[i] = (b[i] - s) / L[index(i, i)]
    return x


def bonus_system_solution(L, b, n, epsilon):
    """
    Exercise 3 (bonus)
    """
    y = bonus_direct_substitution(L, b, n, epsilon)
    x = bonus_inverse_substitution(L, y, n, epsilon)
    return x


def verify_system_solution(A, d, b, x, n, threshold=10 ** -9):
    """
    Exercise 4
    """
    norm = 0
    for i in range(n):
        y = 0
        for j in range(n):
            if i == j:
                y += d[i] * x[j]
            else:
                if i < j:
                    y += A[i][j] * x[j]
                else:
                    y += A[j][i] * x[j]
        norm += (y - b[i]) ** 2
    norm = math.sqrt(norm)
    return "{}\n{} < {} ({})".format(norm, norm, threshold, norm < threshold)


def LU_decomposition(copy_of_A, b):
    """
    Exercise 5
    """
    P, L, U = lu(copy_of_A)
    x = lu_solve(lu_factor(copy_of_A), b)
    return P, L, U, x


def inverse(A, n, epsilon):
    e = np.eye(n, n)
    x = system_solution(A, e[0], n, epsilon)
    inv = x.reshape((n, 1))
    for i in range(1, n):
        x = system_solution(A, e[i], n, epsilon)
        x = x.reshape((n, 1))
        inv = np.hstack((inv, x))
    return inv


def numpy_inverse(copy_of_A):
    return np.linalg.inv(copy_of_A)


def verify_inverse(inv, np_inv, threshold=10 ** -9):
    """
    Exercise 6
    """
    norm = np.linalg.norm(inv - np_inv)
    return "{}\n{} < {} ({})".format(norm, norm, threshold, norm < threshold)
