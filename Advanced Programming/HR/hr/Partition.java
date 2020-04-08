package com.georgiana.ojoc.hr;

import java.util.*;
import java.util.stream.Collectors;

public class Partition {
    private Map<Resident, List<Hospital>> preferredHospitals;
    private Map<Hospital, List<Resident>> preferredResidents;

    public Partition() {
        preferredHospitals = new LinkedHashMap<>();
        preferredResidents = new LinkedHashMap<>();
    }

    public Partition(Map<Resident, List<Hospital>> preferredHospitals,
                     Map<Hospital, List<Resident>> preferredResidents) {
        this.preferredHospitals = preferredHospitals;
        this.preferredResidents = preferredResidents;
    }

    public Map<Resident, List<Hospital>> getPreferredHospitals() {
        return preferredHospitals;
    }

    public void setPreferredHospitals(Map<Resident, List<Hospital>> preferredHospitals) {
        this.preferredHospitals = preferredHospitals;
    }

    public void addHospitals(Resident resident, List<Hospital> hospitalList) {
        preferredHospitals.put(resident, hospitalList);
    }

    public Map<Hospital, List<Resident>> getPreferredResidents() {
        return preferredResidents;
    }

    public void setPreferredResidents(Map<Hospital, List<Resident>> preferredResidents) {
        this.preferredResidents = preferredResidents;
    }

    public void addResidents(Hospital hospital, List<Resident> residentList) {
        preferredResidents.put(hospital, residentList);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Preferences:\n");

        preferredHospitals.forEach((key, value) -> result.append(key)
                                                        .append(": ")
                                                        .append(value.stream()
                        .map(Hospital::getName)
                        .collect(Collectors.joining(", ", "(", ")")))
                                                        .append('\n'));

        result.append('\n');

        preferredResidents.forEach((key, value) -> result.append(key.getName())
                                                        .append(": ")
                                                        .append(value.stream()
                        .map(Resident::getName)
                        .collect(Collectors.joining(", ", "(", ")")))
                                                        .append('\n'));

        return result.toString();
    }
}
