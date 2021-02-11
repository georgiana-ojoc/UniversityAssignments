def compose(notes, moves, start):
    if start < 0 or start >= len(notes):
        return list()
    song = list([notes[start]])
    step = start
    for move in moves:
        step += move
        step %= len(notes)
        song.append(notes[step])
    return song


def main():
    print(compose(["do", "re", "mi", "fa", "sol"], [1, -3, 4, 2], 2))


if __name__ == '__main__':
    main()

