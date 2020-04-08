package dms.command;

import dms.exception.*;
import dms.resource.*;

import java.util.List;

public class AddTagCommand extends Command {
    public AddTagCommand(Catalog catalog, List<String> args) {
        this.catalog = catalog;
        this.args = args;
    }

    @Override
    public String execute() {
        try {
            Document document = catalog.findById(args.get(0));
            document.addTag(args.get(1), args.get(2));
        }
        catch (InvalidDocumentException exception) {
            return "";
        }
        return "Tag added successfully.";
    }
}
