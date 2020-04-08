package knapsack;

import java.util.List;

public interface Algorithm {
    public List<Item> getAvailableItems();
    public Knapsack getKnapsack();
    public void solve();
}
