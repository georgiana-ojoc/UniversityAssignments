def vowels(string):
    result = []
    for character in string:
        if character in "aeiou":
            result += [character]
    return result


def main():
    print(vowels("Programming in Python is fun."))
    print((lambda string: [character for character in string if character in "aeiou"])("Programming in Python is fun."))
    print(list(filter(lambda character: character in "aeiou", "Programming in Python is fun.")))


if __name__ == '__main__':
    main()
