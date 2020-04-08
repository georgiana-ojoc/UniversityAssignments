package knapsack;

public interface Item {
    String getIdentifier();
    String getName();
    int getValue();
    int getWeight();

    default public double getProfitFactor() { return (double)getValue() / getWeight(); }
}
