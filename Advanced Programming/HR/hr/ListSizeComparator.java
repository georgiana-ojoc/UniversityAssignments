package com.georgiana.ojoc.hr;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ListSizeComparator implements Comparator<Resident> {
    private final Map<Resident, List<Hospital>> preferredHospitals;

    public ListSizeComparator(Map<Resident, List<Hospital>> preferredHospitals) {
        this.preferredHospitals = preferredHospitals;
    }

    @Override
    public int compare(Resident resident1, Resident resident2) {
        int difference = preferredHospitals.get(resident1).size() - preferredHospitals.get(resident2).size();
        if (difference != 0) {
            return difference;
        }
        return resident1.getName().compareTo(resident2.getName());
    }
}
