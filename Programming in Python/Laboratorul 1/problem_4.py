def convert_writing_style(string):
    """
    :param string: the string to be transformed
    :return: the string converted from UpperCamelCase to lowercase_with_underscores
    """
    new_string = ""
    for character in string:
        if character.isupper():
            new_string += "_" + character.lower()
        else:
            new_string += character
    return new_string[1:]


def main():
    """
    Reads strings from the console while they are not empty and prints their conversion
    from UpperCamelCase to lowercase_with_underscores.
    """
    while True:
        string = input("Enter a string: ")
        print("\"%s\" becomes \"%s\"." % (string, convert_writing_style(string)))
        if string == "":
            break


if __name__ == '__main__':
    main()
