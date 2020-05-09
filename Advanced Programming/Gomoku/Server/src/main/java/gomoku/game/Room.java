package gomoku.game;

import com.jcraft.jsch.*;
import gomoku.server.GameServer;
import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Room {
    private GameServer gameServer;
    private String identifier;
    private int playersNumber;
    private List<Socket> clientSockets;
    private List<BufferedReader> bufferedReaders;
    private List<PrintWriter> printWriters;
    private List<String> messages;

    public Room(GameServer gameServer, String identifier, int playersNumber) {
        this.gameServer = gameServer;
        this.identifier = identifier;
        this.playersNumber = playersNumber;
        clientSockets = new ArrayList<>();
        bufferedReaders = new ArrayList<>();
        printWriters = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public GameServer getGameServer() {
        return gameServer;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<Socket> getClientSockets() {
        return clientSockets;
    }

    public void addPlayer(Socket clientSocket, BufferedReader bufferedReader, PrintWriter printWriter) {
        clientSockets.add(clientSocket);
        bufferedReaders.add(bufferedReader);
        printWriters.add(printWriter);
    }

    public boolean isFull() {
        return clientSockets.size() == playersNumber;
    }

    public void startGame() throws IOException {
        if (playersNumber == 2) {
            startGameDouble();
        }
        else {
            startGameSingle();
        }
    }

    private void startGameDouble() throws IOException {
        notifyGameStarted();
        Board board = new Board(19);
        int currentPlayer = 1;
        int opponentPlayer = 2;
        while (true) {
            PrintWriter currentPrintWriter = printWriters.get(currentPlayer - 1);
            PrintWriter opponentPrintWriter = printWriters.get(opponentPlayer - 1);
            board.printBoard(currentPrintWriter);
            board.printBoard(opponentPrintWriter);
            notifyCurrentPlayer(currentPlayer);
            notifyOpponentPlayer(opponentPlayer);
            currentPrintWriter.println("Finish.");
            currentPrintWriter.flush();
            opponentPrintWriter.flush();
            Pair<Boolean, String> result = addPiece(board, currentPlayer, currentPrintWriter);
            board.printBoard(currentPrintWriter);
            board.printBoard(opponentPrintWriter);
            String message = result.getValue();
            messages.add(message);
            currentPrintWriter.println(message);
            opponentPrintWriter.println(message);
            currentPrintWriter.flush();
            opponentPrintWriter.flush();
            if (!result.getKey()) {
                if (message.contains("stop") || message.contains("exit")) {
                    currentPrintWriter.println("Bye, bye.");
                    return;
                }
                if (message.contains("won")) {
                    notifyWinner(currentPlayer);
                    notifyLoser(3 - currentPlayer);
                    break;
                }
                continue;
            }
            currentPlayer = 3 - currentPlayer;
            opponentPlayer = 3 - opponentPlayer;
        }
        sendHTML(createHTML());
    }

    private void startGameSingle() throws IOException {
        PrintWriter firstPrintWriter = printWriters.get(0);
        int currentPlayer = 1;
        notifyGameStarted(firstPrintWriter);
        Board board = new Board(19);
        while (true) {
            Pair<Boolean, String> result;
            if (currentPlayer == 1) {
                board.printBoard(firstPrintWriter);
                notifyCurrentPlayer(1);
                firstPrintWriter.println("Finish.");
                firstPrintWriter.flush();
                result = addPiece(board, currentPlayer, firstPrintWriter);
            }
            else {
                result = board.addPiece(currentPlayer);
            }
            board.printBoard(firstPrintWriter);
            String message = result.getValue();
            messages.add(message);
            firstPrintWriter.println(message);
            firstPrintWriter.flush();
            if (!result.getKey()) {
                if (message.contains("stop") || message.contains("exit")) {
                    firstPrintWriter.println("Bye, bye.");
                    return;
                }
                if (message.contains("won")) {
                    if (currentPlayer == 1) {
                        notifyWinner(currentPlayer);
                    }
                    else {
                        notifyLoser(3 - currentPlayer);
                    }
                    break;
                }
                continue;
            }
            currentPlayer = 3 - currentPlayer;
        }
        sendHTML(createHTML());
    }

    public void notifyGameStarted() {
        for (PrintWriter printWriter : printWriters) {
            notifyGameStarted(printWriter);
        }
    }

    public void notifyGameStarted(PrintWriter currentPrintWriter) {
        currentPrintWriter.println("The game has started.");
        currentPrintWriter.flush();
    }

    public void notifyCurrentPlayer(int currentPlayer) {
        PrintWriter currentPrintWriter = printWriters.get(currentPlayer - 1);
        currentPrintWriter.println("It is your turn.");
        currentPrintWriter.flush();
    }

    public void notifyOpponentPlayer(int opponentPlayer) {
        PrintWriter currentPrintWriter = printWriters.get(opponentPlayer - 1);
        currentPrintWriter.println("It is not your turn.");
        currentPrintWriter.flush();
    }

    public Pair<Boolean, String> addPiece(Board board, int currentPlayer, PrintWriter currentPrintWriter)
            throws IOException {
        String move;
        String[] words;
        int currentRow;
        int currentColumn;
        while (true) {
            move = bufferedReaders.get(currentPlayer - 1).readLine();
            if (move.equalsIgnoreCase("stop-round")) {
                return new Pair<>(false, "The game will end because " + currentPlayer + " has stopped it.");
            }
            if (move.equalsIgnoreCase("exit")) {
                return new Pair<>(false, "The game will end because " + currentPlayer + " has exited.");
            }
            words = move.split("\\s+");
            if (words.length < 3) {
                currentPrintWriter.println("Invalid command.");
                currentPrintWriter.println("Finish.");
                currentPrintWriter.flush();
                continue;
            }
            if (!words[0].equalsIgnoreCase("add-piece")) {
                currentPrintWriter.println("Invalid command.");
                currentPrintWriter.println("Finish.");
                currentPrintWriter.flush();
                continue;
            }
            currentRow = Integer.parseInt(words[1]);
            currentColumn = Integer.parseInt(words[2]);
            break;
        }
        return board.addPiece(currentPlayer, currentRow, currentColumn);
    }

    public void notifyWinner(int currentPlayer) {
        PrintWriter currentPrintWriter = printWriters.get(currentPlayer - 1);
        currentPrintWriter.println("You have won.");
        currentPrintWriter.flush();
    }

    public void notifyLoser(int opponentPlayer) {
        PrintWriter opponentPrintWriter = printWriters.get(opponentPlayer - 1);
        opponentPrintWriter.println("You have lost.");
        opponentPrintWriter.flush();
    }

    public void sendHTML(String HTML) {
        String username = "";
        String password = "";
        String host = "";
        InputStream inputStream = new ByteArrayInputStream(HTML.getBytes());
        try {
            JSch jSch = new JSch();
            Session session = jSch.getSession(username, host);
            Properties properties = new Properties();
            properties.put("StrictHostKeyChecking", "no");
            session.setConfig(properties);
            session.setPassword(password);
            session.connect();
            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            channelSftp.put(inputStream, "./gomoku/" + identifier + ".html");
            channelSftp.disconnect();
            session.disconnect();
        } catch (JSchException | SftpException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public String createHTML() {
        StringBuilder HTML = new StringBuilder("<!DOCTYPE html>\n<html>\n<body>\n" +
                "<h1>" + identifier + "</h1>\n");
        for (String message : messages) {
            HTML.append("<p>").append(message).append("</p>\n");
        }
        HTML.append("</body>\n</html>");
        return HTML.toString();
    }
}
