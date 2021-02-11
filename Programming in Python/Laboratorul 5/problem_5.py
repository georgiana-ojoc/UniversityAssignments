def numbers(items):
    return [item for item in items if type(item) in (int, float)]


def main():
    print(numbers([1, "2", {"3": "4"}, {4, 5}, 5, 6, 7.0]))


if __name__ == '__main__':
    main()
