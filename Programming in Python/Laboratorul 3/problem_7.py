def compute_operations_dictionary(*sets):
    dictionary = {}
    for first in sets:
        for second in sets[sets.index(first) + 1:]:
            dictionary[str(first) + " | " + str(second)] = first | second
            dictionary[str(first) + " & " + str(second)] = first & second
            dictionary[str(first) + " - " + str(second)] = first - second
            dictionary[str(second) + " - " + str(first)] = second - first
    return dictionary


def main():
    print(str(compute_operations_dictionary({1, 2}, {2, 3}, {3, 4})).replace(" '", "\n'"))


if __name__ == '__main__':
    main()
