package game.clique.player;

import game.clique.Edge;
import game.clique.Game;

public class SmartPlayer extends Player {
    public SmartPlayer(Game game, String name) {
        super(game, name);
    }

    @Override
    protected void playTurn() {
        if (game.ended()) {
            return;
        }
        Edge edge = graph.getFirstEdge();
        edges.add(edge);
        graph.removeEdge(edge);
        computeMaximumClique();
        System.out.println(graph);
        System.out.println(this);
        checkScore();
    }


    @Override
    public String toString() {
        return name + " (smart player)" + super.toString();
    }
}
