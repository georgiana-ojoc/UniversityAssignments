def get_most_common_letter(string):
    """
    :param string: the string to be traversed
    :return: the most common letter
    """
    if string == "":
        return None
    string = string.lower()
    frequencies = {}
    for character in string:
        if character in frequencies:
            frequencies[character] += 1
        else:
            frequencies[character] = 1
    return sorted(frequencies.items(), key=lambda value: value[1], reverse=True)[0][0]


def main():
    """
    Reads strings from the console while they are not empty and prints their most common letter.
    """
    while True:
        string = input("Enter a string: ")
        print("The most common letter from \"%s\" is %s." % (string, repr(get_most_common_letter(string))))
        if string == "":
            break


if __name__ == '__main__':
    main()
