def compute_operations_list(first_list, second_list):
    first_set = set(first_list)
    second_set = set(second_list)
    return [first_set & second_set, first_set | second_set, first_set - second_set, second_set - first_set,
            first_set ^ second_set]


def compute_operations_set(first_list, second_list):
    first_set = frozenset(first_list)
    second_set = frozenset(second_list)
    return {first_set & second_set, first_set | second_set, first_set - second_set, second_set - first_set,
            first_set ^ second_set}


def main_list():
    first_list = [1, 2, 2, 3, 4, 5]
    second_list = [1, 2, 2, 3, 4, 5]
    print("First list:", first_list)
    print("Second list:", second_list)
    operations = compute_operations_list(first_list, second_list)
    print("Intersection:", operations[0])
    print("Union:", operations[1])
    print("Difference between the first list and the second list:", operations[2])
    print("Difference between the second list and the first list:", operations[3])
    print("Symmetric difference:", operations[4])


def main_set():
    first_list = [1, 2, 2, 3, 4, 5]
    second_list = [1, 2, 2, 3, 4, 5]
    print("First list:", first_list)
    print("Second list:", second_list)
    operations = list(compute_operations_set(first_list, second_list))
    if len(operations) == 5:
        print("Intersection:", operations[0])
        print("Union:", operations[1])
        print("Difference between the first list and the second list:", operations[2])
        print("Difference between the second list and the first list:", operations[3])
        print("Symmetric difference:", operations[4])


if __name__ == '__main__':
    main_list()
    print()
    main_set()
