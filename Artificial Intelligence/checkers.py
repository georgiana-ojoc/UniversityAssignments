"""
Noua funcție de evaluare urmărește să evite situațiie în care calculatorul mută o piesă pe o poziție intermediară din
care nu mai poate înainta către o poziție finală (dead-end).

Aceasta se calculează ca următoarea sumă ponderată:
value = old_value * 2 + computer_victory_positions * 5 + free_south_positions * 1.5 + free_sideways_positions,
unde:
 - old_value este valoarea funcției de evaluare descrise în cerință (12 - suma liniilor calculatorului - suma liniilor
   omului),
 - computer_victory_points este numărul de piese ale calculatorului care au ajuns pe ultima linie,
 - free_south_positions este numărul de poziții libere pe care se pot muta piesele calculatorului înspre ultima linie
   (exemplu: dacă piesele calculatorului se află pe pozițiile (2, 0), (3, 1), (3, 2), (3, 3), atunci
   free_south_positions în acest caz va fi 4, corespunzător pozițiilor (1, 0), (2, 1), (2, 2), (2, 3))
 - free_sideways_positions este numărul de poziții libere pe care se pot muta piesele calculatorului în lateral
   (exemplu: dacă piesele calculatorului se află pe pozițiile (2, 0), (3, 1), (3, 2), (3, 3), atunci
   free_south_positions în acest caz va fi doar 1, corespunzător poziției (3, 0), deoarece poziția (2, 1) este numărată
   la pasul anterior)

Un exemplu de stare în care noua funcție este mai utilă decât vechea funcție este următorul:
 - dacă piesele calculatorului se află pe pozițiile (0, 0), (0, 1), (2, 1), (3, 2), iar piesele omului se află pe
   pozițiile (3, 0), (0, 2), (1, 2), (1, 0),
      -----------------
   3  | 1 |   | 0 |   |
      -----------------
   2  |   | 0 |   |   |
      -----------------
   1  | 1 |   | 1 |   |
      -----------------
   0  | 0 | 0 | 1 |   |
      -----------------
        0   1   2   3
 - atunci calculatorul va muta a treia piesă de pe poziția (2, 1) pe poziția (2, 2),
      -----------------
   3  | 1 |   | 0 |   |
      -----------------
   2  |   |   | 0 |   |
      -----------------
   1  | 1 |   | 1 |   |
      -----------------
   0  | 0 | 0 | 1 |   |
      -----------------
        0   1   2   3
 - în timp ce vechea funcție ar fi indicat ca a treia piesă să fie mutată de pe poziția (2, 1) pe poziția (2, 2), de
   unde trebuie să se întoarcă pentru a ajunge pe ultima linie.
      -----------------
   3  | 1 |   | 0 |   |
      -----------------
   2  |   |   |   |   |
      -----------------
   1  | 1 | 0 | 1 |   |
      -----------------
   0  | 0 | 0 | 1 |   |
      -----------------
        0   1   2   3
"""

import copy
import math


def generate_board():
    """
    The board is saved in memory as tuple of two lists with four tuples, one for computer and one for human.
    Each tuple contains piece cartesian coordinates on board.
    :return: board
    """
    computer_pieces = [(3, 0), (3, 1), (3, 2), (3, 3)]
    human_pieces = [(0, 0), (0, 1), (0, 2), (0, 3)]
    return computer_pieces, human_pieces


def initial_state():
    """
    A state is a tuple of board and current turn (0 for computer and 1 for human)
    :return: initial state (computer starts)
    """
    return generate_board(), 0


def is_final(state):
    """
    Sums lines coordinates of each player in order to see if one moved all his pieces on his last line.
    :param state: current state
    :return: True if a player won, False otherwise
    """
    board, turn = state
    computer_line_sum = sum([line for line, column in board[0]])
    if computer_line_sum == 0:
        return True
    human_line_sum = sum([line for line, column in board[1]]) / 4
    if human_line_sum == 3:
        return True
    return False


def next_turn(turn):
    """
    :param turn: current turn
    :return: next turn
    """
    return (turn + 1) % 2


def exists_piece(state, piece):
    """
    :param state: current state
    :param piece: selected piece
    :return: True if current player has specified piece, False otherwise
    """
    board, turn = state
    return piece in board[turn]


def is_outside(next_position):
    """
    :param next_position: next position
    :return: True if specified position is outside board, False otherwise
    """
    line, column = next_position
    return line < 0 or line > 3 or column < 0 or column > 3


def same_position(current_position, next_position):
    """
    :param current_position: selected position
    :param next_position: next position
    :return: True if specified positions are one and the same, False otherwise
    """
    return current_position[0] == next_position[0] and current_position[1] == next_position[1]


