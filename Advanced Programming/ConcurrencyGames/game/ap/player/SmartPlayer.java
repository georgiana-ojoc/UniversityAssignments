package game.ap.player;

import game.ap.Game;
import game.ap.Token;

import java.util.Collections;

public class SmartPlayer extends Player {
    public SmartPlayer(Game game, String name) {
        super(game, name);
    }

    @Override
    protected void playTurn() {
        if (game.ended()) {
            return;
        }
        Token token;
        if (longestArithmeticProgression.getFirstTerm() < 2) {
            token = board.getFirstToken();
        }
        else {
            token = getNextToken();
        }
        tokens.add(token);
        board.removeToken(token);
        computeLongestArithmeticProgression();
        System.out.println(board);
        System.out.println(this);
        checkScore();
    }

    private Token getNextToken() {
        Collections.sort(tokens);
        int size = tokens.size();
        int maximumPossibleSize = 2;
        Token nextToken = board.getFirstToken();
        Token possibleToken;
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
                possibleToken = board.getTokenByNumber(nextPossibleTerm);
                if (possibleToken == null) {
                    if (currentSize > maximumPossibleSize) {
                        maximumPossibleSize = currentSize;
                    }
                }
                else {
                    if (currentSize > maximumPossibleSize) {
                        nextToken = possibleToken;
                        maximumPossibleSize = currentSize;
                    }
                }
            }
        }
        return nextToken;
    }

    @Override
    public String toString() {
        return name + " (smart player)" + super.toString();
    }
}
