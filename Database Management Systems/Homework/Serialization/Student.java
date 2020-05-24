import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Student implements Serializable {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private int yearOfStudy;

    public Student(String firstName, String lastName, LocalDate dateOfBirth, int yearOfStudy) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.yearOfStudy = yearOfStudy;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    @Override
    public String toString() {
        return "Student { " +
                "firstName = '" + firstName + '\'' +
                ", lastName = '" + lastName + '\'' +
                ", dateOfBirth = " + dateOfBirth +
                ", yearOfStudy = " + yearOfStudy +
                " }";
    }
}
