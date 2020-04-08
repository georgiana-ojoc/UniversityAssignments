package words;

import utility.Efficiency;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Georgiana Ojoc
 */
public class ConnectedWordGraph extends words.WordGraph {
    private int nodesNo;
    private List<Integer>[] adjListArray;

    public ConnectedWordGraph(String[] words, Efficiency efficiency) {
        super(words, efficiency);

        adjListArray = new LinkedList[size];
        for (int i = 0; i < size; ++i) {
            adjListArray[i] = new LinkedList<>();
        }
    }

    public void setListArray() {
        for (int i = 0; i < size - 1; ++i) {
            for (int j = i + 1; j < size; ++j) {
                if (adjMatrix[i][j]) {
                    adjListArray[i].add(j);
                    adjListArray[j].add(i);
                }
            }
        }
    }

    private void DFS(boolean[] visited, int node) {
        visited[node] = true;
        System.out.print(node + 1 + ": " + words[node] + ' ');
        for (int neighbor : adjListArray[node]) {
            if (!visited[neighbor]) {
                ++nodesNo;
                DFS(visited, neighbor);
            }
        }
    }

    public void connectedComponents() {
        int componentsNo = 0;
        boolean[] visited = new boolean[size];
        for (int node = 0; node < size; ++node) {
            if (!visited[node]) {
                nodesNo = 1;
                DFS(visited, node);
                ++componentsNo;
                System.out.println();
                System.out.println("Number of nodes: " + nodesNo);
                System.out.println();
            }
        }

        if (componentsNo == 1) {
            System.out.println("Graph is connected.");
            System.out.println();
        }
        else {
            System.out.println("Number of connected components: " + componentsNo);
            System.out.println();
        }
    }
}
