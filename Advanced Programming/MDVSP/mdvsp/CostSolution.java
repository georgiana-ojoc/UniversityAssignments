package mdvsp;

import java.util.*;

/**
 * @author Georgiana Ojoc
 */
public class CostSolution {
    private Depot[] depots;
    private Client[] clients;
    private int[][] costMatrix;
    private List<Tour> tours;

    /**
     * The cost matrix contains random values from depots to clients and from clients to clients.
     */
    private void generateCostMatrix() {
        Random random = new Random();
        for (int i = 0; i < costMatrix.length; ++i) {
            for (int j = 0; j < costMatrix[i].length; ++j) {
                do {
                    costMatrix[i][j] = random.nextInt(10) - 1;
                } while (costMatrix[i][j] == 0);
            }
        }
    }

    public CostSolution(Problem problem) {
        depots = problem.getDepots();
        clients = problem.getClients();
        Arrays.sort(clients, Comparator.comparingInt(Client::getOrder));
        costMatrix = new int[depots.length + clients.length][clients.length];
        generateCostMatrix();
        tours = new LinkedList<>();
    }

    public List<Tour> getTours() { return tours; }

    private void topologicalSort(int node, boolean[] visited, List<Integer> nodes) {
        nodes.add(node);
        visited[node] = true;
        for (int i = 0; i < costMatrix[node].length; ++i) {
            if (!visited[i] && costMatrix[node][i] > 0) {
                topologicalSort(i, visited, nodes);
            }
        }
    }

    public void setPath(int node, int parentNode, int[] parent, Tour tour) {
        if (parent[node] == parentNode) {
            return;
        }
        setPath(parent[node], parentNode, parent, tour);
        tour.addClients(clients[parent[node]]);
    }

    /**
     * The vertices are processed in topologically order.
     */
    public void dijkstra() {
        for (int start = 0; start < depots.length; ++start) {
            boolean[] visited = new boolean[clients.length];
            int[] parent = new int[clients.length];
            List<Integer> nodes = new LinkedList<>();
            for (int i = 0; i < visited.length; ++i) {
                parent[i] = -(start + 1);
                if (!visited[i]) {
                    topologicalSort(i, visited, nodes);
                }
            }
            int[] cost = new int[clients.length];
            for (int i = 0; i < clients.length; ++i) {
                if (costMatrix[start][i] != -1) {
                    cost[i] = costMatrix[start][i];
                }
                else {
                    cost[i] = Integer.MAX_VALUE;
                }
            }
            int nodesNumber = nodes.size();
            for (int node = 0; node < nodesNumber; ++node) {
                if (cost[node] != Integer.MAX_VALUE) {
                    for (int i = 0; i < costMatrix[depots.length + node].length; i++) {
                        if (costMatrix[depots.length + node][i] > 0) {
                            if (cost[i] > cost[node] + costMatrix[depots.length + node][i]) {
                                cost[i] = cost[node] + costMatrix[depots.length + node][i];
                                parent[i] = node;
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < clients.length; ++i) {
                Tour tour = new Tour(depots[start]);
                setPath(i, -(start + 1), parent, tour);
                tour.addClients(clients[i]);
                tour.setCost(cost[i]);
                if (tours.size() == clients.length) {
                    if (tours.get(i).getCost() > tour.getCost()) {
                        tours.remove(i);
                        tours.add(tour);
                    }
                }
                else {
                    tours.add(tour);
                }
            }
        }
    }

    public void printCostMatrix() {
        for (int i = 0; i < depots.length; ++i) {
            System.out.println(depots[i].getName() + ": " + Arrays.toString(costMatrix[i]));
        }
        for (int i = 0; i < clients.length; ++i) {
            System.out.println(clients[i].getName() + ": " + Arrays.toString(costMatrix[depots.length + i]));
        }
    }

    public void print() {
        for (Tour tour : tours) {
            System.out.println(tour);
        }
    }
}
