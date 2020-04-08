package com.georgiana.ojoc.hr;

import java.util.*;

public class GaleShapley implements Algorithm {
    private List<Resident> residentList;
    private List<Hospital> hospitalList;

    private Map<Resident, List<List<Hospital>>> preferredHospitals;
    private Map<Hospital, List<List<Resident>>> preferredResidents;

    private Map<Hospital, List<Resident>> chosenResidentsMap;
    private int[][] residentRanks;
    Map<Resident, Boolean> assignedResidents;

    private Matching matching;
    private Random random;

    private void resetResidentNumbers() {
        for (Hospital hospital : hospitalList) {
            hospital.resetResidentNumber();
        }
    }

    public GaleShapley(HospitalResidentTies hospitalResidentTies) {
        residentList = hospitalResidentTies.getProblem().getResidentList();
        hospitalList = hospitalResidentTies.getProblem().getHospitalList();
        resetResidentNumbers();

        preferredHospitals = new LinkedHashMap<>(hospitalResidentTies.getPartitionTies().getPreferredHospitals());
        preferredResidents = new LinkedHashMap<>(hospitalResidentTies.getPartitionTies().getPreferredResidents());

        chosenResidentsMap = new LinkedHashMap<>();
        for (Hospital hospital : hospitalList) {
            chosenResidentsMap.put(hospital, new ArrayList<>());
        }
        residentRanks = new int[hospitalList.size()][residentList.size()];
        assignedResidents = new LinkedHashMap<>();
        for (Resident resident : residentList) {
            assignedResidents.put(resident, false);
        }

        matching = new Matching();
        random = new Random();
    }

    public List<Resident> getResidentList() {
        return residentList;
    }

    public List<Hospital> getHospitalList() {
        return hospitalList;
    }

    public Map<Resident, List<List<Hospital>>> getPreferredHospitals() {
        return preferredHospitals;
    }

    public Map<Hospital, List<List<Resident>>> getPreferredResidents() {
        return preferredResidents;
    }

    public void setHospitalResidentTies(HospitalResidentTies hospitalResidentTies) {
        residentList = new ArrayList<>(hospitalResidentTies.getProblem().getResidentList());
        hospitalList = new ArrayList<>(hospitalResidentTies.getProblem().getHospitalList());
        resetResidentNumbers();

        preferredHospitals = new HashMap<>(hospitalResidentTies.getPartitionTies().getPreferredHospitals());
        preferredResidents = new HashMap<>(hospitalResidentTies.getPartitionTies().getPreferredResidents());

        chosenResidentsMap = new LinkedHashMap<>();
        for (Hospital hospital : hospitalList) {
            chosenResidentsMap.put(hospital, new ArrayList<>());
        }
        residentRanks = new int[hospitalList.size()][residentList.size()];
        assignedResidents = new LinkedHashMap<>();
    }

    private void createResidentRanks() {
        int line = 0;
        for (Hospital hospital : preferredResidents.keySet()) {
            int rank = 1;
            for (List<Resident> list : preferredResidents.get(hospital)) {
                for (Resident resident : list) {
                    residentRanks[line][residentList.indexOf(resident)] = rank;
                }
                ++rank;
            }
            ++line;
        }
    }

    private Resident getFirstUnassignedResident() {
        for (Resident resident : assignedResidents.keySet()) {
            if (!assignedResidents.get(resident)) {
                return resident;
            }
        }
        return null;
    }

    private void addMatch(Resident resident, Hospital hospital) {
        List<Resident> chosenResidentsList = chosenResidentsMap.get(hospital);
        chosenResidentsList.add(resident);
        matching.addMatch(new Element(resident, hospital));
        assignedResidents.put(resident, true);
    }

    private void removeMatch(Resident resident, Hospital hospital) {
        List<Resident> chosenResidentsList = chosenResidentsMap.get(hospital);
        chosenResidentsList.remove(resident);
        chosenResidentsMap.put(hospital, chosenResidentsList);
        matching.removeMatch(new Element(resident, hospital));
        assignedResidents.put(resident, false);
    }

