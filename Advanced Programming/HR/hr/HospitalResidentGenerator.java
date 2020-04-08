package com.georgiana.ojoc.hr;

import com.github.javafaker.Faker;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HospitalResidentGenerator extends HospitalResident {
    private int maximumResidentNumber;
    private int maximumHospitalNumber;
    private int maximumCapacity;
    private Faker faker;
    private Random random;

    public HospitalResidentGenerator(int maximumResidentNumber, int maximumHospitalNumber, int maximumCapacity) {
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
            List<Hospital> preferredHospitals = new ArrayList<>();
            for (Integer index : randomIndexes) {
                preferredHospitals.add(hospitalList.get(index));
            }
            partition.addHospitals(residentList.get(count), preferredHospitals);
        }

        for (int count = 0; count < randomHospitalNumber; ++count) {
            List<Integer> randomIndexes = generateRandomNumbers(randomResidentNumber);
            List<Resident> preferredResidents = new ArrayList<>();
            for (Integer index : randomIndexes) {
                preferredResidents.add(residentList.get(index));
            }
            partition.addResidents(hospitalList.get(count), preferredResidents);
        }
    }
}
