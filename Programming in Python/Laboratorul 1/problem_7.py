def get_first_number(string):
    """
    :param string: the string to extract from
    :return: the first number found
    """
    index = 0
    while index < len(string) and not string[index].isdigit():
        index += 1
    if index >= len(string):
        return None
    number = string[index]
    index += 1
    while index < len(string) and string[index].isdigit():
        number += string[index]
        index += 1
    return int(number)


def main():
    """
    Reads strings from the console while they are not empty and prints the first number that is found.
    """
    while True:
        string = input("Enter a string: ")
        result = get_first_number(string)
        if result is None:
            print("The first number from \"%s\" is %s." % (string, str(None)))
            if string == "":
                break
        else:
            print("The first number from \"%s\" is %d." % (string, get_first_number(string)))


if __name__ == '__main__':
    main()