import collections
import sys


def get_alphabet():
    result = []
    for index in range(ord('A'), ord('Z') + 1):
        result.append(chr(index))
    return result


alphabet = get_alphabet()


def get_english_frequencies():
    file = open("frequencies.txt")
    result = {}
    for line in file:
        (key, value) = line.split()
        result[key] = float(value) / 100
    file.close()
    return result


english_frequencies = get_english_frequencies()


def get_formatted_string(string):
    length = len(string)
    result = ""
    for index in range(length):
        if not string[index].isalpha():
            continue
        if string[index].islower():
            result += string[index].upper()
        elif string[index].isupper():
            result += string[index]
        index += 1
    return result


def get_encrypted_string(string, key):
    string_length = len(string)
    key_length = len(key)
    alphabet_length = len(alphabet)
    result = ""
    for index in range(string_length):
        result += alphabet[(ord(string[index]) + ord(key[index % key_length])) % alphabet_length]
    return result


def get_substring(string, order):
    length = len(string)
    result = ""
    for index in range(0, length, order):
        result += string[index]
    return result


def get_index_of_coincidence(string):
    length = len(string)
    if length < 2:
        return 0
    result = 0.0
    occurrences = collections.Counter(string)
    for letter in alphabet:
        result += occurrences[letter] * (occurrences[letter] - 1)
    result /= length * (length - 1)
    return result


def get_average_index_of_coincidence(string, length):
    result = 0
    for index in range(length):
        substring = get_substring(string[index:], length)
        ind_of_co = get_index_of_coincidence(substring)
        """
        if not 0.061 < ind_of_co < 0.073:
            return 0
        """
        result += ind_of_co
    result /= length
    return result


def get_key_length(string):
    alphabet_length = len(alphabet)
    poss_key_len = 0
    poss_avg_ind_of_co = 0
    for key_len in range(1, alphabet_length + 1):
        avg_ind_of_co = get_average_index_of_coincidence(string, key_len)
        if 0.061 < avg_ind_of_co < 0.073:
            poss_key_len = key_len
            poss_avg_ind_of_co = avg_ind_of_co
            break
    else:
        str_len = len(string)
        for key_len in range(alphabet_length + 1, str_len + 1):
            if 0.061 < get_average_index_of_coincidence(string, key_len) < 0.073:
                return key_len

    candidates = {poss_avg_ind_of_co: poss_key_len}
    for key_len in range(poss_key_len + 1, poss_key_len * 2):
        avg_ind_of_co = get_average_index_of_coincidence(string, key_len)
        if 0.061 < avg_ind_of_co < 0.073:
            candidates[avg_ind_of_co] = key_len

    for key_len in range(poss_key_len * 2, alphabet_length + 1, poss_key_len):
        avg_ind_of_co = get_average_index_of_coincidence(string, key_len)
        if 0.061 < avg_ind_of_co < 0.073:
            candidates[avg_ind_of_co] = key_len

    if len(candidates) == 1:
        return poss_key_len

    print("Possible (key length, average index of coincidence):\n")
    sorted_candidates = {}
    sorted_keys = sorted(candidates, reverse=True)
    for index in sorted_keys:
        sorted_candidates[index] = candidates[index]
    for entry in sorted_candidates:
        print("(%d, %.4f)" % (sorted_candidates[entry], entry))
    print()

    list_keys = list(sorted_candidates.keys())
    if len(list_keys) < 3:
        return min(sorted_candidates[list_keys[0]], sorted_candidates[list_keys[1]])
    return min(sorted_candidates[list_keys[0]], sorted_candidates[list_keys[1]], sorted_candidates[list_keys[2]])


def get_mutual_index_of_coincidence(string):
    length = len(string)
    if length == 0:
        return 0
    result = 0.0
    occurrences = collections.Counter(string)
    for letter in alphabet:
        result += english_frequencies[letter] * occurrences[letter]
    result /= length
    return result


def get_shifted_string(string, shift):
    alphabet_length = len(alphabet)
    result = ""
    for letter in string:
        result += alphabet[(ord(letter) + ord(alphabet[shift])) % alphabet_length]
    return result


def get_key(string, key_length):
    alphabet_length = len(alphabet)
    result = ""
    print("Possible (key letter, mutual index of coincidence):\n")
    for index in range(key_length):
        poss_letter = ''
        max_mut_ind_of_co = 0
        for shift in range(alphabet_length):
            substring = get_substring(string[index:], key_length)
            shifted_substring = get_shifted_string(substring, shift)
            mut_ind_of_co = get_mutual_index_of_coincidence(shifted_substring)
            if mut_ind_of_co > max_mut_ind_of_co:
                poss_letter = alphabet[(alphabet_length - shift) % alphabet_length]
                max_mut_ind_of_co = mut_ind_of_co
        result += poss_letter
        print("(%c, %.4f)" % (poss_letter, max_mut_ind_of_co))
    print()
    return result


def get_decrypted_string(string, key):
    string_length = len(string)
    key_length = len(key)
    alphabet_length = len(alphabet)
    result = ""
    for index in range(string_length):
        result += alphabet[(ord(string[index]) - ord(key[index % key_length])) % alphabet_length]
    return result


def main():
    if len(sys.argv) != 3:
        raise ValueError('Specify file and key.')

    file = open(sys.argv[1])
    plain_text = file.read()
    file.close()
    print("Plain text:\n")
    print(plain_text)
    print()

    key = get_formatted_string(sys.argv[2])
    print("Key: %s\n" % key)

    formatted_text = get_formatted_string(plain_text)
    print("Formatted text:\n")
    print(formatted_text)
    print("%s%s" % (key * int(len(formatted_text) / len(key)), key[:(len(formatted_text) % len(key))]))
    print()

    encrypted_text = get_encrypted_string(formatted_text, key)
    print("Encrypted text:\n")
    print(encrypted_text)
    print()

    decrypted_text = get_decrypted_string(encrypted_text, key)
    print("Decrypted text:\n")
    print(decrypted_text)
    print()

    possible_key_length = get_key_length(encrypted_text)
    print("Possible key length: %d\n" % possible_key_length)

    possible_key = get_key(encrypted_text, possible_key_length)
    print("Possible key: %s\n" % possible_key)

    possible_decrypted_text = get_decrypted_string(encrypted_text, possible_key)
    print("Possible decrypted text:\n")
    print(possible_decrypted_text)
    print()

    if formatted_text == possible_decrypted_text:
        print("Correct")


if __name__ == '__main__':
    main()
