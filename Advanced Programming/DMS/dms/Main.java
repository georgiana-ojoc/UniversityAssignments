package dms;

import dms.command.Shell;
import dms.exception.*;
import dms.resource.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Main {
    private void testCreateSave(String path, SaveLoadType type) {
        String extension = "";
        switch (type) {
            case PLAIN_TEXT: extension = ".txt";  break;
            case SERIALIZED_OBJECT: extension = ".ser";
        }
        Catalog catalog = new Catalog("Java Resources", path +  "\\catalog" + extension);
        Document document = new Document("java1", "Java Course 1",
                "https://profs.info.uaic.ro/~acf/java/slides/en/intro_slide_en.pdf");
        try {
            document.addTag("type", "Slides");
            catalog.addDocument(document);
            System.out.println(catalog);
            CatalogUtil.save(catalog, type);
        }
        catch (InvalidCatalogException | InvalidDocumentException | IOException exception) {
            exception.printStackTrace();
            System.err.println(exception.getMessage());
        }
    }

    private void testLoadView(String path, SaveLoadType type) {
        String extension = "";
        switch (type) {
            case PLAIN_TEXT: extension = ".txt";  break;
            case SERIALIZED_OBJECT: extension = ".ser";
        }
        try {
            Catalog catalog = CatalogUtil.load(path + "\\catalog" + extension, type);
            Document document = catalog.findById("java1");
            CatalogUtil.view(document);
        }
        catch (InvalidExtensionException | InvalidCatalogException | InvalidDocumentException
                | URISyntaxException | IOException  exception) {
            exception.printStackTrace();
            System.err.println(exception.getMessage());
        }
    }

    public void compulsory(String path) {
        testCreateSave(path, SaveLoadType.SERIALIZED_OBJECT);
        testLoadView(path, SaveLoadType.SERIALIZED_OBJECT);
    }

    public void testShell() {
        try {
            Shell shell = Shell.getShell();
            shell.run();
        }
        catch (IOException exception) {
            exception.printStackTrace();
            System.err.println(exception.getMessage());
        }
    }

    public void optionalAndBonus(String path) {
        testCreateSave(path, SaveLoadType.PLAIN_TEXT);
        testLoadView(path, SaveLoadType.PLAIN_TEXT);
        testShell();
    }

    public static void main(String[] args) {
        String path;
        if (args.length != 1) {
            System.out.print("Insert a valid path to the catalog (for example, \"D:\\Java\"): ");
            Scanner scanner = new Scanner(System.in);
            path = scanner.nextLine();
        }
        else {
            path = args[0];
        }
        Main app = new Main();
        app.compulsory(path);
        app.optionalAndBonus(path);
    }
}
