def compute_grouped_by_rhyme_words(words):
    rhymes = []
    for word in words:
        if len(word) > 1:
            for rhyme in rhymes:
                if word[-2:] == rhyme[0][-2:]:
                    rhyme.append(word)
                    break
            else:
                rhymes.append([word])
    return rhymes


def main():
    print(compute_grouped_by_rhyme_words(['cana', 'banana', 'carte', 'arme', 'parte']))


if __name__ == '__main__':
    main()
