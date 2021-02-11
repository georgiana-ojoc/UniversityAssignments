"""
(source_position, destination_position, maze, path) => (next_position, destination_position, maze, path)
maze possible cell values:
 - 0: free
 - 1: wall
 - 2: selected
"""

import copy
import math
import numpy
import random


def initialize():
    """
    Reads the maze dimensions and the source and destination position coordinates from the console.
    Creates the maze as a random matrix of 0 and 1 and adds 2 in the source cell.
    Initializes the path with the source position.
    :return: a list containing the source position, the destination position, the maze, the path
    """
    n = 0
    while n == 0:
        n = int(input("Enter n: "))
    m = 0
    while m == 0:
        m = int(input("Enter m: "))
    matrix = numpy.random.randint(2, size=(n, m))
    print_maze(matrix)
    x = -1
    y = -1
    while True:
        while x < 0 or x >= n:
            x = int(input("Enter x of source: "))
        while y < 0 or y >= m:
            y = int(input("Enter y of source: "))
        if matrix[x][y] == 0:
            break
    source = (x, y)
    matrix[x][y] = 2
    x = -1
    y = -1
    while True:
        while x < 0 or x >= n:
            x = int(input("Enter x of destination: "))
        while y < 0 or y >= m:
            y = int(input("Enter y of destination: "))
        if matrix[x][y] == 0:
            break
    destination = (x, y)
    path = [source]
    return [source, destination, matrix, path]


def print_maze(matrix):
    """
    Displays the formatted matrix on the console.
    :param matrix: the matrix to be printed
    """
    print(' \t' + str([number for number in range(len(matrix[0]))])
          .replace('[', '').replace(']', '').replace(', ', '\t'))
    for index, line in enumerate(matrix):
        print(str(index) + '\t' + str([value for value in line])
              .replace('0', 'O').replace('1', 'X').replace('2', '*')
              .replace('[', '').replace(']', '').replace(', ', '\t'))


def is_final(state):
    """
    Checks if the current state is the final state.
    :param state: the current state
    :return: True if the current position is the destination position, False otherwise
    """
    return state[0] == state[1]


def is_valid(visited_cells, state, direction):
    """
    Checks if exists a free, unvisited cell in the specified direction.
    :param visited_cells: a list containing the visited cells
    :param state: the current state
    :param direction: a character specifying the direction ('N' for North, 'S' for South, 'W' for West or 'E' for East)
    :return: True if exists a free cell in the specified direction, False otherwise
    """
    if direction == 'N':
        x, y = state[0]
        return x - 1 >= 0 and state[2][x - 1][y] == 0 and (x - 1, y) not in visited_cells
    elif direction == 'S':
        x, y = state[0]
        return x + 1 < len(state[2]) and state[2][x + 1][y] == 0 and (x + 1, y) not in visited_cells
    elif direction == 'W':
        x, y = state[0]
        return y - 1 >= 0 and state[2][x][y - 1] == 0 and (x, y - 1) not in visited_cells
    elif direction == 'E':
        x, y = state[0]
        return y + 1 < len(state[2][0]) and state[2][x][y + 1] == 0 and (x, y + 1) not in visited_cells


def transition(state, direction):
    """
    Updates the current state with the cell from the specified direction.
    :param state: the current state
    :param direction: a character specifying the direction ('N' for North, 'S' for South, 'W' for West or 'E' for East)
    :return: a list representing the next state
    """
    if direction == 'N':
        x, y = state[0]
        state[2][x - 1][y] = 2
        state[3] += [(x - 1, y)]
        return [(x - 1, y), state[1], state[2], state[3]]
    elif direction == 'S':
        x, y = state[0]
        state[2][x + 1][y] = 2
        state[3] += [(x + 1, y)]
        return [(x + 1, y), state[1], state[2], state[3]]
    elif direction == 'W':
        x, y = state[0]
        state[2][x][y - 1] = 2
        state[3] += [(x, y - 1)]
        return [(x, y - 1), state[1], state[2], state[3]]
    elif direction == 'E':
        x, y = state[0]
        state[2][x][y + 1] = 2
        state[3] += [(x, y + 1)]
        return [(x, y + 1), state[1], state[2], state[3]]


