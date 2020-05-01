import math
import random
import textwrap

hexadecimal_to_binary = {'0': '0000',
                         '1': '0001',
                         '2': '0010',
                         '3': '0011',
                         '4': '0100',
                         '5': '0101',
                         '6': '0110',
                         '7': '0111',
                         '8': '1000',
                         '9': '1001',
                         'A': '1010',
                         'B': '1011',
                         'C': '1100',
                         'D': '1101',
                         'E': '1110',
                         'F': '1111'}

binary_to_hexadecimal = {'0000': '0',
                         '0001': '1',
                         '0010': '2',
                         '0011': '3',
                         '0100': '4',
                         '0101': '5',
                         '0110': '6',
                         '0111': '7',
                         '1000': '8',
                         '1001': '9',
                         '1010': 'A',
                         '1011': 'B',
                         '1100': 'C',
                         '1101': 'D',
                         '1110': 'E',
                         '1111': 'F'}

# The IP contains 64 positions.
initial_permutation = [58, 50, 42, 34, 26, 18, 10, 2,
                       60, 52, 44, 36, 28, 20, 12, 4,
                       62, 54, 46, 38, 30, 22, 14, 6,
                       64, 56, 48, 40, 32, 24, 16, 8,
                       57, 49, 41, 33, 25, 17, 9, 1,
                       59, 51, 43, 35, 27, 19, 11, 3,
                       61, 53, 45, 37, 29, 21, 13, 5,
                       63, 55, 47, 39, 31, 23, 15, 7]

# The PC-1 contains 56 positions, meaning all positions between 1 and 64 without the parity check ones
# (8, 16, 24, 32, 40, 48, 56, 64).
permuted_choice_1 = [57, 49, 41, 33, 25, 17, 9,
                     1, 58, 50, 42, 34, 26, 18,
                     10, 2, 59, 51, 43, 35, 27,
                     19, 11, 3, 60, 52, 44, 36,
                     63, 55, 47, 39, 31, 23, 15,
                     7, 62, 54, 46, 38, 30, 22,
                     14, 6, 61, 53, 45, 37, 29,
                     21, 13, 5, 28, 20, 12, 4]

# The PC-2 contains 48 positions, meaning all positions between 1 and 56 without
# 9, 18, 22, 25, 35, 38, 43, 54.
permuted_choice_2 = [14, 17, 11, 24, 1, 5,
                     3, 28, 15, 6, 21, 10,
                     23, 19, 12, 4, 26, 8,
                     16, 7, 27, 20, 13, 2,
                     41, 52, 31, 37, 47, 55,
                     30, 40, 51, 45, 33, 48,
                     44, 49, 39, 56, 34, 53,
                     46, 42, 50, 36, 29, 32]

substitution_boxes = [[[14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7],
                       [0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8],
                       [4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0],
                       [15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13]],

                      [[15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10],
                       [3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5],
                       [0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15],
                       [13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9]],

                      [[10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8],
                       [13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1],
                       [13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7],
                       [1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12]],

                      [[7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15],
                       [13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9],
                       [10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4],
                       [3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14]],

                      [[2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9],
                       [14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6],
                       [4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14],
                       [11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3]],

                      [[12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11],
                       [10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8],
                       [9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6],
                       [4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13]],

                      [[4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1],
                       [13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6],
                       [1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2],
                       [6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12]],

                      [[13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7],
                       [1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2],
                       [7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8],
                       [2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11]]]

permutation = [16, 7, 20, 21,
               29, 12, 28, 17,
               1, 15, 23, 26,
               5, 18, 31, 10,
               2, 8, 24, 14,
               32, 27, 3, 9,
               19, 13, 30, 6,
               22, 11, 4, 25]


def get_hexadecimal_to_binary(hexadecimals):
    hexadecimals = hexadecimals.upper()
    bits = ""
    for hexadecimal in hexadecimals:
        bits += hexadecimal_to_binary[hexadecimal]
    return bits


