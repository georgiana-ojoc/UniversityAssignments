package com.georgiana.ojoc.hr;

import java.util.Objects;

public class Hospital implements Comparable<Hospital> {
    private String name;
    private int capacity;
    private int residentNumber;

    public Hospital() {
        name = "";
        capacity = 1;
        residentNumber = 0;
    }

    public Hospital(String name) {
        this.name = name;
        capacity = 1;
        residentNumber = 0;
    }

    public Hospital(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        residentNumber = 0;
    }

    public Hospital(Hospital hospital) {
        this.name = hospital.name;
        this.capacity = hospital.capacity;
        this.residentNumber = hospital.residentNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getResidentNumber() { return residentNumber; }

    public boolean isOverSubscribed() {
        return residentNumber > capacity;
    }

    public boolean isFull() {
        return residentNumber == capacity;
    }

    public void addResident() { ++residentNumber; }

    public void removeResident() { --residentNumber; }

    public void resetResidentNumber() { residentNumber = 0; }

    @Override
    public int compareTo(Hospital hospital) {
        return getName().compareTo(hospital.getName());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (!(object instanceof Hospital)) { return false; }
        Hospital hospital = (Hospital) object;
        return getName().equals(hospital.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return name + '(' + capacity + ')';
    }
}
