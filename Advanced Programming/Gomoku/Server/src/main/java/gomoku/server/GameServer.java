package gomoku.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gomoku.communication.Communication;
import gomoku.dtos.rooms.RoomCreateDTO;
import gomoku.dtos.rooms.RoomUpdateDTO;
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
        commands.add("register <username> <password>");
        commands.add("login <username> <password>");
        commands.add("list-commands");
        commands.add("list-available-rooms");
        commands.add("create-room-single");
        commands.add("create-room-double");
        commands.add("join-room <room identifier>");
        commands.add("add-piece <row> <column>");
        commands.add("stop-round");
        commands.add("logout");
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

    public String createRoom(String jwt, int playersNumber) {
        String identifier = generateRandomString();
        synchronized (rooms) {
            rooms.put(identifier, new Room(this, identifier, playersNumber));
            try {
                String request = new ObjectMapper().writeValueAsString(new RoomCreateDTO(identifier));
                Communication.post("rooms", request, jwt);
            } catch (JsonProcessingException exception) {
                System.out.println(exception.getMessage());
            }
        }
        return "The room with identifier " + identifier + " was created successfully.";
    }

    public synchronized void removeRoom(String identifier) {
        rooms.remove(identifier);
    }

    public synchronized Pair<String, Room> joinRoom(String username, String jwt, String identifier,
                                                    Socket clientSocket, BufferedReader bufferedReader,
                                                    PrintWriter printWriter) {
        Room room = rooms.get(identifier);
        if (room == null) {
            return new Pair<>("No room has this identifier.", null);
        }
        if (room.isFull()) {
            return new Pair<>("The room is full.", null);
        }
        room.addPlayer(clientSocket, bufferedReader, printWriter);
        RoomUpdateDTO roomUpdate = room.getRoomUpdateDTO();
        if (room.getRoomUpdateDTO().getFirstPlayer() == null) {
            roomUpdate.setFirstPlayer(username);
            if (room.isFull()) {
                roomUpdate.setSecondPlayer("computer");
            }
        }
        else {
            roomUpdate.setSecondPlayer(username);
        }
        try {
            String request = new ObjectMapper().writeValueAsString(roomUpdate);
            Communication.put("rooms/" + identifier, request, jwt);
        } catch (JsonProcessingException exception) {
            System.out.println(exception.getMessage());
        }
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
