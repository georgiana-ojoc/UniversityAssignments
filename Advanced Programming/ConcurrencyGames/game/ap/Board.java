package game.ap;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Board {
    private List<Token> tokens;

    public Board(int tokenNumber) {
        tokens = IntStream.range(1, tokenNumber + 1).mapToObj(Token::new).collect(Collectors.toList());
    }

    public synchronized Token getTokenByNumber(int number) {
        int index = getIndexByNumber(number);
        if (index != -1) {
            return tokens.get(index);
        }
        return null;
    }

    public synchronized Token getFirstToken() {
        return tokens.get(0);
    }

    public synchronized Token getRandomToken() {
        Random random = new Random();
        return tokens.get(random.nextInt(tokens.size()));
    }

    public synchronized void removeToken(Token token) {
        int index = getIndexByNumber(token.getNumber());
        if (index != -1) {
            tokens.remove(index);
        }
    }

    public synchronized int getTokenNumber() {
        return tokens.size();
    }

    /**
     * Uses binary search because the tokens are ordered in ascending order.
     */
    private int getIndexByNumber(int number) {
        int left = 0;
        int right = tokens.size() - 1;
        while (left <= right) {
            int middle = (left + right) / 2;
            Token middleToken = tokens.get(middle);
            if (middleToken.getNumber() == number) {
                return middle;
            }
            if (middleToken.getNumber() > number) {
                right = middle - 1;
            }
            else {
                left = middle + 1;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return "\nBoard: " + tokens.stream().map(Token::toString).collect(Collectors.joining(", "));
    }
}
