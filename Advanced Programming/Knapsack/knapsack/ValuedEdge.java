package knapsack;

public class ValuedEdge {
    private WeightedVertex source;
    private WeightedVertex target;
    private int value;

    public ValuedEdge(WeightedVertex source, WeightedVertex target, int value) {
        this.source = source;
        this.target = target;
        this.value = value;
    }

    public WeightedVertex getSource() { return source; }

    public void setSource(WeightedVertex source) { this.source = source; }

    public WeightedVertex getTarget() { return target; }

    public void setTarget(WeightedVertex target) { this.target = target; }

    public int getValue() { return value; }

    public void setValue(int value) { this.value = value; }

    @Override
    public String toString() {
        return source + " : " + target + " (" + value + ')';
    }
}

