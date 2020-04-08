package dms.resource;

import dms.exception.*;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import org.json.simple.*;
import org.json.simple.parser.*;

public class CatalogUtil {
    private static void savePlainText(Catalog catalog) throws IOException {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(catalog.getPath()))) {
            fileWriter.write(catalog.toString());
        }
    }

    private static void saveSerializedObject(Catalog catalog) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(catalog.getPath()))) {
            oos.writeObject(catalog);
        }
    }

    public static void save(Catalog catalog, SaveLoadType type) throws IOException, InvalidCatalogException {
        if (catalog == null || catalog.getPath() == null || catalog.getPath().trim().equals("")) {
            throw new InvalidCatalogException();
        }
        switch (type) {
            case PLAIN_TEXT: savePlainText(catalog); break;
            case SERIALIZED_OBJECT: saveSerializedObject(catalog);
        }
    }

    private static Catalog loadPlainText(String path) throws InvalidExtensionException, InvalidCatalogException {
        if (!path.endsWith(".txt")) {
            throw new InvalidExtensionException();
        }
        JSONParser jsonParser = new JSONParser();
        Catalog catalog = new Catalog();
        try {
            Object object = jsonParser.parse(new FileReader(path));
            JSONObject jsonObject = (JSONObject) object;
            catalog.setName((String) jsonObject.get("name"));
            catalog.setPath((String) jsonObject.get("path"));

            JSONArray documents = (JSONArray) jsonObject.get("documents");
            for (Object o : documents) {
                JSONObject document = (JSONObject) o;
                String docName = (String) document.get("name");
                String docId = (String) document.get("id");
                String docLocation = (String) document.get("location");

                catalog.addDocument(new Document(docId, docName, docLocation));
            }
            return catalog;
        }
        catch (IOException | ParseException | InvalidDocumentException e) {
            throw new InvalidCatalogException(e);
        }
    }

    private static Catalog loadSerializedObject(String path) throws InvalidExtensionException, InvalidCatalogException {
        if (!path.endsWith(".ser")) {
            throw new InvalidExtensionException();
        }
        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream(path))) {
            return (Catalog)oos.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new InvalidCatalogException(e);
        }
    }

    public static Catalog load(String path, SaveLoadType type)
            throws InvalidExtensionException, InvalidCatalogException {
        if (path == null || path.trim().equals("")) {
            throw new IllegalArgumentException("Path should not be empty.");
        }
        switch (type) {
            case PLAIN_TEXT: return loadPlainText(path);
            case SERIALIZED_OBJECT: return loadSerializedObject(path);
        }
        return loadSerializedObject(path);
    }

    public static void view(Document document) throws URISyntaxException, IOException, InvalidDocumentException {
        Desktop desktop = Desktop.getDesktop();
        if (document == null) {
            throw new InvalidDocumentException();
        }
        File file = new File(document.getLocation());
        if (file.exists()) { desktop.open(file); }
        else { desktop.browse(new URI(document.getLocation())); }
    }
}
