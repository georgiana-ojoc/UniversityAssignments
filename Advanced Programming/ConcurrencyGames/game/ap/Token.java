package game.ap;

import java.util.Objects;

public class Token implements Comparable {
    private int number;

    public Token(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Token)) {
            return false;
        }
        Token token = (Token) object;
        return getNumber() == token.getNumber();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber());
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }

    @Override
    public int compareTo(Object object) {
        if (this == object) {
            return 0;
        }
        if (!(object instanceof Token)) {
            return 1;
        }
        Token token = (Token) object;
        return Integer.compare(number, token.getNumber());
    }
}
