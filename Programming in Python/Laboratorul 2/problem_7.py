def compute_list_of_copies(copies_number, *lists):
    copies = list()
    extended_list = list()
    for sequence in lists:
        extended_list.extend(sequence)
    for item in extended_list:
        if item not in copies and extended_list.count(item) == copies_number:
            copies.append(item)
    return copies


def main():
    print(compute_list_of_copies(0, [1, 2, 3], [2, 3, 4], [4, 5, 6], ["1", 1, 4]))
    print(compute_list_of_copies(1, [1, 2, 3], [2, 3, 4], [4, 5, 6], ["1", 1, 4]))
    print(compute_list_of_copies(2, [1, 2, 3], [2, 3, 4], [4, 5, 6], ["1", 1, 4]))
    print(compute_list_of_copies(3, [1, 2, 3], [2, 3, 4], [4, 5, 6], ["1", 1, 4]))
    print(compute_list_of_copies(4, [1, 2, 3], [2, 3, 4], [4, 5, 6], ["1", 1, 4]))


if __name__ == '__main__':
    main()
