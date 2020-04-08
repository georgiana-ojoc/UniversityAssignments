package knapsack;

import java.util.ArrayList;
import java.util.Random;

public class KnapsackGenerator extends Knapsack {
    private Random random;
    int minCapacity;
    int maxCapacity;

    public KnapsackGenerator(int minCapacity, int maxCapacity) {
        random = new Random();
        this.minCapacity = minCapacity;
        this.maxCapacity = maxCapacity;
        value = 0;
        weight = 0;
        selectedItems = new ArrayList<>();
    }

    public int getMinCapacity() { return minCapacity; }

    public void setMinCapacity(int minCapacity) { this.minCapacity = minCapacity; }

    public int getMaxCapacity() { return maxCapacity; }

    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public void generate() {
        capacity = random.nextInt(maxCapacity - minCapacity) + minCapacity + 1;
    }
}
