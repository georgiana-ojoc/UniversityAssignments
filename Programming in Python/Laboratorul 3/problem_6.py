def compute_uniques_and_duplicates(item_list):
    item_set = set(item_list)
    uniques = len([item for item in item_set if item_list.count(item) == 1])
    duplicates = len([item for item in item_set if item_list.count(item) > 1])
    return uniques, duplicates


def main():
    uniques, duplicates = compute_uniques_and_duplicates([1, 2, 2, 2, 3])
    print("Number of unique items:", uniques)
    print("Number of duplicate items:", duplicates)


if __name__ == '__main__':
    main()
