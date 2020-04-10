import game.Strategy;
import input.Input;

import java.util.Scanner;

public class Main {
    private static int getGameType(Scanner scanner) {
        System.out.print("Insert '1' for arithmetic progression game or '2' for clique game: ");
        int gameType;
        while (true) {
            gameType = Input.readNumber(scanner, "Insert a valid game option.");
            if (gameType != 1 && gameType != 2) {
                System.out.println("Insert a valid game option.");
            }
            else {
                break;
            }
        }
        return gameType;
    }

    private static int getTimeLimit(Scanner scanner) {
        System.out.print("Insert the time limit (in seconds): ");
        return Input.readNumber(scanner, "Insert a valid time limit (in seconds).");
    }

    private static int getTokenNumber(Scanner scanner) {
        System.out.print("Insert the number of tokens: ");
        return Input.readNumber(scanner, "Insert a valid number of tokens.");
    }

    private static int getNodeNumber(Scanner scanner) {
        System.out.print("Insert the number of nodes: ");
        return Input.readNumber(scanner, "Insert a valid number of nodes.");
    }

    private static int getSize(Scanner scanner) {
        System.out.print("Insert the size: ");
        return Input.readNumber(scanner, "Insert a valid size.");
    }

    private static int getPlayerNumber(Scanner scanner) {
        System.out.print("Insert the number of players: ");
        return Input.readNumber(scanner, "Insert a valid number of players.");
    }

    private static String getName(Scanner scanner) {
        System.out.print("Insert the name of the player: ");
        return scanner.next();
    }

    private static Strategy getStrategy(Scanner scanner) {
        System.out.print("Insert '1' for manual player, '2' for random player or '3' for smart player: ");
        int strategy;
        while (true) {
            strategy = Input.readNumber(scanner, "Insert a valid strategy option.");
            switch (strategy) {
                case 1: return Strategy.Manual;
                case 2: return Strategy.Random;
                case 3: return Strategy.Smart;
            }
            System.out.println("Insert a valid strategy option.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\\n");
        int gameType = getGameType(scanner);
        int timeLimit = getTimeLimit(scanner);
        game.Game game;
        if (gameType == 1) {
            game = new game.ap.Game(getTokenNumber(scanner), getSize(scanner), timeLimit);
        }
        else {
            game = new game.clique.Game(getNodeNumber(scanner), getSize(scanner), timeLimit);
        }
        if (gameType == 1) {
            for (int index = 0; index < getPlayerNumber(scanner); ++index) {
                game.addPlayer(getName(scanner), getStrategy(scanner));
            }
        }
        else {
            for (int index = 0; index < 2; ++index) {
                game.addPlayer(getName(scanner), getStrategy(scanner));
            }
        }
        game.start();
        game.showRanking();
    }
}
