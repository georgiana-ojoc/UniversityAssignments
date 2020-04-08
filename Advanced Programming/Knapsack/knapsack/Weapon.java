package knapsack;

public class Weapon implements Item {
    private String identifier;
    private String name;
    private int value;
    private int weight;

    public Weapon(String identifier, WeaponType type, int value, int weight) {
        this.identifier = identifier;
        this.name = type.toString();
        this.value = value;
        this.weight = weight;
    }

    @Override
    public String getIdentifier() { return identifier; }

    public void setIdentifier(String identifier) { this.identifier = identifier; }

    @Override
    public String getName() {
        return name;
    }

    public void setName(WeaponType type) {
        this.name = type.toString();
    }

    @Override
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) { this.weight = weight; }

    @Override
    public String toString() {
        return identifier + ": name = " + name + ", value = " + value + ", weight = " + weight +
                " (profit factor = " + String.format("%.2f)", getProfitFactor());
    }
}
