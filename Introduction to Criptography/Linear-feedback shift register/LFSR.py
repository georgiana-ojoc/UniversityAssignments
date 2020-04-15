from BlumBlumShub import BlumBlumShub
from Jacobi import Jacobi
from lempel_ziv_complexity import lempel_ziv_complexity
import math
import random
import time


class LinearFeedbackShiftRegister:
    def __init__(self, bits):
        print("Linear Feedback Shift Register pseudorandom number generator")
        self.bits = bits
        self.length = 229
        # connection polynomial: x ^ 229 + x ^ 64 + x ^ 63 + 1
        self.feedback = [0] * self.length
        self.feedback[0] = 1
        self.feedback[-64] = 1
        self.feedback[-63] = 1
        self.feedback[-1] = 1
        # initial state
        self.state = random.getrandbits(self.length)
        while self.state == 0:
            self.state = random.getrandbits(self.length)
        self.sequence = []
        self.zeros = 0

    def generate(self):
        # the most-significant bit
        for i in range(self.bits):
            bit = 0
            # XOR between correspondent bits
            for j in range(self.length):
                if self.feedback[j]:
                    bit ^= self.state >> j
            # AND to get the least-significant bit
            bit &= 1
            # pop the least-significant bit and the most-significant bit
            self.state = (self.state >> 1) | (bit << (self.length - 1))
            self.sequence.append(self.state & 1)
            if self.sequence[i] == 0:
                self.zeros += 1
        print()

    def compare(self):
        print("Number of 0: {} ({:.2%})".format(self.zeros, self.zeros * 1.0 / self.bits))
        ones = self.bits - self.zeros
        print("Number of 1: {} ({:.2%})".format(ones, ones * 1.0 / self.bits))
        print("Difference: {}".format(abs(self.zeros - ones)))
        print()

    def compression(self):
        print("Compression rate for this: {}".format(lempel_ziv_complexity(str(self.sequence))))
        print("Compression rate for random 0-1 string: {}"
              .format(lempel_ziv_complexity(''.join(random.choices(['0', '1'], k=self.bits)))))
        print("Compression rate for 11...1: {}".format(lempel_ziv_complexity("1" * self.bits)))
        print()

    def test(self):
        start = time.time()
        self.generate()
        end = time.time()
        self.compare()
        self.compression()
        return end - start


def main():
    generator = LinearFeedbackShiftRegister(2 ** 15)
    print("Running time:", generator.test(), "seconds")
    print()

    bbs = BlumBlumShub(2 ** 15)
    print("Running time:", bbs.test(), "seconds")
    print()

    j = Jacobi(2 ** 15)
    print("Running time:", j.test(), "seconds")
    print()


if __name__ == '__main__':
    main()
