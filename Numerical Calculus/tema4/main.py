import math
import numpy as np


def addition_precision():
    m = 1
    while 1.0 + 10 ** -m != 1.0:
        m += 1
    return 10 ** (1 - m)


def get_formatted_matrix(n, p, q, a, b, c):
    result = np.zeros((n, n))
    for i in range(n):
        result[i, i] = a[i]
        if i - q >= 0:
            result[i, i - q] = c[i - q]
        if i + p < n:
            result[i, i + p] = b[i]
    return np.array2string(result, precision=3, separator='\t',
                           floatmode="fixed").replace('[ ', '').replace('[', '').replace(']', '')


def get_formatted_array(array):
    return np.array2string(np.array(array), precision=3, separator=' ',
                           floatmode="fixed").replace('[', '').replace(']', '').lstrip()


def read_tridiagonal_matrix(name, epsilon):
    with open(name) as file:
        lines = file.read().split('\n')
    n = int(lines[0])
    q = int(lines[1])
    if q >= n:
        print('q is greater or equal than n in "{}"'.format(name))
    p = int(lines[2])
    if p >= n:
        print('p is greater or equal than n in "{}"'.format(name))
    a = []
    for line in lines[4:n + 4]:
        number = float(line)
        if abs(number) <= epsilon:
            print('"{}" contains null values on the main diagonal'.format(name))
            return None
        a += [number]
    c = []
    for line in lines[n + 5:2 * n - q + 5]:
        c += [float(line)]
    b = []
    for line in lines[2 * n - q + 6:3 * n - p - q + 6]:
        b += [float(line)]
    return n, p, q, a, b, c


def read_array(name):
    with open(name) as file:
        lines = file.read().split('\n')
    n = int(lines[0])
    f = []
    for line in lines[2:n + 2]:
        f += [float(line)]
    return n, f


def is_strictly_diagonally_dominant(n, p, q, a, b, c):
    for j in range(n):
        s = 0
        if j + p < n:
            s += abs(b[j])
        if j - q >= 0:
            s += abs(c[j - q])
        if abs(a[j]) <= s:
            return False
    return True


# matrix is symmetric positive-definite or
# matrix is strictly or irreducibly diagonally dominant
def compute_solution(n, p, q, a, b, c, f, epsilon):
    x = [0.0 for _ in range(n)]
    iteration = 0
    iterations = 10_000
    while iteration < iterations:
        delta = 0
        for i in range(n):
            value = f[i]
            if i - q >= 0:
                value -= c[i - q] * x[i - q]
            if i + p < n:
                value -= b[i] * x[i + p]
            value /= a[i]
            delta += (value - x[i]) ** 2
            x[i] = value
        delta = math.sqrt(delta)
        if delta > 10 ** 8:
            return False, x
        if delta < epsilon:
            break
        iteration += 1
    return True, x


def compute_norm(n, p, q, a, b, c, x, f):
    norm = -math.inf
    for i in range(n):
        value = a[i] * x[i] - f[i]
        if i + p < n:
            value += b[i] * x[i + p]
        if i - q >= 0:
            value += c[i - q] * x[i - q]
        value = abs(value)
        if value > norm:
            norm = value
    return norm


def get_solution(n, index):
    if index == 1:
        return [1.0 for _ in range(n)]
    if index == 2:
        return [1.0 / 3.0 for _ in range(n)]
    if index == 3:
        return [2.0 * (i + 1) / 5.0 for i in range(n)]
    if index == 4:
        return [2000.0 / (i + 1) for i in range(n)]
    if index == 5:
        return [2.0 for _ in range(n)]
    if index == 6:
        return [1.0 for _ in range(5)]
    return None


def are_equal(n, x, y, epsilon):
    for i in range(n):
        if abs(x[i] - y[i]) >= epsilon:
            return False
    return True


def main():
    epsilon = addition_precision()
    for i in range(1, 7):
        result = read_tridiagonal_matrix("a_{}.txt".format(i), epsilon)
        if result is None:
            continue
        n_a, p, q, a, b, c = result
        n_f, f = read_array("f_{}.txt".format(i))
        if n_a != n_f:
            print('n from "a_{}.txt" is not equal to n from "f_{}.txt"'.format(i, i))
            continue
        convergent, x = compute_solution(n_a, p, q, a, b, c, f, epsilon)
        norm = compute_norm(n_a, p, q, a, b, c, x, f)
        if convergent:
            print("{}. convergence (norm = {})".format(i, norm))
        else:
            print("{}. divergence (norm = {})".format(i, norm))


if __name__ == '__main__':
    main()
