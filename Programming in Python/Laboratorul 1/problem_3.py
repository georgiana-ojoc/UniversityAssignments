def compute_occurrences_number(substring, string):
    """
    :param substring: the string to be counted
    :param string: the string in which to count
    :return: the number of occurrences of the first string in the second string
    """
    # Rabin-Karp is more efficient.
    counter = 0
    for index in range(len(string) - len(substring) + 1):
        if string[index:index + len(substring)] == substring:
            counter += 1
    return counter


def main():
    """
    Reads pairs of strings from the console while both are not empty and prints the number of occurrences
    of the first string in the second string.
    """
    while True:
        substring = input("Enter the substring: ")
        string = input("Enter the string: ")
        print("The number of occurrences of \"%s\" in \"%s\" is %d." % (
            substring, string, compute_occurrences_number(substring, string)))
        if substring == "" and string == "":
            break


if __name__ == '__main__':
    main()
