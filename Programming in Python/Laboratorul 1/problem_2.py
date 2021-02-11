def compute_vowels_number(string):
    """
    :param string: the string to count vowels in
    :return: the number of vowels
    """
    return len([character for character in string.lower() if character in "aeiou"])


def main():
    """
    Reads strings from the console while they are not empty and prints their number of vowels.
    """
    while True:
        string = input("Enter a string: ")
        print("\"%s\" has %d vowels." % (string, compute_vowels_number(string)))
        if string == "":
            break


if __name__ == '__main__':
    main()
