def compute_transposed_lists(*lists):
    return [tuple(lists[line][column] if column < len(lists[line]) else None
                  for line in range(len(lists))) for column in range(max([len(sequence) for sequence in lists]))]


def main():
    print(compute_transposed_lists([1, 2, 3], [4, 5, 6, 7], ["a", "b", "c"]))


if __name__ == '__main__':
    main()
