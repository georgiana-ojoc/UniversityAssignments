package game.clique.player;

import game.clique.Game;
import game.clique.Edge;

public class RandomPlayer extends Player {
    public RandomPlayer(Game game, String name) {
        super(game, name);
    }

    @Override
    protected void playTurn() {
        if (game.ended()) {
            return;
        }
        Edge edge = graph.getRandomEdge();
        edges.add(edge);
        graph.removeEdge(edge);
        computeMaximumClique();
        System.out.println(graph);
        System.out.println(this);
        checkScore();
    }

    @Override
    public String toString() {
        return name + " (random player)" + super.toString();
    }
}
