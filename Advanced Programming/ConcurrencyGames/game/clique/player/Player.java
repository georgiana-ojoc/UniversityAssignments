package game.clique.player;

import game.clique.Edge;
import game.clique.Game;
import game.clique.Graph;

import org.jgrapht.alg.clique.ChordalGraphMaxCliqueFinder;
import org.jgrapht.alg.interfaces.CliqueAlgorithm.Clique;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Player extends game.Player {
    protected Game game;
    protected Graph graph;
    protected List<Edge> edges;
    protected Clique<Integer> clique;

    public Player(Game game, String name) {
        super(name);
        this.game = game;
        graph = game.getGraph();
        edges = new ArrayList<>();
        clique = null;
    }

    @Override
    protected int getScore() {
        return clique.size();
    }

    protected void computeMaximumClique() {
        org.jgrapht.Graph<Integer, Edge> jGraph = new DefaultUndirectedGraph<Integer, Edge>(Edge.class);
        for (Edge edge : edges) {
            int source = edge.getSource();
            int target = edge.getTarget();
            if (!jGraph.containsVertex(source)) {
                jGraph.addVertex(source);
            }
            if (!jGraph.containsVertex(target)) {
                jGraph.addVertex(target);
            }
            jGraph.addEdge(source, target, new Edge(source, target));
        }
        ChordalGraphMaxCliqueFinder<Integer, Edge> chordalGraphMaxCliqueFinder =
                new ChordalGraphMaxCliqueFinder<>(jGraph);
        clique = chordalGraphMaxCliqueFinder.getClique();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("\nEdges: ");
        result.append(edges.stream().map(Edge::toString).collect(Collectors.joining(", ")));
        result.append("\nClique: ");
        for (int source = 1; source < graph.getNodeNumber(); ++source) {
            for (int target = source + 1; target <= graph.getNodeNumber(); ++target) {
                if (clique.contains(source) && clique.contains(target)) {
                    result.append('(').append(source).append(", ").append(target).append("), ");
                }
            }
        }
        result.deleteCharAt(result.length() - 1);
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }
}
