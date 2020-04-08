package com.georgiana.ojoc.hr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Problem {
    private List<Resident> residentList;
    private List<Hospital> hospitalList;

    public Problem() {
        residentList = new ArrayList<>();
        hospitalList = new ArrayList<>();
    }

    public Problem(List<Resident> residentList, List<Hospital> hospitalList) {
        this.residentList = residentList;
        this.hospitalList = hospitalList;
    }

    public List<Resident> getResidentList() {
        return residentList;
    }

    public void setResidentList(List<Resident> residentList) {
        this.residentList = residentList;
    }

    public void addResidents(Resident ... residents) {
        residentList.addAll(Arrays.asList(residents));
    }

    public List<Hospital> getHospitalList() {
        return hospitalList;
    }

    public void setHospitalList(List<Hospital> hospitalList) {
        this.hospitalList = hospitalList;
    }

    public void addHospitals(Hospital ... hospitals) {
        hospitalList.addAll(Arrays.asList(hospitals));
    }

    @Override
    public String toString() {
        return "Residents: " + residentList.stream()
                                        .map(Resident::getName)
                                        .collect(Collectors.joining(", "))
                + "\nHospitals: " + hospitalList.stream()
                                                .map(Hospital::toString)
                                                .collect(Collectors.joining(", "))
                + '\n';
    }
}