def compute_candidate_states(visited_cells, state):
    """
    :param visited_cells: a list containing the visited cells
    :param state: the current state
    :return: a list containing the next accessible states from the current state
    """
    candidate_states = list()
    directions = ['N', 'S', 'W', 'E']
    for direction in directions:
        if is_valid(visited_cells, state, direction):
            state_copy = copy.deepcopy(state)
            candidate_states.append(transition(state_copy, direction))
    return candidate_states


def manhattan_distance(source, destination):
    """
    :param source: a tuple (x, y) with the coordinates of a source position
    :param destination: a tuple (x, y) with the coordinates of a destination position
    :return: an integer representing the Manhattan distance from the source to the destination
    """
    return abs(source[0] - destination[0]) + abs(source[1] - destination[1])


def is_equivalent(state, another_state):
    """
    :param state: a list representing a state
    :param another_state: a list representing another state
    :return: True if the first state is as close to the final state as the second state, False otherwise
    """
    return manhattan_distance(state[0], state[1]) == manhattan_distance(another_state[0], another_state[1])


def is_improvement(state, another_state):
    """
    :param state: a list representing a state
    :param another_state: a list representing another state
    :return: True if the first state is closer to the final state than the second state, False otherwise
    """
    return manhattan_distance(state[0], state[1]) < manhattan_distance(another_state[0], another_state[1])


def iterative_backtracking(initial_state):
    """
    Uses a list of visited cells.
    Uses a stack of next accessible states, initialized with the first state.
    At each step, chooses the next state from the stack and checks if it is the final state.
    Otherwise, adds to the stack the next accessible state, in the directions order North, South, East or West.
    If the current state is a dead-end, removes it from the stack.
    If the stack is empty, there is no path from the source to the destination.
    :param initial_state: a list representing the initial state
    :return: the last accessible state from the initial state, which can be the final state
    """
    current_state = copy.deepcopy(initial_state)
    accessible_states = [current_state]
    visited_cells = []
    while len(accessible_states) > 0:
        current_state = accessible_states[-1]
        visited_cells.append(current_state[0])
        if is_final(current_state):
            return current_state
        if is_valid(visited_cells, current_state, 'N'):
            state_copy = copy.deepcopy(current_state)
            accessible_states.append(transition(state_copy, 'N'))
        elif is_valid(visited_cells, current_state, 'S'):
            state_copy = copy.deepcopy(current_state)
            accessible_states.append(transition(state_copy, 'S'))
        elif is_valid(visited_cells, current_state, 'W'):
            state_copy = copy.deepcopy(current_state)
            accessible_states.append(transition(state_copy, 'W'))
        elif is_valid(visited_cells, current_state, 'E'):
            state_copy = copy.deepcopy(current_state)
            accessible_states.append(transition(state_copy, 'E'))
        else:
            del accessible_states[-1]
    return current_state


def recursive_backtracking(initial_state):
    current_state = copy.deepcopy(initial_state)
    return backtracking_recursion([], current_state)


def backtracking_recursion(visited_cells, current_state):
    """
    Uses a list of visited cells.
    At each call, checks if the current state is the final state.
    Otherwise, recursively calls the function with the next accessible state,
    in the directions order North, South, East or West.
    :param visited_cells: a list containing the visited cells, initially empty
    :param current_state: a list representing the current state, initially the first state
    :return: the last accessible state from the initial state, which can be the final state
    """
    visited_cells.append(current_state[0])
    if is_final(current_state):
        return current_state
    directions = ['N', 'S', 'W', 'E']
    for direction in directions:
        if is_valid(visited_cells, current_state, direction):
            current_state_copy = copy.deepcopy(current_state)
            next_state = backtracking_recursion(visited_cells, transition(current_state_copy, direction))
            if is_final(next_state):
                return next_state
    return current_state


