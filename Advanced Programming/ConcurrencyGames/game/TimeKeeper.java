package game;

public class TimeKeeper implements Runnable {
    private Game game;
    private int runningTime;

    public TimeKeeper(Game game, int runningTime) {
        this.game = game;
        this.runningTime = runningTime;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(runningTime * 1000);
        } catch (InterruptedException exception) {
            return;
        }
        game.stop();
    }
}
