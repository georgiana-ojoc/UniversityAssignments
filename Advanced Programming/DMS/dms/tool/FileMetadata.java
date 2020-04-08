package dms.tool;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileMetadata  {
    public static String extractMetadata(String path)
            throws IOException, TikaException, SAXException {
        Parser parser = new AutoDetectParser();
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        parser.parse(fileInputStream, handler, metadata, context);

        StringBuilder result = new StringBuilder();
        String[] metadataNames = metadata.names();
        for(String name : metadataNames) {
            result.append(name).append(": ").append(metadata.get(name)).append('\n');
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }
}
