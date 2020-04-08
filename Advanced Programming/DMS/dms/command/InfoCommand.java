package dms.command;

import dms.tool.FileMetadata;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

public class InfoCommand extends Command {
    public InfoCommand(List<String> args) {
        this.catalog = null;
        this.args = args;
    }

    @Override
    public String execute() {
        try {
            return FileMetadata.extractMetadata(args.get(0));
        } catch (IOException | TikaException | SAXException e) {
            return e.getMessage();
        }
    }
}
