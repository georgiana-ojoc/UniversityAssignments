def compute_set_bits_number(number):
    """
    :param number: the number to count set bits in
    :return: the number of set bits
    """
    return bin(number).count("1")


def main():
    """
    Reads numbers from the console while they are nonzero and prints their number of set bits.
    """
    while True:
        number = int(input("Enter a number: "))
        print("%d contains %d set bits." % (number, compute_set_bits_number(number)))
        if number == 0:
            break


if __name__ == '__main__':
    main()
