package com.georgiana.ojoc.hr;

public class HospitalResident {
    protected Problem problem;
    protected Partition partition;

    public HospitalResident() {
        problem = new Problem();
        partition = new Partition();
    }

    public HospitalResident(Problem problem, Partition partition) {
        this.problem = problem;
        this.partition = partition;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public Partition getPartition() {
        return partition;
    }

    public void setPartition(Partition partition) {
        this.partition = partition;
    }

    @Override
    public String toString() {
        return problem + "\n" + partition;
    }
}
