package gomoku;

import gomoku.rmi.RMIService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            Socket clientSocket = new Socket("localhost", 3000);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());
            Registry registry = LocateRegistry.getRegistry("localhost");
            RMIService rmiService = (RMIService) registry.lookup("RMIService");
            System.out.println("Commands:");
            System.out.println("list-commands");
            System.out.println("list-available-rooms");
            System.out.println("create-room-single");
            System.out.println("create-room-double");
            System.out.println("join-room <room identifier>");
            System.out.println("add-piece <row> <column>");
            System.out.println("stop-round");
            System.out.println("exit");
            while (true) {
                System.out.print("command: ");
                String request = scanner.nextLine();
                if (request.equalsIgnoreCase("list-commands")) {
                    List<String> commands = rmiService.getCommands();
                    for (String command : commands) {
                        System.out.println(command);
                    }
                    continue;
                }
                printWriter.println(request);
                printWriter.flush();
                String response;
                while (true) {
                    response = bufferedReader.readLine();
                    if (response.equals("Finish.")) {
                        break;
                    }
                    System.out.println(response);
                }
                if (request.equalsIgnoreCase("exit")) {
                    break;
                }
            }
            try {
                clientSocket.close();
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
            }
        } catch (IOException | NotBoundException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
