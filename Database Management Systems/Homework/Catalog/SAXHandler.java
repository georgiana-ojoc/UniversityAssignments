import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class SAXHandler extends DefaultHandler {
    private String content;
    private Mark mark;
    private List<Mark> marks;

    public SAXHandler() {
        super();
        content = null;
        mark = null;
        marks = new ArrayList<>();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Mark getMark() {
        return mark;
    }

    public void setMark(Mark mark) {
        this.mark = mark;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }

    @Override
    public void startElement(String uri, String localName, String qualifiedName, Attributes attributes) throws SAXException {
        if (qualifiedName.equals("mark")) {
            mark = new Mark();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qualifiedName) throws SAXException {
        switch (qualifiedName) {
            case "mark":
                marks.add(mark);
                break;
            case "identifier":
                mark.setIdentifier(content);
                break;
            case "firstName":
                mark.setFirstName(content);
                break;
            case "lastName":
                mark.setLastName(content);
                break;
            case "studentYear":
                mark.setStudentYear(content);
                break;
            case "group":
                mark.setGroup(content);
                break;
            case "scholarship":
                mark.setScholarship(content);
                break;
            case "birthDate":
                mark.setBirthDate(content);
                break;
            case "email":
                mark.setEmail(content);
                break;
            case "value":
                mark.setValue(content);
                break;
            case "markDate":
                mark.setMarkDate(content);
                break;
            case "course":
                mark.setCourse(content);
                break;
            case "courseYear":
                mark.setCourseYear(content);
                break;
            case "semester":
                mark.setSemester(content);
                break;
            case "credits":
                mark.setCredits(content);
        }
    }

    @Override
    public void characters(char[] characters, int start, int length) throws SAXException {
        content = String.copyValueOf(characters, start, length).trim();
    }
}
