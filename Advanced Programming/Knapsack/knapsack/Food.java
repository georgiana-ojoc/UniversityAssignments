package knapsack;

public class Food implements Item {
    private String identifier;
    private String name;
    private int value;
    private int weight;

    public Food(String identifier, String name, int weight) {
        this.identifier = identifier;
        this.name = name;
        this.value = weight * 2;
        this.weight = weight;
    }

    @Override
    public String getIdentifier() { return identifier; }

    public void setIdentifier(String identifier) { this.identifier = identifier; }

    @Override
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @Override
    public int getValue() { return value; }

    @Override
    public int getWeight() { return weight; }

    public void setWeight(int weight) {
        this.value = weight / 2;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return identifier + ": name = " + name + ", value = " + value + ", weight = " + weight +
                " (profit factor = " + String.format("%.2f)", getProfitFactor());
    }
}