    private Resident getWorstResident(Hospital hospital) {
        List<Resident> chosenResidentsList = chosenResidentsMap.get(hospital);
        int maximumRank = 0;
        List<Resident> worstResidentsTie = new ArrayList<>();
        for (Resident chosenResident : chosenResidentsList) {
            int rank = residentRanks[hospitalList.indexOf(hospital)][residentList.indexOf(chosenResident)];
            if (rank > maximumRank) {
                maximumRank = rank;
                worstResidentsTie.clear();
                worstResidentsTie.add(chosenResident);
            } else if (rank == maximumRank) {
                worstResidentsTie.add(chosenResident);
            }
        }
        return worstResidentsTie.get(random.nextInt(worstResidentsTie.size()));
    }

    private void removeResident(Resident resident, Hospital hospital) {
        List<List<Resident>> residents = new LinkedList<>(preferredResidents.get(hospital));
        int listIndex = 0;
        int residentIndex = 0;
        boolean found = false;
        outerLoop:
        for (; listIndex < residents.size(); ++listIndex) {
            for (residentIndex = 0; residentIndex < residents.get(listIndex).size();
                 ++residentIndex) {
                if (residents.get(listIndex).get(residentIndex).equals(resident)) {
                    found = true;
                    break outerLoop;
                }
            }
        }
        if (!found) {
            return;
        }
        if (residents.get(listIndex).size() == 1) {
            residents.remove(listIndex);
        }
        else {
            List<Resident> residentTie = new LinkedList<>(residents.get(listIndex));
            residentTie.remove(residentIndex);
            residents.remove(listIndex);
            residents.add(listIndex, residentTie);
        }
        preferredResidents.put(hospital, residents);
    }

    private void removeHospital(Resident resident, Hospital hospital) {
        List<List<Hospital>> hospitals = new LinkedList<>(preferredHospitals.get(resident));
        int listIndex = 0;
        int hospitalIndex = 0;
        boolean found = false;
        outerLoop:
        for (; listIndex < hospitals.size(); ++listIndex) {
            for (hospitalIndex = 0; hospitalIndex < hospitals.get(listIndex).size();
                 ++hospitalIndex) {
                if (hospitals.get(listIndex).get(hospitalIndex).equals(hospital)) {
                    found = true;
                    break outerLoop;
                }
            }
        }
        if (!found) {
            return;
        }
        if (hospitals.get(listIndex).size() == 1) {
            hospitals.remove(listIndex);
        }
        else {
            List<Hospital> hospitalTie = new LinkedList<>(hospitals.get(listIndex));
            hospitalTie.remove(hospitalIndex);
            hospitals.remove(listIndex);
            hospitals.add(listIndex, hospitalTie);
        }
        preferredHospitals.put(resident, hospitals);
    }

    /**
     * https://core.ac.uk/download/pdf/42368869.pdf
     */
    @Override
    public void solve() {
        createResidentRanks();
        for (Resident resident = getFirstUnassignedResident(); resident != null
                && preferredHospitals.get(resident).size() > 0;
             resident = getFirstUnassignedResident()) {
            int hospitalTieSize = preferredHospitals.get(resident).get(0).size();
            Hospital firstHospital = preferredHospitals.get(resident).get(0).get(random.nextInt(hospitalTieSize));
            addMatch(resident, firstHospital);
            if (firstHospital.isOverSubscribed()) {
                removeMatch(getWorstResident(firstHospital), firstHospital);
            }
            if (firstHospital.isFull()) {
                Resident worstResident = getWorstResident(firstHospital);
                int worstResidentRank = residentRanks[hospitalList.indexOf(firstHospital)]
                        [residentList.indexOf(worstResident)];
                for (List<Resident> residents : preferredResidents.get(firstHospital)) {
                    for (Resident nextResident : residents) {
                        if (!nextResident.equals(worstResident) && residentRanks[hospitalList.indexOf(firstHospital)]
                                [residentList.indexOf(nextResident)] >= worstResidentRank) {
                            removeResident(nextResident, firstHospital);
                            removeHospital(nextResident, firstHospital);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() { return matching.toString(); }
}
