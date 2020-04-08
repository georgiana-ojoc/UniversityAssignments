package com.georgiana.ojoc.hr;

public class HospitalResidentTies {
    protected Problem problem;
    protected PartitionTies partitionTies;

    public HospitalResidentTies() {
        problem = new Problem();
        partitionTies = new PartitionTies();
    }

    public HospitalResidentTies(Problem problem, PartitionTies partitionTies) {
        this.problem = problem;
        this.partitionTies = partitionTies;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public PartitionTies getPartitionTies() {
        return partitionTies;
    }

    public void setPartitionTies(PartitionTies PartitionTies) {
        this.partitionTies = PartitionTies;
    }

    @Override
    public String toString() {
        return problem + "\n" + partitionTies;
    }
}
