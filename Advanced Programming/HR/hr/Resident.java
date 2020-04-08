package com.georgiana.ojoc.hr;

import java.util.Objects;

public class Resident implements Comparable<Resident> {
    private String name;

    public Resident() { name = ""; }

    public Resident(String name) {
        this.name = name;
    }

    public Resident(Resident resident) { this.name = resident.name; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Resident resident) {
        return getName().compareTo(resident.getName());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (!(object instanceof Resident)) { return false; }
        Resident resident = (Resident) object;
        return getName().equals(resident.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return name;
    }
}
