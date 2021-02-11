def get_fibonacci_sequence(number):
    sequence = list()
    if number < 1:
        return sequence
    sequence += [0]
    if number == 1:
        return sequence
    sequence += [1]
    for index in range(2, number):
        sequence += [sequence[index - 2] + sequence[index - 1]]
    return sequence


def main():
    number = int(input("Enter the number of numbers from the Fibonacci sequence: "))
    print("The first %d numbers from the Fibonacci sequence are: %s." % (number, get_fibonacci_sequence(number)))


if __name__ == '__main__':
    main()
