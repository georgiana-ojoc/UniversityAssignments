def is_palindrome(number):
    """
    :param number: the number to be checked
    :return: True if the number is palindrome, False otherwise
    """
    if number < 1:
        return None
    string = str(number)
    return string == string[::-1]


def main():
    """
    Reads numbers from the console while they are bigger than 0 and checks if they are palindrome.
    """
    while True:
        number = int(input("Enter a number: "))
        result = is_palindrome(number)
        if result is None:
            print("The number is invalid.")
            break
        elif result is True:
            print("The number is palindrome.")
        else:
            print("The number is not palindrome.")


if __name__ == '__main__':
    main()
