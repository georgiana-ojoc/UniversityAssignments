package knapsack;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class LongestPath implements Algorithm {
    private List<Item> availableItems;
    private Knapsack knapsack;
    private Graph<WeightedVertex, ValuedEdge> graph;
    private List<List<WeightedVertex>> vertexSet;
    private WeightedVertex source;
    private WeightedVertex target;

    public LongestPath(Problem problem, Knapsack knapsack) {
        availableItems = new ArrayList<>(problem.getAvailableItems());
        this.knapsack = new Knapsack(knapsack);
        graph = new DefaultDirectedWeightedGraph<WeightedVertex, ValuedEdge>(ValuedEdge.class);
        vertexSet = new ArrayList<>();
        source = new WeightedVertex(-1, 0);
        target = new WeightedVertex(-2, 0);
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

    public void createGraph() {
        addVertexes();
        addEdges();
    }

    private void addVertexes() {
        Set<WeightedVertex> vertexes = new HashSet<>(graph.vertexSet());
        graph.removeAllVertices(vertexes);

        graph.addVertex(source);

        vertexSet.clear();

        /**
         * Add vertex for each item weight.
         */
        int size = availableItems.size();
        int capacity = knapsack.getCapacity();
        for (int item = 0; item < size; ++item) {
            List<WeightedVertex> vertexList = new ArrayList<>();
            for (int weight = 0; weight <= capacity; ++weight) {
                WeightedVertex vertex = new WeightedVertex(item, weight);
                graph.addVertex(vertex);
                vertexList.add(vertex);
            }
            vertexSet.add(vertexList);
        }

        graph.addVertex(target);
    }

    private void addEdges() {
        Set<ValuedEdge> edges = new HashSet<>(graph.edgeSet());
        graph.removeAllEdges(edges);

        if (availableItems.size() > 0) {
            /**
             * Add edge with value 0 from source to vertex of item 1 with weight 0.
             */
            WeightedVertex vertex = vertexSet.get(0).get(0);
            graph.addEdge(source, vertex, new ValuedEdge(source, vertex, 0));
            /**
            * Add edge with value itemValue from source to vertex of item 1 with weight itemWeight.
            */
            vertex = vertexSet.get(0).get(availableItems.get(0).getWeight());
            graph.addEdge(source, vertex,
                    new ValuedEdge(source, vertex, availableItems.get(0).getValue()));
        }

        int size = availableItems.size();
        int capacity = knapsack.getCapacity();
        /**
         * Add edge with value 0 from item - 1 to item for each weight.
         * Add edge with value itemValue from item - 1 to item for each weight >= itemWeight.
         */
        for (int weight = 0; weight <= capacity; ++weight) {
        for (int item = 1; item < size; ++item) {

                WeightedVertex firstVertex = vertexSet.get(item - 1).get(weight);
                WeightedVertex secondVertex = vertexSet.get(item).get(weight);
                graph.addEdge(firstVertex, secondVertex, new ValuedEdge(firstVertex, secondVertex, 0));

                int currentWeight = availableItems.get(item).getWeight();
                if (currentWeight <= weight) {
                    firstVertex = vertexSet.get(item - 1).get(weight - currentWeight);
                    secondVertex = vertexSet.get(item).get(weight);
                    graph.addEdge(firstVertex, secondVertex,
                            new ValuedEdge(firstVertex, secondVertex, availableItems.get(item).getValue()));
                }
            }
        }

        /**
         * Add edge with value 0 from last item to target for each weight.
         */
        for (int weight = 0; weight <= capacity; ++weight) {
            WeightedVertex vertex = vertexSet.get(size - 1).get(weight);
            graph.addEdge(vertex, target, new ValuedEdge(vertex, target, 0));
        }
    }

    @Override
    public void solve() {

        createGraph();

        Map<WeightedVertex, Integer> value = new HashMap<>();
        for (WeightedVertex vertex : graph.vertexSet()) {
            value.put(vertex, Integer.MIN_VALUE);
        }
        value.put(source, 0);

        Map<WeightedVertex, WeightedVertex> previousItem = new HashMap<>();
        previousItem.put(source, null);

        /**
         * The graph is a directed acyclic graph topologically ordered.
         * Compute the longest path (with maximum value) from source to each vertex.
         */
        for (WeightedVertex vertex : graph.vertexSet()) {
            if (value.get(vertex) != Integer.MIN_VALUE) {
                for (ValuedEdge edge : graph.outgoingEdgesOf(vertex)) {
                    if (value.get(edge.getTarget()) < value.get(vertex) + edge.getValue()) {
                        value.put(edge.getTarget(), value.get(vertex) + edge.getValue());
                        previousItem.put(edge.getTarget(), vertex);
                    }
                }
            }
        }

        /**
         * On the path from the source to the target, if vertex's weight is bigger than his parent's weight,
         * then that item will be selected in the knapsack.
         */
        WeightedVertex vertex = previousItem.get(target);
        if (vertex != null) {
            WeightedVertex previousVertex = previousItem.get(vertex);
            while (previousVertex != null) {
                if (vertex.getWeight() > previousVertex.getWeight()) {
                    knapsack.selectItem(availableItems.get(vertex.getItem()));
                }
                vertex = previousVertex;
                previousVertex = previousItem.get(vertex);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Graph solution:\nvalue = ").append(knapsack.getValue())
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
