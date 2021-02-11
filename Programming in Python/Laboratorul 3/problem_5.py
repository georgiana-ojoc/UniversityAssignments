def validate_dictionary(rules, dictionary):
    checked_entries = 0
    for rule in rules:
        if len(rule) != 4:
            return False
        if rule[0] not in dictionary:
            continue
        value = dictionary[rule[0]]
        if not value.startswith(rule[1]):
            return False
        if not rule[2] in value[1:-1]:
            return False
        if not value.endswith(rule[3]):
            return False
        checked_entries += 1
    if checked_entries != len(dictionary):
        return False
    return True


def main():
    print(validate_dictionary({("1", "", "inside", ""), ("2", "start", "middle", "end")},
                              {"1": "come inside, it is cold outside", "3": "this is a test."}))


if __name__ == '__main__':
    main()