def get_binary_to_hexadecimal(bits):
    if len(bits) % 4 != 0:
        return ""
    hexadecimals = ""
    for index in range(0, len(bits), 4):
        hexadecimals += binary_to_hexadecimal[bits[index: index + 4]]
    return hexadecimals


def get_permutation(positions, bits):
    transformed_bits = ""
    for positions in positions:
        transformed_bits += bits[positions - 1]
    return transformed_bits


def get_halves(bits, half_length):
    return bits[:half_length], bits[half_length:]


def get_shifts():
    shifts = []
    for index in range(16):
        if index + 1 in [1, 2, 9, 16]:
            shifts.append(1)
        else:
            shifts.append(2)
    return shifts


def get_circular_left_shift(bits, shifted_bits):
    return bits[shifted_bits:] + bits[:shifted_bits]


def get_round_keys(bits):
    round_keys = []
    # Discard the parity check bits and permute the remaining ones.
    round_key = get_permutation(permuted_choice_1, bits)
    # Split the remaining bits in two 28-bit halves.
    left, right = get_halves(round_key, len(round_key) // 2)
    # Cyclic left shift one bit if position is in {1, 2, 9, 16} and shift two bits otherwise.
    shifts = get_shifts()
    for index in range(16):
        new_left = get_circular_left_shift(left, shifts[index])
        new_right = get_circular_left_shift(right, shifts[index])
        # Discard 8 bits and permute the remaining ones.
        round_key = get_permutation(permuted_choice_2, new_left + new_right)
        round_keys.append(round_key)
        left = new_left
        right = new_right
    return round_keys


def get_xor(left, right):
    result = ""
    for index in range(len(left)):
        if left[index] == right[index]:
            result += '0'
        else:
            result += '1'
    return result


# The E contains 42 positions, meaning all positions between 1 and 32 and 16 positions appear twice.
def get_expansion():
    expansion = [32]
    index = 1
    while len(expansion) < 47:
        expansion.append(index)
        index += 1
        if index > 2 and (index - 2) % 4 == 0:
            expansion.append(index - 2)
            expansion.append(index - 1)
    expansion.append(1)
    return expansion


def get_split_in_six_bits(bits):
    return textwrap.wrap(bits, 6)


# Convert to decimal the binary number which includes the first and the last bit from the six-bit string.
# Results a number between 0 and 3.
def get_row(bits):
    return int(bits[0]) * 2 + int(bits[-1])


# Convert to decimal the binary number which includes the 4 middle bits from the six-bit string.
# Results a number between 0 and 15.
def get_column(bits):
    column = 0
    for index in range(1, 5):
        column += int(bits[index]) * (2 ** (4 - index))
    return column


# Get the substitution value from the correspondent box, row and column.
def get_substitution(index, bits):
    return bin(substitution_boxes[index][get_row(bits)][get_column(bits)])[2:].zfill(4)


def get_round(bits, key):
    result = ""
    # Expand the 32-bit right half to 48-bit because the key has 48-bit.
    bits = get_permutation(get_expansion(), bits)
    xor = get_xor(bits, key)
    # Split the 48-bit permutation in eight six-bit strings.
    six_bits_list = get_split_in_six_bits(xor)
    for index in range(8):
        # Append eight four-bit strings to the new right half.
        result += get_substitution(index, six_bits_list[index])
    return get_permutation(permutation, result)


# The IP^-1 is the inverse permutation of the IP.
def get_final_permutation():
    return list(initial_permutation.index(index + 1) + 1 for index in range(len(initial_permutation)))


def get_transformation(algorithm, initial_text, key):
    if len(initial_text) != 16 or len(key) != 16:
        return ""
    initial_binary_text = get_hexadecimal_to_binary(initial_text)
    initial_binary_text = get_permutation(initial_permutation, initial_binary_text)
    # Split the initial permuted text in two 32-bit halves.
    left, right = get_halves(initial_binary_text, len(initial_binary_text) // 2)
    binary_key = get_hexadecimal_to_binary(key)
    # Create the 16 round keys.
    round_keys = get_round_keys(binary_key)
    if algorithm == "decryption":
        round_keys.reverse()
    for index in range(16):
        # Create the new right half.
        new_right = get_xor(left, get_round(right, round_keys[index]))
        left = right
        right = new_right
    return get_binary_to_hexadecimal(get_permutation(get_final_permutation(), right + left))


def get_random_plaintext():
    return "".join(random.choices(list(hexadecimal_to_binary.keys()), k=16))


# Returns a number between 1 and 255 which is not a power of 2.
def get_random_byte():
    while True:
        number = random.randint(1, 2 ** 8 - 1)
        if not math.log2(number).is_integer():
            return number


def get_key(byte):
    return get_binary_to_hexadecimal(bin(byte ** 8)[2:].zfill(8 * 8))


def get_encryptions(plaintext):
    encryptions = []
    for byte in range(2 ** 8):
        encryptions.append(get_transformation("encryption", plaintext, get_key(byte)))
    return encryptions


def get_decryptions(ciphertext):
    decryptions = []
    for byte in range(2 ** 8):
        decryptions.append(get_transformation("decryption", ciphertext, get_key(byte)))
    return decryptions


# Returns all common middle ciphertexts.
def get_hits(encryptions, decryptions):
    return list(hit for hit in encryptions if hit in decryptions)


def get_possible_keys(transformations, hit):
    return list(get_key(byte) for byte in range(len(transformations))
                if transformations[byte] == hit)


def meet_in_the_middle():
    print("\nMeet-in-the-middle:")
    first_key = get_key(get_random_byte())
    second_key = get_key(get_random_byte())
    print("First key:", first_key)
    print("Second key:", second_key)
    plaintext = []
    first_ciphertext = []
    second_ciphertext = []
    for index in range(2):
        plaintext.append(get_random_plaintext())
        first_ciphertext.append(get_transformation("encryption", plaintext[index], first_key))
        second_ciphertext.append(get_transformation("encryption", first_ciphertext[index], second_key))
    print("First set:")
    print("Plaintext:", plaintext[0])
    print("First ciphertext:", first_ciphertext[0])
    print("Second ciphertext:", second_ciphertext[0])
    encryptions = get_encryptions(plaintext[0])
    print("2^6 encryptions:", encryptions)
    decryptions = get_decryptions(second_ciphertext[0])
    print("2^8 decryptions:", decryptions)
    hits = get_hits(encryptions, decryptions)
    print("Hits:")
    for hit in hits:
        print("Text:", hit)
        print("Possible key pairs:")
        first_keys = get_possible_keys(encryptions, hit)
        second_keys = get_possible_keys(decryptions, hit)
        for first_key in first_keys:
            for second_key in second_keys:
                print(first_key, second_key)
        print("Second set:")
        print("Plaintext:", plaintext[1])
        print("First ciphertext:", first_ciphertext[1])
        print("Second ciphertext:", second_ciphertext[1])
        print("Valid key pairs:")
        for first_key in first_keys:
            for second_key in second_keys:
                # Check the double encryption with the found keys.
                if get_transformation("encryption", get_transformation("encryption", plaintext[1], first_key),
                                      second_key) == second_ciphertext[1]:
                    print(first_key, second_key)


def main():
    # The plaintext and the key contain 16 hexadecimal digits.
    plaintext = "0123456789ABCDEF"
    print("Plaintext:", plaintext)
    key = "133457799BBCDFF1"
    print("Key:", key)
    # The decryption follows the same algorithm as the encryption but uses the round keys in reverse order.
    ciphertext = get_transformation("encryption", plaintext, key)
    print("Encryption:", ciphertext)
    print("Decryption:", get_transformation("decryption", ciphertext, key))
    meet_in_the_middle()


if __name__ == '__main__':
    main()
