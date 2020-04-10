package game.ap.player;

import game.ap.ArithmeticProgression;
import game.ap.Board;
import game.ap.Game;
import game.ap.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Player extends game.Player {
    protected Game game;
    protected Board board;
    protected List<Token> tokens;
    protected ArithmeticProgression longestArithmeticProgression;

    public Player(Game game, String name) {
        super(name);
        this.game = game;
        board = game.getBoard();
        tokens = new ArrayList<>();
        longestArithmeticProgression = new ArithmeticProgression(0, -1, 0);
    }

    @Override
    protected int getScore() {
        return longestArithmeticProgression.getSize();
    }

    protected void computeLongestArithmeticProgression() {
        int size = tokens.size();
        if (size == 0) {
            return;
        }
        if (size == 1) {
            longestArithmeticProgression.setArithmeticProgression
                    (tokens.get(0).getNumber(), 0, 1);
            return;
        }
        Collections.sort(tokens);
        if (size == 2) {
            longestArithmeticProgression.setArithmeticProgression(tokens.get(0).getNumber(),
                    tokens.get(1).getNumber() - tokens.get(0).getNumber(), 2);
            return;
        }
        int firstItem = tokens.get(0).getNumber();
        int bestCommonDifference = tokens.get(1).getNumber() - tokens.get(0).getNumber();
        int maximumSize = 2;
        for (int first = 0; first < size - 1; ++first) {
            for (int second = first + 1; second < size; ++second) {
                int currentCommonDifference = tokens.get(second).getNumber() - tokens.get(first).getNumber();
                int nextPossibleTerm = tokens.get(second).getNumber() + currentCommonDifference;
                int currentSize = 2;
                for (int next = second + 1; next < size; ++next) {
                    if (tokens.get(next).getNumber() == nextPossibleTerm) {
                        ++currentSize;
                        nextPossibleTerm += currentCommonDifference;
                    }
                }
                if (currentSize > maximumSize) {
                    firstItem = tokens.get(first).getNumber();
                    bestCommonDifference = currentCommonDifference;
                    maximumSize = currentSize;
                }
            }
        }
        longestArithmeticProgression.setArithmeticProgression(firstItem, bestCommonDifference, maximumSize);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("\nTokens: ");
        result.append(tokens.stream().map(Token::toString).collect(Collectors.joining(", ")));
        result.append("\nLongest arithmetic progression: ");
        int term = longestArithmeticProgression.getFirstTerm();
        int commonDifference = longestArithmeticProgression.getCommonDifference();
        int size = longestArithmeticProgression.getSize();
        for (int index = 1; index <= size; ++index) {
            if (index > 1) {
                result.append(", ");
            }
            result.append(term);
            term += commonDifference;
        }
        return result.toString();
    }
}