def depth_first_search(initial_state):
    """
    Uses a list of visited cells.
    Uses a stack of next accessible states, initialized with the first state.
    At each step, chooses the next state from the stack and checks if it is the final state.
    Otherwise, adds to the stack the next accessible states.
    If the current state is a dead-end, removes it from the stack.
    If the stack is empty, there is no path from the source to the destination.
    :param initial_state: a list representing the initial state
    :return: the last accessible state from the initial state, which can be the final state
    """
    current_state = copy.deepcopy(initial_state)
    accessible_states = [current_state]
    visited_cells = []
    while len(accessible_states) > 0:
        current_state = accessible_states.pop()
        visited_cells.append(current_state[0])
        if is_final(current_state):
            return current_state
        accessible_states.extend(compute_candidate_states(visited_cells, current_state))
    return current_state


def breadth_first_search(initial_state):
    """
    Uses a list of visited cells.
    Uses a queue of next accessible states, initialized with the first state.
    At each step, chooses the next state from the queue and checks if it is the final state.
    Otherwise, adds to the queue the next accessible states.
    If the current state is a dead-end, removes it from the queue.
    If the queue is empty, there is no path from the source to the destination.
    :param initial_state: a list representing the initial state
    :return: the last accessible state from the initial state, which can be the final state
    """
    current_state = copy.deepcopy(initial_state)
    accessible_states = [current_state]
    visited_cells = []
    while len(accessible_states) > 0:
        current_state = accessible_states.pop(0)
        visited_cells.append(current_state[0])
        if is_final(current_state):
            return current_state
        accessible_states.extend(compute_candidate_states(visited_cells, current_state))
    return current_state


def next_ascent_hill_climbing(initial_state):
    """
    Uses a list of visited cells.
    Shuffles the list containing the next accessible states from the current state,
    so that it does not return the same path at every run.
    At each step, chooses the state that produces the first improvement, in comparison to the current state.
    If there is no state better than the current state, chooses the first state that is as good as the current state.
    :param initial_state: a list representing the initial state
    :return: the last accessible state from the initial state, which can be the final state
    """
    current_state = copy.deepcopy(initial_state)
    visited_cells = [current_state[0]]
    while not is_final(current_state):
        previous_state = current_state
        candidate_states = compute_candidate_states(visited_cells, current_state)
        random.shuffle(candidate_states)
        for candidate_state in candidate_states:
            visited_cells.append(candidate_state[0])
            if is_final(candidate_state):
                return candidate_state
            if is_improvement(candidate_state, current_state):
                current_state = candidate_state
                break
        if current_state == previous_state:
            random.shuffle(candidate_states)
            for candidate_state in candidate_states:
                if is_equivalent(candidate_state, current_state):
                    current_state = candidate_state
                    break
            if current_state == previous_state:
                return current_state
    return current_state


def steepest_ascent_hill_climbing(initial_state):
    """
    Uses a list of visited cells.
    Shuffles the list containing the next accessible states from the current state,
    so that it does not return the same path at every run.
    At each step, chooses the state that produces the best improvement, in comparison to the current state.
    If there is a state as good as the current state, randomly chooses it.
    :param initial_state: a list representing the initial state
    :return: the last accessible state from the initial state, which can be the final state
    """
    current_state = copy.deepcopy(initial_state)
    visited_cells = [current_state[0]]
    while not is_final(current_state):
        previous_state = current_state
        candidate_states = compute_candidate_states(visited_cells, current_state)
        random.shuffle(candidate_states)
        for candidate_state in candidate_states:
            visited_cells.append(candidate_state[0])
            if is_final(candidate_state):
                return candidate_state
            if is_improvement(candidate_state, current_state):
                current_state = candidate_state
        random.shuffle(candidate_states)
        for candidate_state in candidate_states:
            if is_equivalent(candidate_state, current_state):
                current_state = candidate_state
                break
        if current_state == previous_state:
            return current_state
    return current_state


