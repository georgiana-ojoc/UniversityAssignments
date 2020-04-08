package dms.resource;

import dms.exception.*;
import org.json.simple.JSONArray;


import java.io.Serializable;
import java.util.*;

public class Catalog implements Serializable {
    private String name;
    private String path;
    private List<Document> documents = new ArrayList<>();

    public Catalog() { }

    public Catalog(String name, String path) {
        setName(name);
        setPath(path);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("Name should not be empty.");
        }
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        if (path == null || path.trim().equals("")) {
            throw new IllegalArgumentException("Path should not be empty.");
        }
        this.path = path;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) throws InvalidDocumentException {
        if (documents == null) {
            throw new InvalidDocumentException();
        }
        this.documents = documents;
    }

    public void addDocument(Document document) throws InvalidDocumentException {
        if (document == null) {
            throw new InvalidDocumentException();
        }
        for (Document d : documents) {
            if (d.equals(document)) {
                throw new InvalidDocumentException("A document with this ID already exist in the catalog.");
            }
        }
        documents.add(document);
    }

    public Document findById(String id) throws InvalidDocumentException {
        if (documents == null) {
            throw new InvalidDocumentException();
        }
        for (Document document : documents) {
           if (document.getId().equals(id)) {
               return document;
           }
        }
        throw new InvalidDocumentException("Document not found.");
    }

    @Override
    public String toString() {
        return "{\n" +
                "\"name\":\"" + name + '\"' +
                ",\n\"path\":\"" + path + '\"' +
                ",\n\"documents\":\n" + JSONArray.toJSONString(documents) +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Catalog)) return false;
        Catalog catalog = (Catalog) o;
        return getName().equals(catalog.getName()) &&
                getPath().equals(catalog.getPath()) &&
                getDocuments().equals(catalog.getDocuments());
    }

    @Override
    public int hashCode() { return Objects.hash(getName(), getPath(), getDocuments()); }
}
