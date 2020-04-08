package com.georgiana.ojoc.hr;

import java.util.*;
import java.util.stream.Collectors;

public class FirstComeFirstServed implements Algorithm {
    private Map<Resident, List<Hospital>> preferredHospitals;
    private Map<Hospital, List<Resident>> preferredResidents;
    private Matching matching;

    public FirstComeFirstServed(HospitalResident hospitalResident) {
        preferredHospitals = new TreeMap<>
                (new ListSizeComparator(hospitalResident.getPartition().getPreferredHospitals()));
        preferredHospitals.putAll(hospitalResident.getPartition().getPreferredHospitals());
        preferredResidents = hospitalResident.getPartition().getPreferredResidents();
        matching = new Matching();
    }

    public Map<Resident, List<Hospital>> getPreferredHospitals() {
        return preferredHospitals;
    }

    public Map<Hospital, List<Resident>> getPreferredResidents() {
        return preferredResidents;
    }

    public void setHospitalResident(HospitalResident hospitalResident) {
        preferredHospitals = new TreeMap<>
                (new ListSizeComparator(hospitalResident.getPartition().getPreferredHospitals()));
        preferredHospitals.putAll(hospitalResident.getPartition().getPreferredHospitals());
        preferredResidents = hospitalResident.getPartition().getPreferredResidents();
    }

    @Override
    public void solve() {
        for (Resident resident : preferredHospitals.keySet()) {
            for (Hospital hospital : preferredHospitals.get(resident)) {
                if (preferredResidents.get(hospital).contains(resident)
                        && hospital.getResidentNumber() < hospital.getCapacity()) {
                    matching.addMatch(new Element(resident, hospital));
                    break;
                }
            }
        }
    }

    public boolean isStable() {
        List<Element> matchList = matching.getMatchList();
        for (Element residentMatch : matchList) {
            Resident resident = residentMatch.getResident();
            Hospital hospital = residentMatch.getHospital();
            int hospitalRank = preferredHospitals.get(resident).indexOf(hospital);
            if (hospitalRank > 0) {
                int residentRank = preferredResidents.get(hospital).indexOf(resident);
                List<Hospital> betterHospitals = preferredHospitals.get(resident).stream()
                                                                    .limit(hospitalRank)
                                                                    .collect(Collectors.toList());
                for (Hospital betterHospital : betterHospitals) {
                    for (Element hospitalMatch : matchList) {
                        if (hospitalMatch.getHospital().equals(betterHospital)) {
                            Resident chosenResident = hospitalMatch.getResident();
                            if (residentRank < preferredResidents.get(betterHospital).indexOf(chosenResident)) {
                                return false;
                            }
                        }
                    }

                }
            }
        }
        return true;
    }

    @Override
    public String toString() { return matching.toString();
    }
}
