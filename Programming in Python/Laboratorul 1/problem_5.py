def get_spiral_order(matrix):
    """
    :param matrix: the matrix to be traversed in spiral order
    :return: the string that contains the spiral order
    """
    spiral_order = ""
    top = 0
    left = 0
    bottom = len(matrix) - 1
    right = len(matrix[0]) - 1
    direction = 0
    while top <= bottom and left <= right:
        if direction == 0:  # left to right
            for index in range(left, right + 1):
                spiral_order += matrix[top][index]
            top += 1
        elif direction == 1:  # top to bottom
            for index in range(top, bottom + 1):
                spiral_order += matrix[index][right]
            right -= 1
        elif direction == 2:  # right to left
            for index in range(right, left - 1, -1):
                spiral_order += matrix[bottom][index]
            bottom -= 1
        elif direction == 3:  # bottom to top
            for index in range(bottom, top - 1, -1):
                spiral_order += matrix[index][left]
            left += 1
        direction = (direction + 1) % 4
    return spiral_order


def main():
    """
    Prints the string that contains a matrix of characters in spiral order.
    """
    matrix = [["f", "i", "r", "s"], ["n", "_", "l", "t"], ["o", "b", "a", "_"], ["h", "t", "y", "p"]]
    print(get_spiral_order(matrix))


if __name__ == '__main__':
    main()
