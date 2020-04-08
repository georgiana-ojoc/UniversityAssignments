package knapsack;

public class Book implements Item {
    private String identifier;
    private String name;
    private int value;
    private int weight;
    private int pageNumber;

    public Book(String identifier, String name, int value, int pageNumber) {
        this.identifier = identifier;
        this.name = name;
        this.value = value;
        this.pageNumber = pageNumber;
        this.weight = pageNumber / 100;
    }

    @Override
    public String getIdentifier() { return identifier; }

    public void setIdentifier(String identifier) { this.identifier = identifier; }

    @Override
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @Override
    public int getValue() { return value; }

    public void setValue(int value) { this.value = value; }

    @Override
    public int getWeight() { return weight; }

    public int getPageNumber() { return pageNumber; }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        this.weight = pageNumber / 100;
    }

    @Override
    public String toString() {
        return identifier + ": name = " + name + ", value = " + value + ", weight = " + weight +
                " (profit factor = " + String.format("%.2f)", getProfitFactor());
    }
}
