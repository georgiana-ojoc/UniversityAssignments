package com.georgiana.ojoc.hr;

import com.github.javafaker.Faker;

import java.util.*;
import java.util.stream.*;

public class Main {
    private static Resident[] residents;
    private static Hospital[] hospitals;

    private static void tryFirstComeFirstServed(HospitalResident hospitalResident) {
        FirstComeFirstServed firstComeFirstServed = new FirstComeFirstServed(hospitalResident);
        firstComeFirstServed.solve();
        System.out.println(firstComeFirstServed);

        if (firstComeFirstServed.isStable()) {
            System.out.println("The matching is stable.");
        }
        else {
            System.out.println("The matching is not stable.");
        }
    }

    private static void tryGaleShapley(HospitalResidentTies hospitalResidentTies) {
        GaleShapley galeShapley = new GaleShapley(hospitalResidentTies);
        galeShapley.solve();
        System.out.println(galeShapley);
    }

    private static void compulsory() {
        System.out.println("Compulsory:");

        residents = IntStream.rangeClosed(0, 3)
                .mapToObj(index -> new Resident("R" + index))
                .toArray(Resident[]::new);

        hospitals = IntStream.rangeClosed(0, 2)
                .mapToObj(index -> new Hospital("H" + index))
                .toArray(Hospital[]::new);
        hospitals[1].setCapacity(2);
        hospitals[2].setCapacity(2);

        List<Resident> residentList = new ArrayList<>(Arrays.asList(residents));
        Collections.sort(residentList);

        Set<Hospital> hospitalSet = Stream.of(hospitals).collect(Collectors.toCollection(TreeSet::new));

        Map<Resident, List<Hospital>> preferredHospitals = new LinkedHashMap<>();
        preferredHospitals.put(residents[0], Arrays.asList(hospitals));
        preferredHospitals.put(residents[1], Arrays.asList(hospitals));
        preferredHospitals.put(residents[2], Arrays.asList(hospitals[0], hospitals[1]));
        preferredHospitals.put(residents[3], Arrays.asList(hospitals[0], hospitals[2]));

        Map<Hospital, List<Resident>> preferredResidents = new TreeMap<>();
        preferredResidents.put(hospitals[0], Arrays.asList(residents[3], residents[0], residents[1], residents[2]));
        preferredResidents.put(hospitals[1], Arrays.asList(residents[0], residents[2], residents[1]));
        preferredResidents.put(hospitals[2], Arrays.asList(residents[0], residents[1], residents[3]));

        preferredHospitals.forEach((key, value) -> System.out.println(key + ": "
                + value.stream()
                .map(Hospital::getName)
                .collect(Collectors.joining(", ", "(", ")"))));

        System.out.println();

        preferredResidents.forEach((key, value) -> System.out.println(key + ": "
                + value.stream()
                .map(Resident::getName)
                .collect(Collectors.joining(", ", "(", ")"))));

        System.out.println();

        System.out.println("Residents who prefer H0 and H2: "
                + residentList.stream()
                .filter(resident -> preferredHospitals.get(resident)
                        .containsAll(Arrays.asList(hospitals[0], hospitals[2])))
                .map(Resident::getName)
                .collect(Collectors.joining(", ")));

        System.out.println("Hospitals that have R0 as their top preference: "
                + hospitalSet.stream()
                .filter(hospital -> preferredResidents.get(hospital)
                        .indexOf(residents[0]) == 0)
                .map(Hospital::getName)
                .collect(Collectors.joining(", ")));

        System.out.println();

        Problem problem = new Problem(residentList, new ArrayList<>(hospitalSet));
        Partition partition = new Partition(preferredHospitals, preferredResidents);
        HospitalResident hospitalResident = new HospitalResident(problem, partition);

        tryFirstComeFirstServed(hospitalResident);
    }

    private static void optional() {
        System.out.println("\nOptional:");

        HospitalResidentGenerator hospitalResidentGenerator
                = new HospitalResidentGenerator(5, 5, 2);
        hospitalResidentGenerator.generate();
        System.out.println(hospitalResidentGenerator);

        tryFirstComeFirstServed(hospitalResidentGenerator);
    }

    public static void bonus() {
        System.out.println("\nBonus:\n");

        Problem problem = new Problem(Arrays.asList(residents), Arrays.asList(hospitals));

        Map<Resident, List<List<Hospital>>> preferredHospitals = new LinkedHashMap<>();
        preferredHospitals.put(residents[0],
                Arrays.asList(Collections.singletonList(hospitals[0]),
                        Arrays.asList(hospitals[1], hospitals[2])));
        preferredHospitals.put(residents[1],
                Arrays.asList(Collections.singletonList(hospitals[0]),
                        Collections.singletonList(hospitals[1]),
                        Collections.singletonList(hospitals[2])));
        preferredHospitals.put(residents[2],
                Arrays.asList(Collections.singletonList(hospitals[0]),
                        Collections.singletonList(hospitals[1])));
        preferredHospitals.put(residents[3],
                Collections.singletonList(Arrays.asList(hospitals[0], hospitals[2])));

        Map<Hospital, List<List<Resident>>> preferredResidents = new LinkedHashMap<>();
        preferredResidents.put(hospitals[0],
                Arrays.asList(Collections.singletonList(residents[3]),
                        Arrays.asList(residents[0], residents[1]),
                        Collections.singletonList(residents[2])));
        preferredResidents.put(hospitals[1],
                Arrays.asList(Collections.singletonList(residents[0]),
                        Collections.singletonList(residents[2]),
                        Collections.singletonList(residents[1])));
        preferredResidents.put(hospitals[2],
                Arrays.asList(Collections.singletonList(residents[0]),
                        Collections.singletonList(residents[1]),
                        Collections.singletonList(residents[3])));

        PartitionTies partitionTies = new PartitionTies(preferredHospitals, preferredResidents);
        HospitalResidentTies hospitalResidentTies = new HospitalResidentTies(problem, partitionTies);
        System.out.println(hospitalResidentTies);

        tryGaleShapley(hospitalResidentTies);
        tryGaleShapley(hospitalResidentTies);
        System.out.println();

        HospitalResidentTiesGenerator hospitalResidentTiesGenerator
            = new HospitalResidentTiesGenerator(10, 8, 4);
        hospitalResidentTiesGenerator.generate();
        System.out.println(hospitalResidentTiesGenerator);

        tryGaleShapley(hospitalResidentTiesGenerator);
        tryGaleShapley(hospitalResidentTiesGenerator);
    }

    public static void main(String[] args) {
        compulsory();
        optional();
        bonus();
    }
}
