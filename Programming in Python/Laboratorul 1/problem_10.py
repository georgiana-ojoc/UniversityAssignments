def get_words_number(string):
    """
    :param string: the string to count words in
    :return: the number of words
    """
    return len(string.split())


def main():
    """
    Reads strings from the console while they are not empty and prints their number of words.
    """
    while True:
        string = input("Enter a string: ")
        print("\"%s\" contains %d words." % (string, get_words_number(string)))
        if string == "":
            break


if __name__ == '__main__':
    main()
