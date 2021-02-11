import re


def words(text):
    return re.findall(r"[a-zA-Z-0-9]+", text)


def main():
    print(words("This is a test. 123 is a number."))


if __name__ == '__main__':
    main()
