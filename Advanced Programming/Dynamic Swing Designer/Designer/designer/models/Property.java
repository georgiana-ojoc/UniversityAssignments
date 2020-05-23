package designer.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Property {
    private String name;
    private String type;
    private Type propertyType;
    private int intValue;
    private String stringValue;

    public Property(String name, String type) {
        this.name = name;
        this.type = type;
        propertyType = Type.Other;
        intValue = -1;
        stringValue = null;
    }

    public Property(String name, String type, int intValue) {
        this.name = name;
        this.type = type;
        propertyType = Type.Int;
        this.intValue = intValue;
        stringValue = null;
    }

    public Property(String name, String type, String stringValue) {
        this.name = name;
        this.type = type;
        propertyType = Type.String;
        intValue = -1;
        this.stringValue = stringValue;
    }

    public Object getValue() {
        if (type.equals("java.lang.String")) {
            return stringValue;
        }
        if (type.equals("int")) {
            return intValue;
        }
        return null;
    }

    public boolean isValueEditable() {
        return type.equals("java.lang.String") || type.equals("int");
    }

    public void update(Object value) {
        switch (propertyType) {
            case Int:
                intValue = Integer.parseInt((String) value);
                return;
            case String:
                stringValue = (String) value;
        }
    }
}
