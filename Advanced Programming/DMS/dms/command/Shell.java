package dms.command;

import dms.resource.Catalog;

import java.io.*;
import java.util.*;

/**
 * Follows Singleton Design Pattern.
 */
public class Shell {
    private static Shell shell = null;
    private Catalog catalog;

    private Shell() { catalog = new Catalog(); }

    private Shell(String name, String path) { catalog = new Catalog(name, path + "\\catalog.ser"); }

    public static Shell getShell() {
        if (shell == null) {
            shell = new Shell();
        }
        return shell;
    }

    public static Shell getShell(String name, String path) {
        if (shell == null) {
            shell = new Shell(name, path);
        }
        return shell;
    }

    private void showHelp() {
        System.out.println("Commands:");
        System.out.println("\thelp - show commands");
        System.out.println("\tcreate <name> <path> - create catalog with specified name and path");
        System.out.println("\tadd-doc <id> <name> <location> - add document with unique ID and name" +
                " from specified location");
        System.out.println("\tadd-tag <id> <key> <value> - add tag to document specified by ID");
        System.out.println("\tsave - save catalog at his specified path");
        System.out.println("\tload <path> - load catalog from specified path");
        System.out.println("\tlist - print catalog and documents information");
        System.out.println("\tview <id> - open document specified by ID");
        System.out.println("\tinfo <path> - print file metadata");
        System.out.println("\treport [html] <path> - create and save html report at specified path");
        System.out.println("\texit - close shell");
    }

    private void runCommand(Command command) {
        String result = command.execute();
        if (result != null && !result.trim().equals("")) {
            System.out.println(result);
        }
    }

    /**
     * Type "exit" to end program.
     */
    public void run() throws IOException {
        System.out.println("~ Shell made by Georgiana and Vlad ~");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("command: ");
            String input = reader.readLine();
            if (input == null || input.trim().equals("")) {
                continue;
            }
            StringTokenizer st = new StringTokenizer(input, " ");
            String commandType = st.nextToken();
            List<String> args = new ArrayList<>();
            while (st.hasMoreTokens()) {
                args.add(st.nextToken());
            }
            if (args.size() == 0) {
                switch (commandType) {
                    case "help":
                        showHelp();
                        break;
                    case "save":
                        runCommand(new SaveCommand(catalog));
                    case "list":
                        runCommand(new ListCommand(catalog));
                        break;
                    case "exit": System.out.println("Bye, bye! :)"); return;
                    default: System.out.println("Unknown command or invalid number of arguments.");
                }
            }
            else {
                switch (commandType) {
                    case "add-doc":
                        if (args.size() != 3) {
                            System.out.println("Invalid number of arguments.");
                            continue;
                        }
                        runCommand(new AddDocCommand(catalog, args));
                        break;
                    case "add-tag":
                        if (args.size() != 3) {
                            System.out.println("Invalid number of arguments.");
                            continue;
                        }
                        runCommand(new AddTagCommand(catalog, args));
                        break;
                    case "create":
                        if (args.size() != 2) {
                            System.out.println("Invalid number of arguments.");
                            continue;
                        }
                        CreateCommand createCommand = new CreateCommand(catalog, args);
                        runCommand(createCommand);
                        catalog = createCommand.catalog;
                        break;
                    case "load":
                        if (args.size() != 1) {
                            System.out.println("Invalid number of arguments.");
                            continue;
                        }
                        LoadCommand loadCommand = new LoadCommand(args);
                        runCommand(loadCommand);
                        catalog = loadCommand.catalog;
                        break;
                    case "view":
                        if (args.size() != 1) {
                            System.out.println("Invalid number of arguments.");
                            continue;
                        }
                        runCommand(new ViewCommand(catalog, args));
                        break;
                    case "info":
                        if (args.size() != 1) {
                            System.out.println("Invalid number of arguments.");
                            continue;
                        }
                        runCommand(new InfoCommand(args));
                        break;
                    case "report":
                        if (args.size() != 2) {
                            System.out.println("Invalid number of arguments.");
                            continue;
                        }
                        runCommand(new ReportCommand(catalog, args));
                        break;
                    default: System.out.println("Unknown command.");
                }
            }
        }
    }
}
