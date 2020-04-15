import random
import string


def create_plaintext(length):
    return ''.join(random.choices(string.ascii_uppercase, k=length))


class RC4:
    def __init__(self):
        self.length = 16
        self.i = 0
        self.j = 0
        self.K = []
        self.S = []
        self.keystream = []

    def initialize(self):
        self.__init__()
        for i in range(self.length):
            self.K.append(random.getrandbits(8))
        for i in range(256):
            self.S.append(i)
        j = 0
        for i in range(256):
            j = (j + self.S[i] + self.K[i % self.length]) % 256
            self.S[i], self.S[j] = self.S[j], self.S[i]

    def transition(self):
        self.i = (self.i + 1) % 256
        self.j = (self.j + self.S[self.i]) % 256
        self.S[self.i], self.S[self.j] = self.S[self.j], self.S[self.i]
        self.keystream.append(self.S[(self.S[self.i] + self.S[self.j]) % 256])

    def create_keystream(self, count):
        self.initialize()
        for i in range(count):
            self.transition()

    def encrypt_plaintext(self, plaintext):
        self.initialize()
        cryptotext = ""
        for i in range(len(plaintext)):
            self.transition()
            cryptotext += chr(ord(plaintext[i]) ^ self.keystream[i])
        return cryptotext

    def decrypt_cryptotext(self, cryptotext):
        decrypted_text = ""
        for i in range(len(cryptotext)):
            decrypted_text += chr(ord(cryptotext[i]) ^ self.keystream[i])
        return decrypted_text

    def test_randomness(self):
        self.create_keystream(2 ** 16)
        zeros = 0
        binary_keystream = []
        for key in self.keystream:
            binary_keystream.append(key & 1)
            if key & 1 == 0:
                zeros = zeros + 1

        print("Number of 0: {} ({:.2%})".format(zeros, zeros * 1.0 / (2 ** 16)))
        ones = 2 ** 16 - zeros
        print("Number of 1: {} ({:.2%})".format(ones, ones * 1.0 / (2 ** 16)))
        print("Difference: {}".format(abs(zeros - ones)))
        print()

    def test_encryption(self):
        plaintext = create_plaintext(32)
        print("Plaintext:", plaintext)
        cryptotext = self.encrypt_plaintext(plaintext)
        print("Cryptotext:", cryptotext)
        decrypted_text = self.decrypt_cryptotext(cryptotext)
        print("Decrypted text:", decrypted_text)
        print()

    def test_zero_bias(self):
        zero_on_two = [0] * 255
        count = 2 ** 17
        for i in range(count):
            self.create_keystream(255)
            for j in range(255):
                if self.keystream[j] == 0:
                    zero_on_two[j] = zero_on_two[j] + 1
        print("Absolute difference between 1 / 128 and probability of zero on second byte:",
              abs(1.0 / 128 - zero_on_two[1] / count))
        for j in range(2, 255):
            print("Byte", j + 1, zero_on_two[j] / (2 ** 16) - 1.0 / 256,
                  (abs(zero_on_two[j] / count - 1.0 / 256)) * (256 ** 2))


def main():
    stream_cipher = RC4()
    stream_cipher.test_randomness()
    stream_cipher.test_encryption()
    stream_cipher.test_zero_bias()


if __name__ == '__main__':
    main()
