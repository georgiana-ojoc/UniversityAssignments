package gomoku.game;

import java.io.IOException;
import java.net.Socket;

public class Round extends Thread {
    private Room room;

    public Round(Room room) {
        this.room = room;
    }

    public void run() {
                room.getGameServer().removeRoom(room.getIdentifier());
        try {
            room.startGame();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        for (Socket clientSocket : room.getClientSockets()) {
            synchronized (clientSocket) {
                clientSocket.notify();
            }
        }
        room.getGameServer().removeRoom(room.getIdentifier());
    }
}
