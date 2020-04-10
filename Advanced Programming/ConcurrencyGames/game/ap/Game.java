package game.ap;

import game.Strategy;
import game.ap.player.ManualPlayer;
import game.ap.player.RandomPlayer;
import game.ap.player.SmartPlayer;

import java.util.ArrayList;

public class Game extends game.Game {
    private Board board;

    public Game(int tokenNumber, int size, int runningTime) {
        super(size, runningTime);
        board = new Board(tokenNumber);
        players = new ArrayList<>();
    }

    public Board getBoard() {
        return board;
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
        System.out.println("The arithmetic progression game started.");
        super.start();
        System.out.println("\nThe arithmetic progression game ended.");
    }

    @Override
    public boolean ended() {
        return board.getTokenNumber() == 0;
    }

    @Override
    public void stop() {
        super.stop();
        System.out.println("\nThe arithmetic progression game exceeded the time limit.");
    }
}
