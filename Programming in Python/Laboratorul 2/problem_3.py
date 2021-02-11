def compute_greatest_common_divisor(first, second):
    if first == 0 and second == 0:
        return None
    while second:
        first, second = second, first % second
    return first


def compute_extended_greatest_common_divisor(numbers):
    if len(numbers) < 2:
        return None
    result = compute_greatest_common_divisor(numbers[0], numbers[1])
    for number in numbers[2:]:
        result = compute_greatest_common_divisor(result, number)
    return result


def get_lines(points):
    points.sort()
    lines = list()
    for x in points:
        for y in points[points.index(x) + 1:]:
            a = x[1] - y[1]
            b = y[0] - x[0]
            c = x[0] * y[1] - y[0] * x[1]
            extended_greatest_common_divisor = compute_extended_greatest_common_divisor([a, b, c])
            a //= extended_greatest_common_divisor
            b //= extended_greatest_common_divisor
            c //= extended_greatest_common_divisor
            if (a, b, c) not in lines:
                lines += [(a, b, c)]
    return lines


def main():
    number = int(input("Enter the number of points: "))
    points = [(int(input("Enter x: ")), int(input("Enter y: "))) for _ in range(number)]
    print("The lines determined by the points are: %s." % get_lines(points))


if __name__ == '__main__':
    main()
