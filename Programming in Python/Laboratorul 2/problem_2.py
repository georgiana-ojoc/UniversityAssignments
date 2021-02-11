import math


def is_prime(number):
    if number < 2:
        return False
    if number % 2 == 0:
        return False
    return len([divisor for divisor in range(2, int(math.sqrt(number)) + 1, 2) if number % divisor == 0]) == 0


def get_prime_numbers(numbers):
    return [number for number in numbers if is_prime(number)]


def main():
    number = int(input("Enter the number of numbers: "))
    prime_numbers = get_prime_numbers(int(input("Enter a number: ")) for _ in range(number))
    print("The prime numbers from the list are: %s." % prime_numbers)


if __name__ == '__main__':
    main()
