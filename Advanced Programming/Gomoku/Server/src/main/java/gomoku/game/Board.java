package gomoku.game;

import javafx.util.Pair;

import java.io.PrintWriter;
import java.util.Random;

public class Board {
    private int size;
    private int [][] board;
    private Random random;

    public Board(int size) {
        this.size = size;
        board = new int[size][size];
        for (int row = 0; row < size; ++row) {
            for (int column = 0; column < size; ++column) {
                board[row][column] = 0;
            }
        }
        random = new Random();
    }

    public void printBoard(PrintWriter printWriter) {
        for (int row = 0; row < size; ++row) {
            for (int column = 0; column < size; ++column) {
                printWriter.print(board[row][column]);
                printWriter.print(' ');
            }
            printWriter.println();
        }
        printWriter.flush();
    }

    public Pair<Boolean, String> addPiece(int currentPlayer, int currentRow, int currentColumn) {
        Pair<Boolean, String> result = checkTurn(currentPlayer, currentRow, currentColumn);
        if (!result.getKey()) {
            return result;
        }
        board[currentRow][currentColumn] = currentPlayer;
        return checkContinue(currentPlayer, currentRow, currentColumn);
    }

    public Pair<Boolean, String> addPiece(int currentPlayer) {
        if (isFull()) {
            return new Pair<>(false, "There is no empty cell left.");
        }
        if (currentPlayer != 1 && currentPlayer != 2) {
            return new Pair<>(true, "The currentPlayer is invalid.");
        }
        for (int row = 0; row < size; ++row) {
            for (int column = 0; column < size; ++column) {
                if (board[row][column] != currentPlayer) {
                    continue;
                }
                Pair<Integer, Integer> currentRowStartEnd = getRowStartEnd(currentPlayer, row, column);
                int startColumn = currentRowStartEnd.getKey();
                int endColumn = currentRowStartEnd.getValue();
                if (endColumn - startColumn + 1 >= 4) {
                    int currentColumn = startColumn - 1;
                    if (currentColumn >= 0 && board[row][currentColumn] == 0) {
                        board[row][currentColumn] = currentPlayer;
                        return checkContinue(currentPlayer, row, currentColumn);
                    }
                    currentColumn = endColumn + 1;
                    if (currentColumn < size && board[row][currentColumn] == 0) {
                        board[row][endColumn + 1] = currentPlayer;
                        return checkContinue(currentPlayer, row, currentColumn);
                    }
                }
                Pair<Integer, Integer> currentColumnStartEnd = getColumnStartEnd(currentPlayer, row, column);
                int startRow = currentColumnStartEnd.getKey();
                int endRow = currentColumnStartEnd.getValue();
                if (endRow - startRow + 1 >= 4) {
                    int currentRow = startRow - 1;
                    if (currentRow >= 0 && board[currentRow][column] == 0) {
                        board[currentRow][column] = currentPlayer;
                        return checkContinue(currentPlayer, currentRow, column);
                    }
                    currentRow = endRow + 1;
                    if (currentRow < size && board[currentRow][column] == 0) {
                        board[currentRow][column] = currentPlayer;
                        return checkContinue(currentPlayer, currentRow, column);
                    }
                }
            }
        }
        int opponentPlayer = 3 - currentPlayer;
        for (int row = 0; row < size; ++row) {
            for (int column = 0; column < size; ++column) {
                if (board[row][column] != opponentPlayer) {
                    continue;
                }
                Pair<Integer, Integer> opponentRowStartEnd = getRowStartEnd(opponentPlayer, row, column);
                int startColumn = opponentRowStartEnd.getKey();
                int endColumn = opponentRowStartEnd.getValue();
                if (endColumn - startColumn + 1 >= 2) {
                    int currentColumn = startColumn - 1;
                    if (currentColumn >= 0 && board[row][currentColumn] == 0) {
                        board[row][currentColumn] = currentPlayer;
                        return checkContinue(currentPlayer, row, currentColumn);
                    }
                    currentColumn = endColumn + 1;
                    if (currentColumn < size && board[row][currentColumn] == 0) {
                        board[row][endColumn + 1] = currentPlayer;
                        return checkContinue(currentPlayer, row, currentColumn);
                    }
                }
                Pair<Integer, Integer> opponentColumnStartEnd = getColumnStartEnd(opponentPlayer, row, column);
                int startRow = opponentColumnStartEnd.getKey();
                int endRow = opponentColumnStartEnd.getValue();
                if (endRow - startRow + 1 >= 2) {
                    int currentRow = startRow - 1;
                    if (currentRow >= 0 && board[currentRow][column] == 0) {
                        board[currentRow][column] = currentPlayer;
                        return checkContinue(currentPlayer, currentRow, column);
                    }
                    currentRow = endRow + 1;
                    if (currentRow < size && board[currentRow][column] == 0) {
                        board[currentRow][column] = currentPlayer;
                        return checkContinue(currentPlayer, currentRow, column);
                    }
                }
            }
        }
        for (int row = 0; row < size; ++row) {
            for (int column = 0; column < size; ++column) {
                if (board[row][column] == 0) {
                    continue;
                }
                int currentRow = row - 1;
                if (currentRow >= 0 && board[currentRow][column] == 0) {
                    board[currentRow][column] = currentPlayer;
                    return checkContinue(currentPlayer, currentRow, column);
                }
                currentRow = row + 1;
                if (currentRow < size && board[currentRow][column] == 0) {
                    board[currentRow][column] = currentPlayer;
                    return checkContinue(currentPlayer, currentRow, column);
                }
                int currentColumn = column - 1;
                if (currentColumn >= 0 && board[row][currentColumn] == 0) {
                    board[row][currentColumn] = currentPlayer;
                    return checkContinue(currentPlayer, row, currentColumn);
                }
                currentColumn = column + 1;
                if (currentColumn <size && board[row][currentColumn] == 0) {
                    board[row][currentColumn] = currentPlayer;
                    return checkContinue(currentPlayer, row, currentColumn);
                }
            }
        }
        int randomRow;
        int randomColumn;
        do {
            randomRow = random.nextInt(size);
            randomColumn = random.nextInt(size);
        } while (board[randomRow][randomColumn] > 0);
        board[randomRow][randomColumn] = currentPlayer;
        return checkContinue(currentPlayer, randomRow, randomColumn);
    }

