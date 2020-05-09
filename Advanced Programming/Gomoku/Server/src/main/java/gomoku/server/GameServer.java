package gomoku.server;

import gomoku.game.Room;
import gomoku.game.Round;
import gomoku.rmi.RMIService;
import gomoku.rmi.RMIServiceImplementation;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameServer {
    private int port;
    private ServerSocket serverSocket;
    private List<String> commands;
    private final Map<String, Room> rooms;

    public GameServer(int port) {
        this.port = port;
        serverSocket = null;
        commands = new ArrayList<>();
        createCommands();
        rooms = new HashMap<>();
    }

    private void createCommands() {
        commands.add("list-commands");
        commands.add("list-available-rooms");
        commands.add("create-room-single");
        commands.add("create-room-double");
        commands.add("join-room <room identifier>");
        commands.add("add-piece <row> <column>");
        commands.add("stop-round");
        commands.add("exit");
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            RMIService rmiService = new RMIServiceImplementation(commands);
            RMIService stub = (RMIService) UnicastRemoteObject.exportObject(rmiService, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("RMIService", stub);
        } catch (IOException | AlreadyBoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void waitClients() {
        try {
            System.out.println("Listening at port " + port + '.');
            while (true) {
                new ClientThread(this, serverSocket.accept(), commands).start();
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException exception) {
                    System.out.println(exception.getMessage());
                }
            }
        }
    }

    public synchronized List<String> getAvailableRooms() {
        List<String> availableRooms = new ArrayList<>();
        for (String identifier : rooms.keySet()) {
            if (!rooms.get(identifier).isFull()) {
                availableRooms.add(identifier);
            }
        }
        return availableRooms;
    }

    public String createRoom(int playersNumber) {
        String identifier = generateRandomString();
        synchronized (rooms) {
            rooms.put(identifier, new Room(this, identifier, playersNumber));
        }
        return "The room with identifier " + identifier + " was created successfully.";
    }

    public synchronized void removeRoom(String identifier) {
        rooms.remove(identifier);
    }

    public synchronized Pair<String, Room> joinRoom(String identifier, Socket clientSocket, BufferedReader bufferedReader,
                                                    PrintWriter printWriter) {
        Room room = rooms.get(identifier);
        if (room == null) {
            return new Pair<>("No room has this identifier.", null);
        }
        if (room.isFull()) {
            return new Pair<>("The room is full.", null);
        }
        room.addPlayer(clientSocket, bufferedReader, printWriter);
        return new Pair<>("The player has joined the room with identifier " + identifier + " successfully.", room);
    }

    public void checkStartGame(String identifier, PrintWriter printWriter) {
        Room room = rooms.get(identifier);
        if (room == null) {
            return;
        }
        if (room.isFull()) {
            printWriter.println("The game will start.");
            printWriter.flush();
            new Round(room).start();
        }
        else {
            printWriter.println("Waiting another player.");
            printWriter.flush();
        }
    }

    private String generateRandomString() {
        String lowerCharacters = "abcdefghijklmnopqrstuvwxyz";
        String upperCharacters = lowerCharacters.toUpperCase();
        String numbers = "0123456789";
        String characters = lowerCharacters + upperCharacters + numbers;
        StringBuilder randomString = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int index = 0; index < 5; ++index) {
            randomString.append(characters.charAt(secureRandom.nextInt(characters.length())));
        }
        return randomString.toString();
    }
}