def simulated_annealing(initial_state):
    """
    Uses a list of visited cells.
    Shuffles the list containing the next accessible states from the current state,
    so that it does not return the same path at every run.
    At each step, chooses the state that produces the best improvement, in comparison to the current state.
    If there is a state as good as the current state, randomly chooses it.
    If no state has been chosen by now, randomly chooses a state with a probability based on a temperature
    that decreases with every step.
    :param initial_state: a list representing the initial state
    :return: the last accessible state from the initial state, which can be the final state
    """
    current_state = copy.deepcopy(initial_state)
    visited_cells = [current_state[0]]
    temperature = 200
    cooling_rate = 0.1
    while not is_final(current_state):
        previous_state = current_state
        candidate_states = compute_candidate_states(visited_cells, current_state)
        random.shuffle(candidate_states)
        for candidate_state in candidate_states:
            visited_cells.append(candidate_state[0])
            if is_final(candidate_state):
                return candidate_state
            if is_improvement(candidate_state, current_state):
                current_state = candidate_state
        for candidate_state in candidate_states:
            if is_equivalent(candidate_state, current_state) and random.random() >= 0.5:
                current_state = candidate_state
        if current_state == previous_state:
            random.shuffle(candidate_states)
            for candidate_state in candidate_states:
                candidate_value = manhattan_distance(candidate_state[0], candidate_state[1])
                current_value = manhattan_distance(current_state[0], current_state[1])
                if random.random() < math.exp((current_value - candidate_value) / temperature):
                    current_state = candidate_state
                    break
            if current_state == previous_state:
                return current_state
        temperature *= 1 - cooling_rate
    return current_state


def main():
    """
    Exemplifies the functionality of the program.
    Shows the maze if a path from the source to the destination position was found.
    """
    initial_state = initialize()
    print("Iterative Backtracking:")
    final_state = iterative_backtracking(initial_state)
    print(str(final_state[3]).replace('[', '').replace(']', ''))
    if is_final(final_state):
        print_maze(final_state[2])

    print("Recursive Backtracking:")
    final_state = recursive_backtracking(initial_state)
    print(str(final_state[3]).replace('[', '').replace(']', ''))
    if is_final(final_state):
        print_maze(final_state[2])

    print("Depth First Search:")
    final_state = depth_first_search(initial_state)
    print(str(final_state[3]).replace('[', '').replace(']', ''))
    if is_final(final_state):
        print_maze(final_state[2])

    print("Breadth First Search:")
    final_state = breadth_first_search(initial_state)
    print(str(final_state[3]).replace('[', '').replace(']', ''))
    if is_final(final_state):
        print_maze(final_state[2])

    print("First Ascent Hill Climbing:")
    for run in range(1, 5):
        final_state = next_ascent_hill_climbing(initial_state)
        print(str(run) + ". " + str(final_state[3]).replace('[', '').replace(']', ''))
        if is_final(final_state):
            print_maze(final_state[2])

    print("Steepest Ascent Hill Climbing:")
    for run in range(1, 5):
        final_state = steepest_ascent_hill_climbing(initial_state)
        print(str(run) + ". " + str(final_state[3]).replace('[', '').replace(']', ''))
        if is_final(final_state):
            print_maze(final_state[2])

    print("Simulated Annealing:")
    for run in range(1, 5):
        final_state = simulated_annealing(initial_state)
        print(str(run) + ". " + str(final_state[3]).replace('[', '').replace(']', ''))
        if is_final(final_state):
            print_maze(final_state[2])


if __name__ == '__main__':
    main()
