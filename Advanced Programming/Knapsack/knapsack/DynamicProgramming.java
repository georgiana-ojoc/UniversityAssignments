package knapsack;

import java.util.List;
import java.util.ArrayList;

public class DynamicProgramming implements Algorithm {
    private List<Item> availableItems;
    private Knapsack knapsack;

    public DynamicProgramming(Problem problem, Knapsack knapsack) {
        availableItems = new ArrayList<>(problem.getAvailableItems());
        this.knapsack = new Knapsack(knapsack);
    }

    @Override
    public List<Item> getAvailableItems() { return availableItems; }

    public void setProblem(Problem problem) {
        availableItems = new ArrayList<>(problem.getAvailableItems());
    }

    @Override
    public Knapsack getKnapsack() { return knapsack; }

    public void setKnapsack(Knapsack knapsack) {
        this.knapsack = new Knapsack(knapsack);
    }

    @Override
    public void solve() {
        int[][] values = new int[availableItems.size() + 1][knapsack.getCapacity() + 1];
        for (int item = 0; item < values.length; ++item) {
            for (int weight = 0; weight < values[0].length; ++weight) {
                if (item == 0 || weight == 0) {
                    values[item][weight] = 0;
                }
                else {
                    Item previousItem = availableItems.get(item - 1);
                    if (previousItem.getWeight() <= weight) {
                        values[item][weight] = Integer.max(previousItem.getValue()
                                + values[item - 1][weight - previousItem.getWeight()],
                                values[item - 1][weight]);
                    }
                    else {
                        values[item][weight] = values[item - 1][weight];
                    }
                }
            }
        }

        int value = values[availableItems.size()][knapsack.getCapacity()];
        int weight = knapsack.getCapacity();
        for (int item = availableItems.size(); item > 0 && value > 0; --item) {
            if (value != values[item - 1][weight]) {
                Item previousItem = availableItems.get(item - 1);
                knapsack.selectItem(previousItem);
                value -= previousItem.getValue();
                weight -= previousItem.getWeight();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Dynamic programming solution:\nvalue = ").append(knapsack.getValue())
                .append(", weight = ").append(knapsack.getWeight()).append("\nselected items:\n");
        List<Item> selectedItems = knapsack.getSelectedItems();
        if (selectedItems != null) {
            for (Item item : selectedItems) {
                result.append(item.getIdentifier()).append('\n');
            }
        }
        return result.toString();
    }
}
