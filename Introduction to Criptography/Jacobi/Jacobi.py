from lempel_ziv_complexity import lempel_ziv_complexity
from nistrng import *
import numpy
import random
import sympy
import time


def get_prime_number(digits):
    number = int(time.time() * 10 ** digits)
    prime_number = sympy.nextprime(number)
    while prime_number % 4 != 3:
        prime_number = sympy.nextprime(prime_number)
    return prime_number


def get_jacobi_symbol(a, n):
    assert (n > a > 0 and n % 2 == 1)
    s = 1
    while a != 0:
        while a % 2 == 0:
            a //= 2
            r = n % 8
            if r == 3 or r == 5:
                s = -s
        a, n = n, a
        if a % 4 == n % 4 == 3:
            s = -s
        a %= n
    if n == 1:
        return s
    else:
        return 0


class Jacobi:
    def __init__(self, length):
        print("Jacobi pseudorandom number generator")
        self.precision = 200  # a binary integer of 512 ones will convert to base 10 integer with 155 digits
        self.length = length
        self.seed = get_prime_number(self.precision)
        self.p = get_prime_number(self.precision)
        self.q = get_prime_number(self.precision)
        self.n = self.p * self.q
        self.symbol = []
        self.digit = []
        self.zeros = 0

    def generate(self):
        for i in range(self.length):
            self.symbol.append(get_jacobi_symbol(self.seed + i, self.n))
            assert self.symbol[i] != 0
            if self.symbol[i] == -1:
                self.digit.append(0)
            else:
                self.digit.append(1)
            if self.digit[i] == 0:
                self.zeros += 1
        print()

    def compare(self):
        print("Number of 0: {} ({:.2%})".format(self.zeros, self.zeros * 1.0 / self.length))
        ones = self.length - self.zeros
        print("Number of 1: {} ({:.2%})".format(ones, ones * 1.0 / self.length))
        print("Difference: {}".format(abs(self.zeros - ones)))
        print()

    def compression(self):
        print("Compression rate for this: {}".format(lempel_ziv_complexity(str(self.digit))))
        print("Compression rate for random 0-1 string: {}"
              .format(lempel_ziv_complexity(''.join(random.choices(['0', '1'], k=self.length)))))
        print("Compression rate for 11...1: {}".format(lempel_ziv_complexity("1" * self.length)))
        print()

    def nist(self):
        copy = numpy.reshape(self.digit, (int(numpy.ceil(self.length / 32)), 32))
        binary_copy = pack_sequence(copy)
        eligible_battery = check_eligibility_all_battery(binary_copy, SP800_22R1A_BATTERY)
        print("Eligible test from NIST-SP800-22r1a:")
        for name in eligible_battery.keys():
            print("- " + name)
        results = run_all_battery(binary_copy, eligible_battery, False)
        print("Test results:")
        for result, elapsed_time in results:
            if result.passed:
                print("- PASSED - score: "
                      + str(numpy.round(result.score, 3)) + " - " + result.name + " - elapsed time: "
                      + str(elapsed_time) + " ms")
            else:
                print("- FAILED - score: "
                      + str(numpy.round(result.score, 3)) + " - " + result.name + " - elapsed time: "
                      + str(elapsed_time) + " ms")

    def test(self):
        start = time.time()
        self.generate()
        end = time.time()
        self.compare()
        self.compression()
        return end - start


def main():
    generator = Jacobi(2 ** 16)
    print("Running time:", generator.test(), "seconds")
    print()
    generator.nist()


if __name__ == '__main__':
    main()
