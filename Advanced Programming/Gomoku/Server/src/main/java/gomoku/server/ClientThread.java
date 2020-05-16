package gomoku.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gomoku.communication.Communication;
import gomoku.dtos.players.JwtDTO;
import gomoku.dtos.players.PlayerCredentialsDTO;
import gomoku.dtos.rooms.RoomUpdateDTO;
import gomoku.game.Room;
import javafx.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

public class ClientThread extends Thread {
    private GameServer gameServer;
    private Socket clientSocket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private String username;
    private String jwt;
    private boolean loggedIn;

    public ClientThread(GameServer gameServer, Socket clientSocket, List<String> commands) {
        this.gameServer = gameServer;
        this.clientSocket = clientSocket;
        bufferedReader = null;
        printWriter = null;
        loggedIn = false;
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
        else if (words[0].equalsIgnoreCase("register")) {
            register(words);
        }
        else if (words[0].equalsIgnoreCase("login")) {
            login(words);
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
        else if (command.equalsIgnoreCase("logout")) {
            username = null;
            jwt = null;
            loggedIn = false;
            printWriter.println("You have logged out successfully.");
            printWriter.println("Finish.");
            printWriter.flush();
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

    private void register(String[] words) {
        if (words.length == 1) {
            printWriter.println("You did not specify the username and password.");
            printWriter.println("Finish.");
            return;
        }
        if (words.length == 2) {
            printWriter.println("You did not specify the password.");
            printWriter.println("Finish.");
            return;
        }
        try {
            String request = new ObjectMapper().writeValueAsString(new PlayerCredentialsDTO(words[1], words[2]));
            Communication.post("players", request, null);
            printWriter.println("You have registered successfully.");
            printWriter.println("Finish.");
        } catch (JsonProcessingException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void login(String[] words) {
        if (words.length == 1) {
            printWriter.println("You did not specify the username and password.");
            printWriter.println("Finish.");
            return;
        }
        if (words.length == 2) {
            printWriter.println("You did not specify the password.");
            printWriter.println("Finish.");
            return;
        }
        try {
            String request = new ObjectMapper().writeValueAsString(new PlayerCredentialsDTO(words[1], words[2]));
            ResponseEntity<String> response = Communication.post("authenticate", request, null);
            username = words[1];
            jwt = new ObjectMapper().readValue(Objects.requireNonNull(response.getBody()), JwtDTO.class).getToken();
            loggedIn = true;
            printWriter.println("You have logged in successfully.");
        } catch (JsonProcessingException exception) {
            System.out.println(exception.getMessage());
        }
        catch (HttpClientErrorException exception) {
            printWriter.println("No player with this username and password was found.");
        }
        printWriter.println("Finish.");
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
        if (!loggedIn) {
            printWriter.println("You are not logged in.");
        }
        else {
            printWriter.println(gameServer.createRoom(jwt, playersNumber));
        }
        printWriter.println("Finish.");
        printWriter.flush();
    }

    private void joinRoom(String[] words) {
        if (!loggedIn) {
            printWriter.println("You are not logged in.");
            printWriter.println("Finish.");
            printWriter.flush();
            return;
        }
        if (words.length == 1) {
            printWriter.println("You did not specify the room identifier.");
            printWriter.println("Finish.");
            return;
        }
        Pair<String, Room> result = gameServer.joinRoom(username, jwt, words[1],
                clientSocket, bufferedReader, printWriter);
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
        RoomUpdateDTO roomUpdate = room.getRoomUpdateDTO();
        if (roomUpdate.getWinner() != null) {
            try {
                String request = new ObjectMapper().writeValueAsString(roomUpdate);
                Communication.put("rooms/" + room.getIdentifier(), request, jwt);
            } catch (JsonProcessingException exception) {
                System.out.println(exception.getMessage());
            }
        }
        printWriter.println("The game has ended.");
        printWriter.println("Finish.");
        printWriter.flush();
    }
}
