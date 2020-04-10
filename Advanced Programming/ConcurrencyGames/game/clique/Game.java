package game.clique;

import game.Strategy;
import game.clique.player.ManualPlayer;
import game.clique.player.RandomPlayer;
import game.clique.player.SmartPlayer;

import java.util.ArrayList;

public class Game extends game.Game {
    private Graph graph;

    public Game(int nodeNumber, int size, int runningTime) {
        super(size, runningTime);
        graph = new Graph(nodeNumber);
        players = new ArrayList<>();
    }

    public Graph getGraph() {
        return graph;
    }

    @Override
    public void addPlayer(String name, Strategy strategy) {
        switch (strategy) {
            case Manual: players.add(new ManualPlayer(this, name)); break;
            case Random: players.add(new RandomPlayer(this, name)); break;
            case Smart: players.add(new SmartPlayer(this, name));
        }
    }

    @Override
    public void start() {
        System.out.println("The clique game started.");
        super.start();
        System.out.println("\nThe clique game ended.");
    }

    @Override
    public boolean ended() {
        return graph.getEdgeNumber() == 0;
    }

    @Override
    public void stop() {
        super.stop();
        System.out.println("\nThe clique game exceeded the time limit.");
    }
}
