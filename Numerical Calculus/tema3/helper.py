def addition_precision():
    m = 1
    while 1.0 + 10 ** -m != 1.0:
        m += 1
    return 10 ** (1 - m)


def get_option():
    while True:
        try:
            option = int(input("Enter 1 to read A as list of lists or 2 to read A as dictionary of keys: "))
        except ValueError:
            print("option must be integer.")
        else:
            if option in (1, 2):
                print("option = {}".format(option))
                return option
            print("option must be 1 or 2.")


def read_tridiagonal_matrix(name):
    with open(name) as file:
        lines = file.read().split('\n')
    n = int(lines[0])
    p = int(lines[1])
    q = int(lines[2])
    a = []
    for line in lines[4:n + 4]:
        a += [float(line)]
    b = []
    for line in lines[n + 5:2 * n - p + 5]:
        b += [float(line)]
    c = []
    for line in lines[2 * n - p + 6:]:
        c += [float(line)]
    return p, q, a, b, c
