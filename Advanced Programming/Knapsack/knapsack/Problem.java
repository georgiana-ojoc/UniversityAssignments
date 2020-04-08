package knapsack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Problem {
    protected List<Item> availableItems;

    public Problem() { availableItems = new ArrayList<>(); }

    public Problem(List<Item> availableItems) { this.availableItems = availableItems; }

    public List<Item> getAvailableItems() { return availableItems; }

    public void setAvailableItems(List<Item> availableItems) { this.availableItems = availableItems; }

    public void addItems(Item ... items) {
        availableItems.addAll(Arrays.asList(items));
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Problem:\navailable items:\n");
        if (availableItems != null) {
            availableItems.sort(Comparator.comparing(Item::getIdentifier));
            for (Item item : availableItems) {
                result.append(item).append('\n');
            }
        }
        return result.toString();
    }
}
