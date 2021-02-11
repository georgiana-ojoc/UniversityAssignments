import math


def process_item(number):
    prime = number + 1
    while True:
        if prime == 2:
            return 2
        if prime % 2 == 0:
            prime += 1
        else:
            divisor = 3
            while divisor <= int(math.sqrt(prime)):
                if prime % divisor == 0:
                    prime += 1
                    break
                divisor += 1
            else:
                return prime


def main():
    try:
        number = int(input("Enter number: "))
    except ValueError:
        print("Enter numbers.")
    else:
        print("Least prime number greater than {}: {}".format(number, process_item(number)))


if __name__ == '__main__':
    main()
