import math
import matplotlib.pyplot as plt
import numpy as np


def x_0_x_n(i=1):
    if i == 1:
        return 1.0, 5.0
    if i == 2:
        return 0.0, 1.5
    return 0.0, 2.0


def f(x, i=1):
    if i == 1:
        return x ** 2 - 12 * x + 30
    if i == 2:
        return math.sin(x) - math.cos(x)
    return 2 * x ** 3 - 3 * x + 15


def generate_xs(x_0, x_n, n=6):
    return np.concatenate(([x_0], np.sort(np.random.uniform(x_0, x_n, n - 1)), [x_n]))


def compute_ys(xs, i=1):
    return np.array([f(x, i) for x in xs])


def compute_polynomial(xs, ys):
    n = xs.size - 1
    b = np.empty(shape=(0, n + 1))
    for i in range(n + 1):
        row = xs ** i
        b = np.vstack((b, row))
    return np.linalg.inv(b.T) @ ys


def compute_y(x, p):
    y = 0
    for p_i in np.flip(p):
        y = y * x + p_i
    return y


def compute_x_norm(x, y, i=1):
    return np.abs(y - f(x, i))


def compute_xs_norm(xs, ys, p):
    return np.sum([np.abs(compute_y(xs[i], p) - ys[i]) for i in range(xs.size)])


def compute_i_0(x, xs):
    n = xs.size - 1
    for i in range(n):
        if xs[i] <= x <= xs[i + 1]:
            return i
    return n


def d(x, h=10 ** -5, i=1):
    return (3 * f(x, i) - 4 * f(x - h, i) + f(x - 2 * h, i)) / (2.0 * h)


def compute_as(xs, ys, i_0, h=10 ** -5, i=1):
    a_i_0 = d(xs[0], h, i)
    a_i_1 = -a_i_0 + 2.0 * (ys[1] - ys[0]) / (xs[1] - xs[0])
    for i in range(1, i_0 + 1):
        a_i_0 = a_i_1
        a_i_1 = -a_i_0 + 2.0 * (ys[i + 1] - ys[i]) / (xs[i + 1] - xs[i])
    return a_i_0, a_i_1


def compute_spline(x, xs, ys, h=10 ** -5, i=1):
    i_0 = compute_i_0(x, xs)
    a_i_0, a_i_1 = compute_as(xs, ys, i_0, h, i)
    return (a_i_1 - a_i_0) / (2.0 * (xs[i_0 + 1] - xs[i_0])) * (x - xs[i_0]) ** 2 + a_i_0 * (x - xs[i_0]) + ys[i_0]


def plot(xs, ys, p, h=10 ** -5, i=1):
    _xs = np.linspace(xs[0], xs[-1], 100)
    f_ys = compute_ys(_xs, i)
    p_ys = np.array([compute_y(x, p) for x in _xs])
    s_ys = np.array([compute_spline(x, xs, ys, h, i) for x in _xs])
    figure, axis = plt.subplots(3)
    figure.tight_layout(pad=3.0)
    axis[0].plot(_xs, f_ys)
    axis[0].set_title("function")
    axis[1].plot(_xs, p_ys, "tab:green")
    axis[1].set_title("polynomial")
    axis[2].plot(_xs, s_ys, "tab:purple")
    axis[2].set_title("spline")
    plt.show()
