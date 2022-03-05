import numpy as np
import matplotlib.pyplot as plt


def generate_points_labels(x_range, y_range, point_number, label):
    shape = (point_number, 1)
    x = np.random.uniform(*x_range, shape)
    y = np.random.uniform(*y_range, shape)
    ones = np.ones(shape)
    labels = np.ones(point_number) * label
    return np.concatenate((x, y, ones), axis=1), labels


def generate_line(_min, _max):
    a, b = np.random.uniform(0, 1, 2)
    c = np.random.uniform(_min, _max)
    return np.array([a, b, c])


def plot(_min, _max, point_number, points, line):
    plt.scatter(points[:point_number, 0], points[:point_number, 1], c="orange")
    plt.scatter(points[point_number:, 0], points[point_number:, 1], c="blue")
    x = np.linspace(_min, _max, _max - _min + 1)
    a, b, c = line
    y = (-a * x - c) / b
    plt.plot(x, y, "-r")
    plt.xlim(_min, _max)
    plt.ylim(_min, _max)
    plt.show()


def compute_fitness_inaccuracy(epsilon, points, labels, line):
    distances = np.dot(points, line) * labels
    distances = np.where(distances < 0, np.abs(distances), 0)
    fitness = 1 / (epsilon + np.sum(distances))
    inaccuracy = np.count_nonzero(distances)
    return fitness, inaccuracy


def generate_neighbors(neighbor_number, a_b_error, c_error, line):
    shape = (neighbor_number, 1)
    a, b, c = line
    a_neighbors = np.random.uniform(a - a_b_error, a + a_b_error, shape)
    b_neighbors = np.random.uniform(b - a_b_error, b + a_b_error, shape)
    c_neighbors = np.random.uniform(c - c_error, c + c_error, shape)
    return np.concatenate((a_neighbors, b_neighbors, c_neighbors), axis=1)


def steepest_ascent_hill_climbing(_min, _max, point_number, neighbor_number, epsilon, a_b_error, c_error, points,
                                  labels, line):
    iteration = 1
    current_fitness, current_inaccuracy = compute_fitness_inaccuracy(epsilon, points, labels, line)
    while current_fitness < 1 / epsilon:
        plot(_min, _max, point_number, points, line)
        print(f"{iteration}. Fitness: {current_fitness} and incorrectly classified points: "
              f"{current_inaccuracy}")
        neighbors = generate_neighbors(neighbor_number, a_b_error, c_error, line)
        fitnesses, inaccuracies = np.hsplit(np.apply_along_axis(lambda neighbor:
                                                                compute_fitness_inaccuracy(epsilon, points,
                                                                                           labels, neighbor), 1,
                                                                neighbors), 2)
        best_line_index = np.argmax(fitnesses)
        line = neighbors[best_line_index]
        current_fitness = fitnesses[best_line_index][0]
        current_inaccuracy = np.int(inaccuracies[best_line_index][0])
        iteration += 1
    plot(_min, _max, point_number, points, line)
    print(f"{iteration}. Fitness: {current_fitness} and incorrectly classified points: {current_inaccuracy}")
    return line


def main():
    _min = 0
    _max = 100
    x_1_max = 45
    x_2_min = 55
    point_number = 50
    neighbor_number = 50
    epsilon = 1e-8
    a_b_error = 1e-1
    c_error = a_b_error * (_max - _min)
    y_range = (_min, _max)
    points_1, labels_1 = generate_points_labels((_min, x_1_max), y_range, point_number, -1)
    points_2, labels_2 = generate_points_labels((x_2_min, _max), y_range, point_number, 1)
    points = np.concatenate((points_1, points_2), axis=0)
    labels = np.concatenate((labels_1, labels_2))
    line = generate_line(_min, _max)
    line = steepest_ascent_hill_climbing(_min, _max, point_number, neighbor_number, epsilon, a_b_error, c_error,
                                         points, labels, line)
    a, b, c = line
    print(f"Line: {a}x + {b}y + {c} = 0")


if __name__ == '__main__':
    main()
