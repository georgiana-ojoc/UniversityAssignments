def compute_palindromes(numbers):
    palindromes = [number for number in numbers if str(number) == str(number)[::-1]]
    if len(palindromes) == 0:
        return 0, []
    return len(palindromes), max(palindromes)


def main():
    print(compute_palindromes([]))
    print(compute_palindromes([10]))
    print(compute_palindromes([1, 25, 121, 400, 111]))


if __name__ == '__main__':
    main()
