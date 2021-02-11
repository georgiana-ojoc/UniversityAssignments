def compute_divisible_characters(strings, value=1, flag=True):
    character_lists = []
    for string in strings:
        characters = [character for character in string if (ord(character) % value == 0) is flag]
        divisible_characters = []
        for character in characters:
            if character not in divisible_characters:
                divisible_characters.append(character)
        character_lists.append(divisible_characters)
    return character_lists


def main():
    print(compute_divisible_characters(["Ana", "Maria", "Elena"]))


if __name__ == '__main__':
    main()
