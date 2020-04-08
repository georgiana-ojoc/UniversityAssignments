package utility;

/**
 * @author Georgiana Ojoc
 */
public class Pair<U, V> {
    public final U first;
    public final V second;
    
    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
            return true;

        if (object == null || getClass() != object.getClass())
            return false;

        Pair<?, ?> pair = (Pair<?, ?>) object;

        if (!first.equals(pair.first))
            return false;

        return second.equals(pair.second);
    }

    @Override
    public int hashCode() {
        return 31 * first.hashCode() + second.hashCode();
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
