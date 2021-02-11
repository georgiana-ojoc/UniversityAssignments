def sum_variable_numbers(*numbers, **keyword_numbers):
    return sum(keyword_numbers.values())


def main():
    try:
        print(sum_variable_numbers(1, 2, c=3, d=4))
        print((lambda *numbers, **keyword_numbers: sum(keyword_numbers.values()))(1, 2, c=3, d=4))
    except TypeError:
        print("Enter numbers.")


if __name__ == '__main__':
    main()
