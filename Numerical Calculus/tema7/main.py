import numpy


def addition_precision():
    m = 1
    while 1.0 + 10 ** -m != 1.0:
        m += 1
    return 10 ** (1 - m)


def load_array(name):
    return numpy.loadtxt(name, delimiter='\n')


def round_array(array):
    return numpy.unique(numpy.around(array, 3))


def format_array(array):
    return numpy.array2string(array, precision=3, separator=' ',
                              floatmode="fixed").replace('[', '').replace(']', '').lstrip()


def save_array(index, array):
    return numpy.savetxt("approximated_x_{}.txt".format(index), array, fmt="%.3f",
                         delimiter='\n')


def compute_p_1_coefficients(a):
    power = a.size - 1
    result = []
    for a_i in a[:-1]:
        result += [a_i * power]
        power -= 1
    return numpy.array(result)


def compute_degree(a):
    index = a.size - 1
    while index >= 0 and a[index] == 0:
        index -= 1
    return index


def divide(a_1, a_2):
    a_1_degree = compute_degree(a_1)
    a_2_degree = compute_degree(a_2)
    difference = a_1_degree - a_2_degree
    b_2 = numpy.hstack((a_2, numpy.zeros(difference)))
    q = numpy.zeros(difference + 1)
    while difference >= 0:
        c_2 = numpy.roll(b_2, difference)
        q[difference] = a_1[a_1_degree] / c_2[a_1_degree]
        c_2 *= q[difference]
        a_1 -= c_2
        a_1_degree = compute_degree(a_1)
        a_2_degree = compute_degree(a_2)
        difference = a_1_degree - a_2_degree
    while a_1.size > 1 and a_1[-1] == 0:
        a_1 = a_1[:-1]
    return q, a_1


def compute_gcd(a_1, a_2):
    while not (a_2 == numpy.array([0])).all():
        q, r = divide(a_1, a_2)
        a_1 = numpy.copy(a_2)
        a_2 = numpy.copy(r)
    return a_1


def simplify_p(a):
    a_1 = compute_p_1_coefficients(a)
    a = a[::-1]
    a_1 = a_1[::-1]
    gcd = compute_gcd(numpy.copy(a), a_1)
    q, r = divide(a, gcd)
    return q[::-1]


def compute_r(a):
    abs_a_0 = numpy.abs(a[0])
    r = (abs_a_0 + numpy.max(a)) / abs_a_0
    if r < 0:
        return -r
    return r


def compute_p(a, x):
    result = a[0]
    for a_i in a[1:]:
        result = result * x + a_i
    return result


def compute_p_1(a, x):
    power = a.size - 1
    result = a[0] * power
    power -= 1
    for a_i in a[1:-1]:
        result = result * x + a_i * power
        power -= 1
    return result


def compute_p_2(a, x):
    power = a.size - 1
    result = a[0] * power * (power - 1)
    power -= 1
    for a_i in a[1:-2]:
        result = result * x + a_i * power * (power - 1)
        power -= 1
    return result


def compute_bonus_p(a, c, d):
    p = -2 * c
    q = c ** 2 + d ** 2
    b_i_2 = a[0]
    b_i_1 = a[1] - p * b_i_2
    for a_i in a[2:]:
        b_i = a_i - p * b_i_1 - q * b_i_2
        b_i_2, b_i_1 = b_i_1, b_i
    return b_i_2 * c + b_i_1 + p * b_i_2, b_i_2 * d


def compute_delta(a, x):
    p_of_x = compute_p(a, x)
    p_1_of_x = compute_p_1(a, x)
    p_2_of_x = compute_p_2(a, x)
    c = p_of_x ** 2 * p_2_of_x / (p_1_of_x ** 3)
    return p_of_x / p_1_of_x + c / 2.0


def approximate_x(epsilon, a, x):
    max_delta = 10 ** 8
    max_i = 1_000
    i = 0
    while True:
        if numpy.abs(compute_p_1(a, x)) <= epsilon:
            return None
        delta = compute_delta(a, x)
        x -= delta
        i += 1
        abs_delta = numpy.abs(delta)
        if abs_delta <= epsilon:
            return x
        if abs_delta > max_delta or i == max_i:
            return None


def exists(epsilon, array, value):
    for item in array:
        if numpy.abs(item - value) <= epsilon:
            return True
    return False


def solve(epsilon, index):
    a, expected_x = load_array("a_{}.txt".format(index)), load_array("expected_x_{}.txt".format(index))
    if a.size == 0:
        return ["a is empty"]
    if a[0] == 0:
        return ["a[0] is 0"]
    messages = ["Polynomial {}".format(index)]
    messages += ["Coefficients: {}".format(format_array(a))]
    a = simplify_p(a)
    messages += ["Simplified coefficients: {}".format(format_array(a))]
    r = compute_r(a)
    messages += ["[-R, R] = [%.3f, %.3f]" % (-r, r)]
    step = 0.5
    x_0 = -r
    approximated_x = []
    while x_0 <= r:
        x = approximate_x(epsilon, a, x_0)
        if x is not None and not exists(epsilon, approximated_x, x):
            approximated_x += [x]
        x_0 += step
    approximated_x = round_array(approximated_x)
    messages += ["Expected x: {}".format(format_array(expected_x))]
    messages += ["Approximated x: {}".format(format_array(approximated_x))]
    save_array(index, approximated_x)
    return messages


def print_messages(messages):
    for message in messages:
        print(message)
    print()


def main():
    print(divide(numpy.array([-42.0, 0.0, -12.0, 1.0]), numpy.array([-3, 1])))
    print(compute_gcd(numpy.array([6.0, 7.0, 1.0]), numpy.array([-6.0, -5.0, 1.0])))
    epsilon = addition_precision()
    print_messages(solve(epsilon, 1))
    print_messages(solve(epsilon, 2))
    print_messages(solve(epsilon, 3))
    print_messages(solve(epsilon, 4))


if __name__ == "__main__":
    main()
