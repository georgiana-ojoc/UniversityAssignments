package game;

import java.util.*;

import static java.util.stream.Collectors.toMap;

public abstract class Game {
    protected List<Player> players;
    protected TimeKeeper timeKeeper;
    protected TurnManager turnManager;
    protected static int size;
    protected static boolean run = false;
    protected static Player winner = null;

    public Game(int size, int runningTime) {
        Game.size = size;
        timeKeeper = new TimeKeeper(this, runningTime);
    }

    public static int getSize() { return size; }

    public abstract void addPlayer(String name, Strategy strategy);

    public void start() {
        run = true;
        Thread timeKeeperThread = new Thread(timeKeeper);
        timeKeeperThread.start();
        turnManager = new TurnManager(players);
        Thread turnManagerThread = new Thread(turnManager);
        turnManagerThread.start();
        while (isRunning()) {
            if (existWinner()) {
                run = false;
            }
            else if (ended()) {
                run = false;
            }
        }
        if (timeKeeperThread.isAlive()) {
            timeKeeperThread.interrupt();
        }
    }

    protected static boolean isRunning() {
        return run;
    }

    public abstract boolean ended();

    private boolean existWinner() {
        return winner != null;
    }

    public static void setWinner(Player winner) {
        Game.winner = winner;
    }

    protected void stop() {
        run = false;
    }

    public void showRanking() {
        if (winner != null) {
            System.out.println("\nWinner: " + winner + '\n');
            return;
        }
        System.out.println("\nRanking:");
        Map<Player, Integer> ranking = new HashMap<>();
        for (Player player : players) {
            ranking.put(player, player.getScore());
        }
        Map<Player, Integer> sortedRanking = ranking.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (entry1, entry2) -> entry2,
                        LinkedHashMap::new));
        int index = 0;
        for (Player player : sortedRanking.keySet()) {
            System.out.print(++index + ". " + ranking.get(player) + " points: ");
            System.out.println(player);
            System.out.println();
        }
    }
}
