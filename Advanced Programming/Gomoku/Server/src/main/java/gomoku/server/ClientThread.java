package gomoku.server;

import gomoku.game.Room;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientThread extends Thread {
    private GameServer gameServer;
    private Socket clientSocket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private List<String> commands;

    public ClientThread(GameServer gameServer, Socket clientSocket, List<String> commands) {
        this.gameServer = gameServer;
        this.clientSocket = clientSocket;
        bufferedReader = null;
        printWriter = null;
        this.commands = commands;
    }

    public void run() {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            printWriter = new PrintWriter(clientSocket.getOutputStream());
            String command;
            while (true) {
                command = bufferedReader.readLine();
                if (!processCommand(command)) {
                    break;
                }
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        finally {
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException exception) {
                    System.out.println(exception.getMessage());
                }
            }
        }
    }

    private boolean processCommand(String command) {
        String[] words = command.split("\\s+");
        if (words.length == 0) {
            printWriter.println("Invalid command.");
            printWriter.println("Finish.");
        }
        else if (command.equalsIgnoreCase("list-available-rooms")) {
            listAvailableRooms();
        }
        else if (command.equalsIgnoreCase("create-room-single")) {
            createRoom(1);
        }
        else if (command.equalsIgnoreCase("create-room-double")) {
            createRoom(2);
        }
        else if (words[0].equalsIgnoreCase("join-room")) {
            joinRoom(words);
        }
        else if (command.equalsIgnoreCase("exit")) {
            printWriter.println("Bye, bye.");
            printWriter.println("Finish.");
            printWriter.flush();
            return false;
        }
        else {
            printWriter.println("Invalid command.");
            printWriter.println("Finish.");
        }
        printWriter.flush();
        return true;
    }

    private void listAvailableRooms() {
        List<String> availableRooms = gameServer.getAvailableRooms();
        if (availableRooms.size() == 0) {
            printWriter.println("There is no room available to join.");
        }
        else {
            for (String room : availableRooms) {
                printWriter.println(room);
            }
        }
        printWriter.println("Finish.");
        printWriter.flush();
    }

    private void createRoom(int playersNumber) {
        printWriter.println(gameServer.createRoom(playersNumber));
        printWriter.println("Finish.");
        printWriter.flush();
    }

    private void joinRoom(String[] words) {
        if (words.length == 1) {
            printWriter.println("You did not specify the room identifier.");
            printWriter.println("Finish.");
            return;
        }
        Pair<String, Room> result = gameServer.joinRoom(words[1], clientSocket, bufferedReader, printWriter);
        printWriter.println(result.getKey());
        printWriter.flush();
        Room room = result.getValue();
        if (room != null) {
            wait(room);
        }
        else {
            printWriter.println("Finish.");
            printWriter.flush();
        }
    }

    private void wait(Room room) {
        gameServer.checkStartGame(room.getIdentifier(), printWriter);
        try {
            synchronized (clientSocket) {
                clientSocket.wait();
            }
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        printWriter.println("The game has ended.");
        printWriter.println("Finish.");
        printWriter.flush();
    }
}
