def compute_occurrences(*items, **dictionary):
    return len([item for item in items if item in dictionary.values()])


def main():
    print(compute_occurrences(1, 2, 3, 4, x=1, y=2, z=3, w=5))


if __name__ == '__main__':
    main()
