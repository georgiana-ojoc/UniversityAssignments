def compute_chain(dictionary):
    if "start" not in dictionary:
        return []
    value = dictionary["start"]
    chain = []
    while value not in chain and len(chain) < len(dictionary):
        chain.append(value)
        if value in dictionary:
            value = dictionary[value]
        else:
            break
    return chain


def main():
    print(compute_chain({"a": "b"}))
    print(compute_chain({"start": "a", "b": "a", "a": "6", "6": "z", "x": "2", "z": "2", "2": "6", "y": "start"}))
    print(compute_chain({"start": "a", "b": "c"}))


if __name__ == '__main__':
    main()
