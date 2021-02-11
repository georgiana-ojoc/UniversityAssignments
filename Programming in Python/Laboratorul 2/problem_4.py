def compute_intersection(first, second):
    second_copy = list(second)
    intersection = list()
    for number in first:
        if number in second:
            intersection.append(number)
            second_copy.remove(number)
    return intersection


def compute_union(first, second):
    first_copy = list(first)
    union = list(first)
    for number in second:
        if number not in first_copy:
            union.append(number)
        else:
            first_copy.remove(number)
    return union


def compute_difference(first, second):
    return [number for number in first if number not in second]


def compute_operations(first, second):
    return compute_intersection(first, second), compute_union(first, second), \
           compute_difference(first, second), compute_difference(second, first)


def read_list():
    number = int(input("Enter the number of numbers: "))
    return [int(input("Enter a number: ")) for _ in range(number)]


def main():
    first = read_list()
    second = read_list()
    lists = compute_operations(first, second)
    print("The intersection of the two lists is: %s." % lists[0])
    print("The union of the two lists is: %s." % lists[1])
    print("The difference between the first list and the second list is: %s." % lists[2])
    print("The difference between the second list and the first list is: %s." % lists[3])


if __name__ == '__main__':
    main()
