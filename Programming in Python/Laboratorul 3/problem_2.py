def compute_frequencies(string):
    frequencies = {character: 0 for character in string}
    for character in string:
        frequencies[character] = frequencies[character] + 1
    return frequencies


def main():
    print(compute_frequencies("Ana has apples."))


if __name__ == '__main__':
    main()
