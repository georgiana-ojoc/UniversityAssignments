package knapsack;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class Greedy implements Algorithm {
    private List<Item> availableItems;
    private Knapsack knapsack;

    public Greedy(Problem problem, Knapsack knapsack) {
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
        availableItems.sort(Collections.reverseOrder(Comparator.comparing(Item::getProfitFactor)));
        for (Item item : availableItems) {
            knapsack.selectItem(item);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Greedy solution:\nvalue = ").append(knapsack.getValue())
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
