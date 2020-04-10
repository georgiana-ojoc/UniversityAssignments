package game.ap.player;

import game.ap.Game;
import game.ap.Token;

public class RandomPlayer extends Player {
    public RandomPlayer(Game game, String name) {
        super(game, name);
    }

    @Override
    protected void playTurn() {
        if (game.ended()) {
            return;
        }
        Token token = board.getRandomToken();
        tokens.add(token);
        board.removeToken(token);
        computeLongestArithmeticProgression();
        System.out.println(board);
        System.out.println(this);
        checkScore();
    }

    @Override
    public String toString() {
        return name + " (random player)" + super.toString();
    }
}
