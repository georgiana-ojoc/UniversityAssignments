import math


def addition_precision():
    m = 1
    while 1.0 + 10 ** -m != 1.0:
        m += 1
    return 10 ** (1 - m)


def f(x, i=1):
    if i == 1:
        return 1.0 / 3 * x ** 3 - 2 * x ** 2 + 2 * x + 3
    if i == 2:
        return x ** 2 + math.sin(x)
    return x ** 4 - 6 * x ** 3 + 13 * x ** 2 - 12 * x + 4


def expect_x_star(i=1):
    if i == 1:
        return 2 + math.sqrt(2)
    if i == 2:
        return -0.4501836112948
    return 1.0, 2.0


def g(x, h=10 ** -5, i_f=1, i_g=1):
    if i_g == 1:
        return (3 * f(x, i_f) - 4 * f(x - h, i_f) + f(x - 2 * h, i_f)) / (2.0 * h)
    return (-f(x + 2 * h, i_f) + 8 * f(x + h, i_f) - 8 * f(x - h, i_f) + f(x - 2 * h, i_f)) / (12.0 * h)


def f_2(x, h=10 ** -5, i=1):
    return (-f(x + 2 * h, i) + 16 * f(x + h, i) - 30 * f(x, i) + 16 * f(x - h, i)
            - f(x - 2 * h, i)) / (12 * h ** 2)


def approximate_x_star(x, epsilon=1e-15, h=10 ** -5, i_f=1, i_g=1):
    k = 1
    while True:
        g_x = g(x, h, i_f, i_g)
        q = g(x + g_x, h, i_f, i_g) - g_x
        if abs(q) <= epsilon:
            return x, k
        z = x + g_x ** 2 / q
        delta_x = g_x * (g(z, h, i_f, i_g) - g_x) / q
        x -= delta_x
        abs_delta_x = abs(delta_x)
        if abs_delta_x <= epsilon:
            return x, k
        k += 1
        if abs_delta_x > 10 ** 8 or k > 1_000:
            return None, None


def get_range(i=1):
    r = 10
    expected_x_star = expect_x_star(i)
    if i == 3:
        return range(int(expected_x_star[0]) - r, int(expected_x_star[1]) + r)
    expected_x_star = int(expected_x_star)
    return range(expected_x_star - r, expected_x_star + r)


def get_result(epsilon=1e-15, h=10 ** -5, i_f=1, i_g=1):
    expected_x_star = expect_x_star(i_f)
    if i_f == 3:
        results_1 = []
        results_2 = []
        for x in get_range(i_f):
            approximated_x_star, k = approximate_x_star(x, epsilon, h, i_f, i_g)
            if approximated_x_star is not None:
                if f_2(approximated_x_star, h, i_f) > 0:
                    if abs(round(approximated_x_star) - expected_x_star[0]) <= epsilon:
                        results_1 += [(x, approximated_x_star, k)]
                    else:
                        results_2 += [(x, approximated_x_star, k)]
        results_1.sort(key=lambda t: (abs(t[1] - expected_x_star[0]), t[2]))
        results_2.sort(key=lambda t: (abs(t[1] - expected_x_star[1]), t[2]))
        return "x₀₁ = {}, minimum₁ = {}, iterations₁ = {}\nx₀₂ = {}, " \
               "minimum₂ = {}, iterations₂ = {}".format(results_1[0][0], results_1[0][1], results_1[0][2],
                                                        results_2[0][0], results_2[0][1], results_2[0][2])
    results = []
    for x in get_range(i_f):
        approximated_x_star, k = approximate_x_star(x, epsilon, h, i_f, i_g)
        if approximated_x_star is not None:
            if f_2(approximated_x_star, h, i_f) > 0:
                results += [(x, approximated_x_star, k)]
    results.sort(key=lambda t: (abs(t[1] - expected_x_star), t[2]))
    return "x₀ = {}, minimum = {}, iterations = {}".format(results[0][0], results[0][1], results[0][2])


def main():
    epsilon = addition_precision()
    print("epsilon = {}".format(epsilon))
    h = 10 ** -5
    print("h = {}".format(h))
    print()
    print("f₁, g₁")
    print(get_result(epsilon, h, 1, 1))
    print()
    print("f₁, g₂")
    print(get_result(epsilon, h, 1, 2))
    print("\n\n")
    print("f₂, g₁")
    print(get_result(epsilon, h, 2, 1))
    print()
    print("f₂, g₂")
    print(get_result(epsilon, h, 2, 2))
    print("\n\n")
    print("f₃, g₁")
    print(get_result(epsilon, h, 3, 1))
    print()
    print("f₃, g₂")
    print(get_result(epsilon, h, 3, 2))


if __name__ == "__main__":
    main()
