def compute_custom_ordered_tuples(tuples):
    return sorted(tuples, key=lambda pair: pair[1][2])


def main():
    print(compute_custom_ordered_tuples([("abc", "bcd"), ("abc", "zza")]))


if __name__ == '__main__':
    main()
