package game.ap.player;

import game.ap.Game;
import game.ap.Token;
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
        System.out.println(board);
        System.out.println(this);
        Scanner scanner = new Scanner(System.in);
        Token token;
        while (true) {
            System.out.print("Insert a token: ");
            int number = Input.readNumber(scanner, "Insert a valid token.");
            token = board.getTokenByNumber(number);
            if (token == null) {
                System.out.println("Insert a valid token.");
            }
            else {
                break;
            }
        }
        tokens.add(token);
        board.removeToken(token);
        computeLongestArithmeticProgression();
        checkScore();
    }

    @Override
    public String toString() {
        return name + " (manual player)" + super.toString();
    }
}
