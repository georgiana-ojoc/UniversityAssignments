package gomoku;

import gomoku.server.GameServer;

public class Main {
    public static void main(String[] args) {
        GameServer gameServer = new GameServer(3000);
        gameServer.startServer();
        gameServer.waitClients();
    }
}
