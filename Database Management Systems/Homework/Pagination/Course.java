import java.util.Date;

public class Course {
    private String identifier;
    private String surname;
    private String name;
    private int mark;
    private Date date;

    public Course(String identifier, String surname, String name, int mark, Date date) {
        this.identifier = identifier;
        this.surname = surname;
        this.name = name;
        this.mark = mark;
        this.date = date;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
