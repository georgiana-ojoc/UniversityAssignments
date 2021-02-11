def compute_greatest_common_divisor(first, second):
    """
    :param first: the first number
    :param second: the second number
    :return: the greatest common divisor using the euclidean algorithm
    """
    if first == 0 and second == 0:
        return None
    while second:
        first, second = second, first % second
    return first


def compute_extended_greatest_common_divisor(numbers):
    """
    :param numbers: the numbers to compute on
    :return: the greatest common divisor of the numbers
    """
    if len(numbers) < 2:
        return None
    result = compute_greatest_common_divisor(numbers[0], numbers[1])
    for number in numbers[2:]:
        result = compute_greatest_common_divisor(result, number)
    return result


def read_numbers():
    """
    :return: the numbers read from the console
    """
    size = int(input("Enter the number of numbers: "))
    numbers = list()
    for index in range(size):
        numbers.append(int(input("Enter a number: ")))
    return numbers


def main():
    """
    Reads a number of integers from the console and prints their greatest common divisor.
    """
    numbers = read_numbers()
    print("The extended greatest common divisor is %s." % str(compute_extended_greatest_common_divisor(numbers)))


if __name__ == '__main__':
    main()
