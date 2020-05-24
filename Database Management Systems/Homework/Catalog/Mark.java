public class Mark {
    private String identifier;
    private String firstName;
    private String lastName;
    private String studentYear;
    private String group;
    private String scholarship;
    private String birthDate;
    private String email;
    private String value;
    private String markDate;
    private String course;
    private String courseYear;
    private String semester;
    private String credits;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    public String getStudentYear() {
        return studentYear;
    }

    public void setStudentYear(String studentYear) {
        this.studentYear = studentYear;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getScholarship() {
        return scholarship;
    }

    public void setScholarship(String scholarship) {
        this.scholarship = scholarship;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMarkDate() {
        return markDate;
    }

    public void setMarkDate(String markDate) {
        this.markDate = markDate;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCourseYear() {
        return courseYear;
    }

    public void setCourseYear(String courseYear) {
        this.courseYear = courseYear;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "Mark {" +
                "identifier = '" + identifier + '\'' +
                ", firstName = '" + firstName + '\'' +
                ", lastName = '" + lastName + '\'' +
                ", studentYear = '" + studentYear + '\'' +
                ", group = '" + group + '\'' +
                ", scholarship = '" + scholarship + '\'' +
                ", birthDate = '" + birthDate + '\'' +
                ", email = '" + email + '\'' +
                ", value = '" + value + '\'' +
                ", markDate = '" + markDate + '\'' +
                ", course = '" + course + '\'' +
                ", courseYear = '" + courseYear + '\'' +
                ", semester = '" + semester + '\'' +
                ", credits = '" + credits + '\'' +
                " }";
    }
}
