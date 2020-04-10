package game.clique.player;

import game.clique.Edge;
import game.clique.Game;
import input.Input;

import java.util.Scanner;

public class ManualPlayer extends Player {
    public ManualPlayer(Game game, String name) {
        super(game, name);
    }

    @Override
    protected void playTurn() {
        if (game.ended()) {
            return;
        }
        System.out.println(graph);
        System.out.println(this);
        Scanner scanner = new Scanner(System.in);
        Edge edge;
        while (true) {
            System.out.print("Insert a source: ");
            int source = Input.readNumber(scanner, "Insert a valid source.");
            System.out.println("Insert a target: ");
            int target = Input.readNumber(scanner, "Insert a valid target.");
            edge = graph.getEdgeByNodes(source, target);
            if (edge == null) {
                System.out.println("Insert valid source and target.");
            }
            else {
                break;
            }
        }
        edges.add(edge);
        graph.removeEdge(edge);
        computeMaximumClique();
        checkScore();
    }

    @Override
    public String toString() {
        return name + " (manual player)" + super.toString();
    }
}
