package com.georgiana.ojoc.hr;

import java.util.Objects;

public class Element {
    private Resident resident;
    private Hospital hospital;

    public Element(Resident resident, Hospital hospital) {
        this.resident = resident;
        this.hospital = hospital;
    }

    public Resident getResident() {
        return resident;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (!(object instanceof Element)) { return false; }
        Element element = (Element) object;
        return getResident().equals(element.getResident()) &&
                getHospital().equals(element.getHospital());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getResident(), getHospital());
    }

    @Override
    public String toString() {
        return "(" + resident + ": " + hospital.getName() + ")";
    }
}
