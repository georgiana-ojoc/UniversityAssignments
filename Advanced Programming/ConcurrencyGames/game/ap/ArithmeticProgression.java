package game.ap;

public class ArithmeticProgression {
    private int firstTerm;
    private int commonDifference;
    private int size;

    public ArithmeticProgression(int firstTerm, int commonDifference, int size) {
        setArithmeticProgression(firstTerm, commonDifference, size);
    }

    public int getFirstTerm() {
        return firstTerm;
    }

    public int getCommonDifference() {
        return commonDifference;
    }

    public int getSize() {
        return size;
    }

    public void setArithmeticProgression(int firstTerm, int commonDifference, int size) {
        this.firstTerm = firstTerm;
        this.commonDifference = commonDifference;
        this.size = size;
    }
}
