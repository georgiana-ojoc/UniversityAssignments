def particular_dictionaries(*dictionaries, **keyword_dictionaries):
    result = []
    for item in dictionaries + tuple(keyword_dictionaries.values()):
        if type(item) is dict:
            if len(item.keys()) >= 2:
                if len([key for key in item if type(key) is str and len(key) >= 3]) > 0:
                    if len(list(filter(lambda key: type(key) is str and len(key) >= 3, item))) > 0:
                        result += [item]
    return result


def main():
    print(particular_dictionaries({1: 2, 3: 4, 5: 6},
                                  {'a': 5, 'b': 7, 'c': 9},
                                  {2: 3},
                                  [1, 2, 3],
                                  {"abc": 4, "def": 5},
                                  3456,
                                  dictionary={"ab": 4, "ac": "abc", "ad": "abc"},
                                  test={1: 1, "test": True}))


if __name__ == '__main__':
    main()