def is_occupied(state, next_position):
    """
    :param state: current state
    :param next_position: next position
    :return: True if on next position is another piece (of computer or of human), False otherwise
    """
    board, turn = state
    for piece in board[turn]:
        if same_position(piece, next_position):
            return True
    for piece in board[next_turn(turn)]:
        if same_position(piece, next_position):
            return True
    return False


def are_neighbors(current_position, next_position):
    """
    :param current_position: selected position
    :param next_position: next position
    :return: True if specified positions are neighbors, False otherwise
    """
    return abs(current_position[0] - next_position[0]) <= 1 and abs(current_position[1] - next_position[1]) <= 1


def move_is_valid(state, move):
    """
    A move is a tuple of selected position and next position.
    :param state: current state
    :param move: possible move
    :return: True if next position is valid, False otherwise
    """
    current_position, next_position = move
    if not exists_piece(state, current_position) or is_outside(next_position) \
            or is_occupied(state, next_position) or not are_neighbors(current_position, next_position):
        return False
    return True


def north_directions():
    """
    :return: list of tuples with north accessible directions from selected position
    """
    return [(1, -1), (1, 0), (1, 1)]


def south_directions():
    """
    :return: list of tuples with south accessible directions from selected position
    """
    return [(-1, -1), (-1, 0), (-1, 1)]


def sideways_directions():
    """
    :return: list of tuples with sideways accessible directions from selected position
    """
    return [(0, -1), (0, 1)]


def all_directions():
    """
    :return: list of tuples with all accessible directions from selected position
    """
    return south_directions() + sideways_directions() + north_directions()


def free_positions(state):
    """
    :param state: current state
    :return: tuple of tree sets with free south, sideways and north positions for computer
    """
    board, turn = state
    free_positions_set = set()
    free_north_positions = set()
    free_south_positions = set()
    free_sideways_positions = set()
    for piece in board[0]:
        for direction in south_directions():
            next_position = (piece[0] + direction[0], piece[1] + direction[1])
            if not (next_position in free_positions_set
                    or is_outside(next_position) or is_occupied(state, next_position)):
                free_positions_set.add(next_position)
                free_south_positions.add(next_position)
    for piece in board[0]:
        for direction in sideways_directions():
            next_position = (piece[0] + direction[0], piece[1] + direction[1])
            if not (next_position in free_positions_set
                    or is_outside(next_position) or is_occupied(state, next_position)):
                free_positions_set.add(next_position)
                free_sideways_positions.add(next_position)
    for piece in board[0]:
        for direction in north_directions():
            next_position = (piece[0] + direction[0], piece[1] + direction[1])
            if not (next_position in free_positions_set
                    or is_outside(next_position) or is_occupied(state, next_position)):
                free_positions_set.add(next_position)
                free_north_positions.add(next_position)
    return free_south_positions, free_sideways_positions, free_north_positions


def evaluation_function(state):
    """
    :param state: current state
    :return: value of evaluation function
    """
    board, turn = state
    computer_line_sum = sum([line for line, column in board[0]])
    human_line_sum = sum([line for line, column in board[1]])
    value = 12 - computer_line_sum - human_line_sum
    computer_victory_positions = len([position for position in board[0] if position[0] == 0])
    free_positions_sets = free_positions(state)
    value = value * 2 + computer_victory_positions * 5 + len(free_positions_sets[0]) * 1.5 + len(free_positions_sets[1])
    return value


def generate_possible_moves(state):
    """
    :param state: current state
    :return: list of possible moves from selected position
    """
    board, turn = state
    moves = []
    directions = all_directions()
    for piece in board[turn]:
        for direction in directions:
            next_position = (piece[0] + direction[0], piece[1] + direction[1])
            next_mode = (piece, next_position)
            if move_is_valid(state, next_mode):
                moves += [next_mode]
    return moves


def piece_index(positions, piece):
    """
    :param positions: position list of current player
    :param piece: selected piece
    :return: index of specified piece in specified position list
    """
    for index in range(len(positions)):
        if same_position(positions[index], piece):
            return index
    return -1


def make_move(state, move):
    """
    :param state: current state
    :param move: selected move
    :return: next state with updated positions and next turn
    """
    board, turn = state
    current_position, next_position = move
    next_computer_positions = copy.deepcopy(board[0])
    next_human_positions = copy.deepcopy(board[1])
    new_board = (next_computer_positions, next_human_positions)
    new_board[turn][piece_index(board[turn], current_position)] = next_position
    return new_board, next_turn(turn)


