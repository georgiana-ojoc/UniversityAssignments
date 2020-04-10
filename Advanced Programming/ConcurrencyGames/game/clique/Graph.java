package game.clique;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Graph {
    private int nodeNumber;
    private List<Edge> edges;

    public Graph(int nodeNumber) {
        this.nodeNumber = nodeNumber;
        edges = new ArrayList<>();
        for (int source = 1; source < nodeNumber; ++source) {
            for (int target = source + 1; target <= nodeNumber; ++target) {
                edges.add(new Edge(source, target));
            }
        }
    }

    public int getNodeNumber() { return nodeNumber; }

    public synchronized Edge getEdgeByNodes(int source, int target) {
        int index = getIndexByNodes(source, target);
        if (index != -1) {
            return edges.get(index);
        }
        return null;
    }

    public synchronized void removeEdge(Edge edge) {
        int index = getIndexByNodes(edge.getSource(), edge.getTarget());
        if (index != -1) {
            edges.remove(index);
        }
    }

    public synchronized Edge getFirstEdge() {
        return edges.get(0);
    }

    public synchronized Edge getRandomEdge() {
        Random random = new Random();
        return edges.get(random.nextInt(edges.size()));
    }

    public synchronized int getEdgeNumber() {
        return edges.size();
    }

    /**
     * Uses binary search because the edges are sorted in ascending number by source and target.
     */
    private int getIndexByNodes(int source, int target) {
        if (source > target) {
            source += target;
            target = source - target;
            source -= target;
        }
        int left = 0;
        int right = edges.size() - 1;
        while (left <= right) {
            int middle = (left + right) / 2;
            Edge middleEdge = edges.get(middle);
            if (middleEdge.getSource() == source) {
                if (middleEdge.getTarget() == target) {
                    return middle;
                }
                if (middleEdge.getTarget() > target) {
                    right = middle - 1;
                }
                else {
                    left = middle + 1;
                }
            }
            else if (middleEdge.getSource() > source) {
                right = middle - 1;
            }
            else {
                left = middle + 1;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return "\nGraph: " + edges.stream().map(Edge::toString).collect(Collectors.joining(", "));
    }
}
