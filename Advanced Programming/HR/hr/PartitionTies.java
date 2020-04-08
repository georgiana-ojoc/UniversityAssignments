package com.georgiana.ojoc.hr;

import java.util.*;

public class PartitionTies {
    private Map<Resident, List<List<Hospital>>> preferredHospitals;
    private Map<Hospital, List<List<Resident>>> preferredResidents;

    public PartitionTies() {
        preferredHospitals = new LinkedHashMap<>();
        preferredResidents = new LinkedHashMap<>();
    }

    public PartitionTies(Map<Resident, List<List<Hospital>>> preferredHospitals,
                         Map<Hospital, List<List<Resident>>> preferredResidents) {
        this.preferredHospitals = preferredHospitals;
        this.preferredResidents = preferredResidents;
    }

    public Map<Resident, List<List<Hospital>>> getPreferredHospitals() {
        return preferredHospitals;
    }

    public void setPreferredHospitals(Map<Resident, List<List<Hospital>>> preferredHospitals) {
        this.preferredHospitals = preferredHospitals;
    }

    public void addHospitals(Resident resident, List<List<Hospital>> hospitalLists) {
        preferredHospitals.put(resident, hospitalLists);
    }

    public Map<Hospital, List<List<Resident>>> getPreferredResidents() {
        return preferredResidents;
    }

    public void setPreferredResidents(Map<Hospital, List<List<Resident>>> preferredResidents) {
        this.preferredResidents = preferredResidents;
    }

    public void addResidents(Hospital hospital, List<List<Resident>> residentLists) {
        preferredResidents.put(hospital, residentLists);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Preferences:\n");

        for (Resident resident : preferredHospitals.keySet()) {
            result.append(resident).append(": (");
            for (List<Hospital> list : preferredHospitals.get(resident)) {
                if (list.size() < 2) {
                    result.append(list.get(0)).append(", ");
                }
                else {
                    result.append('[');
                    for (Hospital hospital : list) {
                        result.append(hospital).append(", ");
                    }
                    int size = result.length();
                    result.replace(size - 2, size, "");
                    result.append("], ");
                }
            }
            int size = result.length();
            result.replace(size - 2, size, "");
            result.append(")\n");
        }

        result.append('\n');

        for (Hospital hospital : preferredResidents.keySet()) {
            result.append(hospital.getName()).append(": (");
            for (List<Resident> list : preferredResidents.get(hospital)) {
                if (list.size() < 2) {
                    result.append(list.get(0)).append(", ");
                }
                else {
                    result.append('[');
                    for (Resident resident : list) {
                        result.append(resident).append(", ");
                    }
                    int size = result.length();
                    result.replace(size - 2, size, "");
                    result.append("], ");
                }
            }
            int size = result.length();
            result.replace(size - 2, size, "");
            result.append(")\n");
        }

        return result.toString();
    }
}
