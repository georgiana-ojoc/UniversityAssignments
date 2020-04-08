package knapsack;

import java.util.List;
import java.util.ArrayList;

public class Knapsack {
    protected int capacity;
    protected int value;
    protected int weight;
    protected List<Item> selectedItems;

    public Knapsack() {
        this.capacity = 0;
        value = 0;
        weight = 0;
        selectedItems = new ArrayList<>();
    }

    public Knapsack(int capacity) {
        this.capacity = capacity;
        value = 0;
        weight = 0;
        selectedItems = new ArrayList<>();
    }

    public Knapsack(Knapsack knapsack) {
        capacity = knapsack.getCapacity();
        value = 0;
        weight = 0;
        selectedItems = new ArrayList<>();
    }

    public int getCapacity() { return capacity; }

    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getValue() { return value; }

    public int getWeight() { return weight; }

    public List<Item> getSelectedItems() { return selectedItems; }

    public void selectItem(Item item) {
        if (weight + item.getWeight() <= capacity) {
            selectedItems.add(item);
            value += item.getValue();
            weight += item.getWeight();
        }
    }

    @Override
    public String toString() {
        return "Knapsack:\ncapacity = " + capacity + '\n';
    }
}
