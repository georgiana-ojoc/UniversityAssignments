package knapsack;

import java.util.Objects;

public class WeightedVertex {
    private int item;
    private int weight;

    public WeightedVertex(int item, int weight) {
        this.item = item;
        this.weight = weight;
    }

    public int getItem() { return item; }

    public void setItem(int item) { this.item = item; }

    public int getWeight() { return weight; }

    public void setWeight(int weight) { this.weight = weight; }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (!(object instanceof WeightedVertex)) { return false; }
        WeightedVertex weightedVertex = (WeightedVertex) object;
        return getItem() == weightedVertex.getItem() && getWeight() == weightedVertex.getWeight();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItem(), getWeight());
    }

    @Override
    public String toString() {
        return item + " (" + weight + ')';
    }
}
