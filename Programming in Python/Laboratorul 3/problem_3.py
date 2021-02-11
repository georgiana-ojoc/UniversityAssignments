def get_type(item):
    return str(type(item)).split(" ")[1].replace("'", "").replace(">", "")


def compare_dictionaries(first, second):
    differences = []
    for key, value in first.items():
        if key not in second:
            differences.append("Key {} of type {} with value {} of type {} only in first dictionary.".format(
                key, get_type(key), value, get_type(value)))
    for key, value in second.items():
        if key not in first:
            differences.append("Key {} of type {} with value {} of type {} only in second dictionary.".format(
                key, get_type(key), value, get_type(value)))
    for key, value in first.items():
        if key in second:
            if not isinstance(value, type(second[key])):
                differences.append("For key {} of type {}, value {} has type {}, but value {} has type {}."
                                   .format(key, get_type(key), value, get_type(value),
                                           second[key], get_type(second[key])))
            else:
                if isinstance(value, (int, float, complex, bool, str, bool, bytes, bytearray, memoryview)):
                    if value != second[key]:
                        differences.append(
                            "For key {} of type {}, value {} of type {} is not equal to value {} of type {}.".format(
                                key, get_type(key), value, get_type(value),
                                second[key], get_type(second[key])))
                elif isinstance(value, (list, tuple, range, set, frozenset)):
                    new_first = {index + 1: item for index, item in enumerate(value)}
                    new_second = {index + 1: item for index, item in enumerate(second[key])}
                    new_differences = compare_dictionaries(new_first, new_second)
                    if new_differences is not None:
                        differences.append(new_differences)
                elif isinstance(value, dict):
                    new_differences = compare_dictionaries(value, second[key])
                    if new_differences is not None:
                        differences.append(new_differences)

    if len(differences) == 0:
        return None
    return differences


def main():
    first_dictionary = {"a": 1, "1": 2, "b": 2, (1, 1): frozenset({1, 2, 2, 3}), "x": {"a": [1, 2], "b": 3}}
    second_dictionary = {1: 2, "1": "2", (1, 1): frozenset({1, 3, 2, 2}), "x": {"b": 3, "a": [1, 2, 3]}}
    differences = compare_dictionaries(first_dictionary, second_dictionary)
    if differences is not None:
        print(str(differences).replace("', ", "',\n"))


if __name__ == '__main__':
    main()
