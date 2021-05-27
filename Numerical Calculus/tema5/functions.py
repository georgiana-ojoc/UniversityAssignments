import numpy as np


def addition_precision():
    m = 1
    while 1.0 + 10 ** -m != 1.0:
        m += 1
    return 10 ** (1 - m)


def get_formatted_matrix(matrix):
    return np.array2string(matrix, precision=3, separator='\t',
                           floatmode="fixed").replace('[ ', '').replace('[', '').replace(']', '')


def get_formatted_array(array):
    return np.array2string(array, precision=3, separator=' ',
                           floatmode="fixed").replace('[', '').replace(']', '').lstrip()


def get_jacobi_a(i=1):
    if i == 1:
        return 3, np.array([2.0, -1.0, 2.0, 0.0, -1.0, 2.0])
    if i == 2:
        return 3, np.array([0.0, 0.0, 0.0, 1.0, 1.0, 1.0])
    if i == 3:
        return 3, np.array([1.0, 1.0, 1.0, 2.0, 2.0, 2.0])
    if i == 4:
        return 4, np.array([1.0, 0.0, 1.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0])
    return 4, np.array([1.0, 2.0, 3.0, 3.0, 4.0, 5.0, 4.0, 5.0, 6.0, 7.0])


def get_svd_a(i=1):
    if i == 1:
        return 2, 3, np.array([[3.0, 2.0, 2.0], [2.0, 3.0, -2.0]])
    return 4, 3, np.array([[5.0, 7.0, 6.0], [7.0, 10.0, 8.0], [6.0, 8.0, 10.0], [5.0, 7.0, 9.0]])


def get_matrix_a(n, array_a):
    matrix_a = np.zeros((n, n))
    for i in range(n):
        for j in range(i):
            matrix_a[i, j] = matrix_a[j, i] = array_a[get_index(i, j)]
        matrix_a[i, i] = array_a[get_index(i, i)]
    return matrix_a


def get_index(i, j):
    if i < j:
        i, j = j, i
    return i * (i + 1) // 2 + j


def get_element(n, a):
    element, p, q = -np.inf, -1, -1
    for i in range(n):
        for j in range(i):
            x = a[get_index(i, j)]
            abs_x = np.abs(x)
            if abs_x > element:
                element, p, q = abs_x, i, j
    return element, p, q


def get_theta(a, p, q):
    alfa = (a[get_index(p, p)] - a[get_index(q, q)]) / (2.0 * a[get_index(p, q)])
    alfa_sign = 1 if alfa >= 0 else -1
    t = -alfa + alfa_sign * np.sqrt(alfa ** 2 + 1)
    c = 1 / (np.sqrt(1 + t ** 2))
    s = t / (np.sqrt(1 + t ** 2))
    return c, s, t


def compute_a(n, a, p, q, c, s, t):
    for i in range(n):
        if i == p or i == q:
            continue
        copy_of_a_of_i_p = a[get_index(p, i)]
        a[get_index(p, i)] = c * a[get_index(p, i)] + s * a[get_index(q, i)]
        a[get_index(q, i)] = -s * copy_of_a_of_i_p + c * a[get_index(q, i)]
    a[get_index(p, p)] += + t * a[get_index(p, q)]
    a[get_index(q, q)] += - t * a[get_index(p, q)]
    a[get_index(p, q)] = 0.0
    return a


def compute_u(n, u, p, q, c, s):
    for i in range(n):
        u_of_i_p = u[i, p]
        u[i, p] = c * u[i, p] + s * u[i, q]
        u[i, q] = -s * u_of_i_p + c * u[i, q]
    return u


def jacobi_approximate(n, a, epsilon=1e-15):
    i = 0
    u = np.eye(n)
    element, p, q = get_element(n, a)
    if element <= epsilon:
        return np.array([a[get_index(i, i)] for i in range(n)]), u
    c, s, t = get_theta(a, p, q)
    while True:
        a = compute_a(n, a, p, q, c, s, t)
        u = compute_u(n, u, p, q, c, s)
        element, p, q = get_element(n, a)
        if element <= epsilon:
            break
        c, s, t = get_theta(a, p, q)
        i += 1
        if i >= 1_000:
            break
    return np.array([a[get_index(i, i)] for i in range(n)]), u


def compute_norm(a, lambdas, u):
    return get_norm(a @ u - u * lambdas)


def get_norm(a):
    return np.linalg.norm(a)


def compute_series(a, epsilon=1e-15):
    _l = np.linalg.cholesky(a)
    next_a = _l.T @ _l
    i = 0
    while np.linalg.norm(next_a - a) > epsilon and i < 1_000:
        a = next_a
        _l = np.linalg.cholesky(a)
        next_a = _l.T @ _l
        i += 1
    return next_a


def get_svd(a):
    return np.linalg.svd(a)


def compute_rank(s, epsilon=1e-15):
    return len([sigma for sigma in s if sigma > epsilon])


def get_rank(a):
    return np.linalg.matrix_rank(a)


def compute_condition_number(s, epsilon=1e-15):
    _max = -np.inf
    _min = np.inf
    for sigma in s:
        if sigma > _max:
            _max = sigma
        if epsilon < sigma < _min:
            _min = sigma
    return _max / _min


def get_condition_number(a):
    return np.linalg.cond(a)


def compute_moore_penrose_pseudo_inverse(p, n, a, epsilon):
    u, s, v_t = get_svd(a)
    s_i = np.zeros((n, p))
    sigmas = [sigma for sigma in s if sigma > epsilon]
    for i in range(len(sigmas)):
        s_i[i, i] = 1.0 / sigmas[i]
    return v_t.T @ s_i @ u.T


def get_moore_penrose_pseudo_inverse(a):
    return np.linalg.pinv(a)


def compute_least_squares_pseudo_inverse(a):
    return np.linalg.inv(a.T @ a) @ a.T
