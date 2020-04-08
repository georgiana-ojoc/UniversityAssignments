package dms.resource;

import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.*;

public class Document implements Serializable {
    private String id;
    private String name;
    private String location;
    private Map<String, String> tags = new HashMap<>();

    public Document(String id, String name, String location) {
        setId(id);
        setName(name);
        setLocation(location);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().equals("")) {
            throw new IllegalArgumentException("Id should not be empty.");
        }
        this.id = id;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location == null || location.trim().equals("")) {
            throw new IllegalArgumentException("Location should not be empty.");
        }
        this.location = location;
    }

    public Map<String, String> getTags() { return tags; }

    public void setTags(Map<String, String> tags) { this.tags = tags; }

    public void addTag(String key, String value) {
        if (key == null || key.trim().equals("")) {
            throw new IllegalArgumentException("Key should not be empty.");
        }
        if (value == null || value.trim().equals("")) {
            throw new IllegalArgumentException("Value should not be empty.");
        }
        tags.put(key, value);
    }

    @Override
    public String toString() {
        return "{\n" +
                "\"id\":\"" + id + '\"' +
                ",\n\"name\":\"" + name + '\"' +
                ",\n\"location\":\"" + location + '\"' +
                ",\n\"tags\":" + JSONObject.toJSONString(tags) +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document)) return false;
        Document document = (Document) o;
        return Objects.equals(getId(), document.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
