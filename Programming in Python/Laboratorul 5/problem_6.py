def pairs(integers):
    evens = list(filter(lambda integer: integer % 2 == 0, integers))
    odds = list(filter(lambda integer: integer % 2 == 1, integers))
    return list(zip(evens, odds))


def main():
    print(pairs([1, 3, 5, 2, 8, 7, 4, 10, 9, 2]))


if __name__ == '__main__':
    main()
