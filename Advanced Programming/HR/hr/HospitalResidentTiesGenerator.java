package com.georgiana.ojoc.hr;

import com.github.javafaker.Faker;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HospitalResidentTiesGenerator extends HospitalResidentTies {
    private int maximumResidentNumber;
    private int maximumHospitalNumber;
    private int maximumCapacity;
    private Faker faker;
    private Random random;

    public HospitalResidentTiesGenerator(int maximumResidentNumber, int maximumHospitalNumber, int maximumCapacity) {
        this.maximumResidentNumber = maximumResidentNumber;
        this.maximumHospitalNumber = maximumHospitalNumber;
        this.maximumCapacity = maximumCapacity;
        faker = new Faker();
        random = new Random();
    }

    private List<Integer> generateRandomNumbers(int limit) {
        List<Integer> randomNumbers = IntStream.range(0, limit).boxed().collect(Collectors.toList());
        Collections.shuffle(randomNumbers);
        return randomNumbers.stream().limit(random.nextInt(limit) + 1).collect(Collectors.toList());
    }

    public void generate() {
        int randomResidentNumber = random.nextInt(maximumResidentNumber) + 1;
        List<Resident> residentList = new ArrayList<>();
        for (int index = 0; index < randomResidentNumber; ++index) {
            Resident resident = new Resident();
            do {
                resident.setName(faker.name().fullName());
            } while (residentList.contains(resident));
            residentList.add(resident);
        }
        problem.setResidentList(residentList);

        int randomHospitalNumber = random.nextInt(maximumHospitalNumber) + 1;
        List<Hospital> hospitalList = new ArrayList<>();
        for (int index = 0; index < randomHospitalNumber; ++index) {
            Hospital hospital = new Hospital();
            do {
                hospital.setName(faker.harryPotter().character() + " Hospital");
            } while (hospitalList.contains(hospital));
            hospital.setCapacity(random.nextInt(maximumCapacity) + 1);
            hospitalList.add(hospital);
        }
        problem.setHospitalList(hospitalList);

        for (int count = 0; count < randomResidentNumber; ++count) {
            List<Integer> randomIndexes = generateRandomNumbers(randomHospitalNumber);
            List<List<Hospital>> preferredHospitals = new ArrayList<>();
            List<Hospital> hospitalTie = new ArrayList<>();
            for (Integer index : randomIndexes) {
                hospitalTie.add(hospitalList.get(index));
                if (random.nextInt(3) == 0) {
                    List<Hospital> hospitalTieCopy = new ArrayList<>(hospitalTie);
                    preferredHospitals.add(hospitalTieCopy);
                    hospitalTie.clear();
                }
            }
            if (!hospitalTie.isEmpty()) {
                preferredHospitals.add(hospitalTie);
            }
            partitionTies.addHospitals(residentList.get(count), preferredHospitals);
        }

        for (int count = 0; count < randomHospitalNumber; ++count) {
            List<Integer> randomIndexes = generateRandomNumbers(randomResidentNumber);
            List<List<Resident>> preferredResidents = new ArrayList<>();
            List<Resident> residentTie = new ArrayList<>();
            for (Integer index : randomIndexes) {
                residentTie.add(residentList.get(index));
                if (random.nextInt(3) == 0) {
                    List<Resident> residentTieCopy = new ArrayList<>(residentTie);
                    preferredResidents.add(residentTieCopy);
                    residentTie.clear();
                }
            }
            if (!residentTie.isEmpty()) {
                preferredResidents.add(residentTie);
            }
            partitionTies.addResidents(hospitalList.get(count), preferredResidents);
        }
    }
}
