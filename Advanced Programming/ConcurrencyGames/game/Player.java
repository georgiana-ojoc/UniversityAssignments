package game;

public abstract class Player implements Runnable {
    protected String name;

    public Player (String name) {
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            waitTurn();
            if (Game.isRunning()) {
                playTurn();
                endTurn();
            }
            else {
                endTurn();
                break;
            }
        }
    }

    private synchronized void waitTurn() {
        try {
            wait();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    private synchronized void endTurn() {
        notify();
    }

    protected abstract void playTurn();

    protected void checkScore() {
        if (getScore() == Game.getSize()) {
            Game.setWinner(this);
        }
    }

    protected abstract int getScore();
}
