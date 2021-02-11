def compute_disadvantaged_spectators(heights):
    if len(heights) == 0:
        return []
    heights = [[heights[line][column] for line in range(len(heights))] for column in range(len(heights[0]))]
    return [(column, line) for line in range(len(heights)) for column in range(len(heights[0]))
            if column > 0 and heights[line][column] <= max(heights[line][:column])]


def main():
    print(compute_disadvantaged_spectators([]))
    print(compute_disadvantaged_spectators([[]]))
    print(compute_disadvantaged_spectators([[1, 2, 3, 2, 1, 1],
                                            [2, 4, 4, 3, 7, 2],
                                            [5, 5, 2, 5, 6, 4],
                                            [6, 6, 7, 6, 7, 5]]))


if __name__ == '__main__':
    main()