def minimax(state, height, is_maximizing_level):
    """
    :param state: current state
    :param height: tree height
    :param is_maximizing_level: True if is computer level, False otherwise
    :return: tuple of selected move with corresponding evaluation function value
    """
    if height == 0 or is_final(state):
        return None, evaluation_function(state)
    move_values = []
    moves = generate_possible_moves(state)
    for move in moves:
        value = minimax(make_move(state, move), height - 1, not is_maximizing_level)[1]
        move_values += [(move, value)]
    if is_maximizing_level:
        return max(move_values, key=lambda item: item[1])
    else:
        return min(move_values, key=lambda item: item[1])


def minimum_value(state, alfa, beta, height):
    """
    :param state: current state
    :param alfa: computer level node value
    :param beta: human level node value
    :param height: tree height
    :return: tuple of selected move with corresponding evaluation function value
    """
    if height == 0 or is_final(state):
        return None, evaluation_function(state)
    optimal_value = math.inf
    optimal_move = None
    moves = generate_possible_moves(state)
    for move in moves:
        value = maximum_value(make_move(state, move), alfa, beta, height - 1)[1]
        if optimal_value > value:
            optimal_value = value
            optimal_move = move
        if optimal_value <= alfa:
            return optimal_move, optimal_value
        beta = min(beta, optimal_value)
    return optimal_move, optimal_value


def maximum_value(state, alfa, beta, height):
    """
    :param state: current state
    :param alfa: computer level node value
    :param beta: human level node value
    :param height: tree height
    :return: tuple of selected move with corresponding evaluation function value
    """
    if height == 0 or is_final(state):
        return None, evaluation_function(state)
    optimal_value = -math.inf
    optimal_move = None
    moves = generate_possible_moves(state)
    for move in moves:
        value = minimum_value(make_move(state, move), alfa, beta, height - 1)[1]
        if optimal_value < value:
            optimal_value = value
            optimal_move = move
        if optimal_value >= beta:
            return optimal_move, optimal_value
        alfa = max(alfa, optimal_value)
    return optimal_move, optimal_value


def minimax_alfa_beta_pruning(state, height):
    """
    :param state: current state
    :param height: tree height
    :return: tuple of selected move with corresponding evaluation function value
    """
    alfa = -math.inf
    beta = math.inf
    move, value = maximum_value(state, alfa, beta, height)
    return move, value


def print_board(board):
    """
    Prints board in matrix format.
    :param board: current board
    """
    print("\n   -----------------")
    for line in range(3, -1, -1):
        print(line, " | ", end='')
        for column in range(4):
            cell = ' '
            for position in board[0]:
                if same_position((line, column), position):
                    cell = '0'
            for position in board[1]:
                if same_position((line, column), position):
                    cell = '1'
            print(cell, end=" | ")
        print('\n   -----------------')
        print('', end='')
    print("     ", end='')
    for line in range(4):
        print(line, "  ", end='')
    print("\n")


def test_minimax():
    """
    Shows Minimax algorithm functionality.
    """
    state = initial_state()
    print_board(state[0])
    while not is_final(state):
        board, turn = state
        if turn == 0:
            state = make_move(state, minimax(state, 4, True)[0])
            print_board(state[0])
        else:
            move = input("Enter move like (1, 3) -> (1, 2): ").split("->")
            current_position = move[0].replace('(', '').replace(')', '').split(',')
            next_position = move[1].replace('(', '').replace(')', '').split(',')
            current_position = int(current_position[0]), int(current_position[1])
            next_position = int(next_position[0]), int(next_position[1])
            move = current_position, next_position
            if move_is_valid(state, move):
                state = make_move(state, move)
                print_board(state[0])
            else:
                print("Invalid move.")
    winner = "the human player."
    if state[1] == 1:
        winner = "the computer."
    print("The winner is", winner)


def test_minimax_alfa_beta_pruning():
    """
    Shows Minimax with Alfa-Beta Pruning algorithm functionality.
    """
    state = initial_state()
    print_board(state[0])
    while not is_final(state):
        board, turn = state
        if turn == 0:
            state = make_move(state, minimax_alfa_beta_pruning(state, 2)[0])
            print_board(state[0])
        else:
            move = input("Enter move like (1, 3) -> (1, 2): ").split("->")
            current_position = move[0].replace('(', '').replace(')', '').split(',')
            next_position = move[1].replace('(', '').replace(')', '').split(',')
            current_position = int(current_position[0]), int(current_position[1])
            next_position = int(next_position[0]), int(next_position[1])
            move = current_position, next_position
            if move_is_valid(state, move):
                state = make_move(state, move)
                print_board(state[0])
            else:
                print("Invalid move.")
    winner = "the human player."
    if state[1] == 1:
        winner = "the computer."
    print("The winner is", winner)


def main():
    """
    Shows functionality of program.
    """
    test_minimax()
    test_minimax_alfa_beta_pruning()


if __name__ == '__main__':
    main()