    public boolean isFull() {
        for (int row = 0; row < size; ++row) {
            for (int column = 0; column < size; ++column) {
                if (board[row][column] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private Pair<Boolean, String> checkTurn(int currentPlayer, int currentRow, int currentColumn) {
        if (isFull()) {
            return new Pair<>(false, "There is no empty cell left.");
        }
        if (currentPlayer != 1 && currentPlayer != 2) {
            return new Pair<>(false, "The currentPlayer is invalid.");
        }
        if (currentRow == -1 || currentRow >= size || currentColumn == -1 || currentColumn >= size) {
            return new Pair<>(false,"The selected cell is invalid.");
        }
        if (board[currentRow][currentColumn] > 0) {
            return new Pair<>(false, "The selected cell is occupied.");
        }
        return new Pair<>(true, null);
    }

    private Pair<Boolean, String> checkContinue(int currentPlayer, int currentRow, int currentColumn) {
        Pair<Integer, Integer> rowStartEnd = getRowStartEnd(currentPlayer, currentRow, currentColumn);
        if (rowStartEnd.getValue() - rowStartEnd.getKey() + 1 == 5) {
            return new Pair<>(false, "The piece was placed successfully on position " +
                    currentRow + " - " + currentColumn + " by player " + currentPlayer + ".\n" +
                    "Player " + currentPlayer + " has won the game with five on a row.");
        }
        Pair<Integer, Integer> columnStartEnd = getColumnStartEnd(currentPlayer, currentRow, currentColumn);
        if (columnStartEnd.getValue() - columnStartEnd.getKey() + 1 >= 5) {
            return new Pair<>(false, "The piece was placed successfully on position " +
                    currentRow + " - " + currentColumn + " by player " + currentPlayer + ".\n" +
                    "Player " + currentPlayer + " has won the game with five on a column.");
        }
        return new Pair<>(true, "The piece was placed successfully on position " +
                currentRow + " - " + currentColumn + " by player " + currentPlayer + ".");
    }

    private Pair<Integer, Integer> getRowStartEnd(int currentPlayer, int currentRow, int currentColumn) {
        int startColumn = currentColumn;
        while (startColumn >= 0 && board[currentRow][startColumn] == currentPlayer) {
            --startColumn;
        }
        ++startColumn;
        int endColumn = currentColumn;
        while (endColumn < size && board[currentRow][endColumn] >= currentPlayer) {
            ++endColumn;
        }
        --endColumn;
        return new Pair<>(startColumn, endColumn);
    }

    private Pair<Integer, Integer> getColumnStartEnd(int currentPlayer, int currentRow, int currentColumn) {
        int startRow = currentRow;
        while (startRow >= 0 && board[startRow][currentColumn] == currentPlayer) {
            --startRow;
        }
        ++startRow;
        int endRow = currentRow;
        while (endRow < size && board[endRow][currentColumn] == currentPlayer) {
            ++endRow;
        }
        --endRow;
        return new Pair<>(startRow, endRow);
    }
}
