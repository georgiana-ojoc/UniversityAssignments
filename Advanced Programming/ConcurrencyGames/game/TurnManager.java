package game;

import java.util.List;

public class TurnManager implements Runnable {
    private List<Player> players;
    Thread[] threads;

    public TurnManager(List<Player> players) {
        this.players = players;
        threads = new Thread[players.size()];
    }

    @Override
    public void run() {
        for (int index = 0; index < players.size(); ++index) {
            threads[index] = new Thread(players.get(index));
            threads[index].start();
        }
        try {
            Thread.sleep(100);
        }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        int turn = 0;
        while (Game.isRunning()) {
            synchronized (players.get(turn)) {
                players.get(turn).notify();
                try {
                    players.get(turn).wait();
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
            turn = (turn + 1) % players.size();
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
        endTurns();
    }

    private void endTurns() {
        for (int index = 0; index < players.size(); ++index) {
            synchronized (players.get(index)) {
                players.get(index).notify();
            }
        }
    }
}
