import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            SAXHandler saxHandler = new SAXHandler();
            saxParser.parse(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("catalog.xml")),
                    saxHandler);
            for (Mark mark : saxHandler.getMarks()) {
                System.out.println(mark);
            }
        } catch (ParserConfigurationException | SAXException | IOException exception) {
            exception.printStackTrace();
        }
    }
}
