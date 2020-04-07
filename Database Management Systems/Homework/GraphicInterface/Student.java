public class Student {
    private int index;
    private String lastName;
    private String firstName;
    private double average;

    public Student(int index, String lastName, String firstName, double average) {
        this.index = index;
        this.lastName = lastName;
        this.firstName = firstName;
        this.average = average;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }
}
