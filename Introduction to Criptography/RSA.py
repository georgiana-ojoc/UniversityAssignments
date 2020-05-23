import sympy
import time
from sympy import *


def gcd(a, b):
    while a != 0:
        a, b = b % a, a
    return b


def extended_gcd(a, b):
    if a == 0:
        return b, 0, 1
    _gcd, x, y = extended_gcd(b % a, a)
    return _gcd, y - (b // a) * x, x


def modular_inverse(a, m):
    _gcd, x, y = extended_gcd(a, m)
    if _gcd != 1:
        raise Exception("Modular inverse of {} modulo {} does not exist.".format(a, m))
    else:
        return x % m


def prime_numbers(digits):
    seed = int(time.time() * 10 ** digits)
    p = sympy.nextprime(seed)
    q = sympy.nextprime(p)
    return p, q


def carmichael(p, q):  # the exponent of the multiplicative group of integers modulo n
    return (p - 1) * (q - 1) // gcd(p - 1, q - 1)


def public_exponent(_limit):
    return sympy.randprime(2, _limit)


def private_exponent(a, m):
    b = modular_inverse(a, m)
    if a == b:
        b += m
    return b


def transform_number(number, exponent, n):
    return pow(number, exponent, n)


def crt_decrypt_number(number, _private_exponent, p, q):
    first_exponent = _private_exponent % (p - 1)
    second_exponent = _private_exponent % (q - 1)
    _modular_inverse = modular_inverse(p, q)
    x = pow(number % p, first_exponent, p)
    y = pow(number % q, second_exponent, q)
    return x + p * (((y - x) * _modular_inverse) % q)


def continuous_fraction(a, b):
    q = [a // b]
    r = a % b
    while r != 0:
        a, b = b, r
        q.append(a // b)
        r = a % b
    return q


def convergents(q):
    a = []
    b = []
    for i in range(len(q)):
        if i == 0:
            _a = q[i]
            _b = 1
        elif i == 1:
            _a = q[i - 1] * q[i] + 1
            _b = q[i]
        else:
            _a = a[i - 1] * q[i] + a[i - 2]
            _b = b[i - 1] * q[i] + b[i - 2]
        a.append(_a)
        b.append(_b)
        yield _a, _b


def rsa():
    print("RSA:")
    p, q = prime_numbers(155)  # a binary integer of 512 ones will convert to base 10 integer with 155 digits
    n = p * q
    _carmichael = carmichael(p, q)
    e = public_exponent(_carmichael)
    d = private_exponent(e, _carmichael)
    print("Plaintext: A")
    encryption = transform_number(ord('A'), e, n)
    first = time.time()
    print("Normal decryption: {}".format(chr(transform_number(encryption, d, n))))
    second = time.time()
    print("Normal decryption elapsed time: {}".format(second - first))
    print("Decryption with CRT: {}".format(chr(crt_decrypt_number(encryption, d, p, q))))
    third = time.time()
    print("Decryption with CRT elapsed time: {}".format(third - second))


def wiener_attack():
    print("Wiener attack:")
    seed = int(time.time() * 10 ** 155)
    q = sympy.nextprime(seed)
    p = sympy.nextprime(q)
    while p >= 2 * q:
        q = sympy.nextprime(p)
        p = sympy.nextprime(q)
    n = p * q
    d = sympy.randprime(2, 1 << 32 - 1)
    _carmichael = carmichael(p, q)
    e = modular_inverse(d, _carmichael)
    fractions = continuous_fraction(e, n)
    _convergents = convergents(fractions)
    for _l, _d in _convergents:
        if _l == 0:
            continue
        _phi = (e * _d - 1) // _l
        x = symbols('x')
        _roots = solve(x ** 2 - (n - _phi + 1) * x + n)
        if len(_roots) == 2:
            p, q = _roots
            if p * q == n:
                print("Found p and q:")
                print("p = {}".format(int(p)))
                print("q = {}".format(int(q)))
                return
    print("Unable to find p and q.")


def main():
    rsa()
    wiener_attack()


if __name__ == '__main__':
    main()
