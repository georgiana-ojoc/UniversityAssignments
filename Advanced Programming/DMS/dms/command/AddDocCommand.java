package dms.command;

import dms.exception.*;
import dms.resource.*;

import java.util.List;

public class AddDocCommand extends Command {
    public AddDocCommand(Catalog catalog, List<String> args) {
        this.catalog = catalog;
        this.args = args;
    }

    @Override
    public String execute() {
        Document document = new Document(args.get(0), args.get(1), args.get(2));
        try {
            catalog.addDocument(document);
        }
        catch (InvalidDocumentException e) {
            return e.getMessage();
        }
        return "Document added successfully.";
    }
}
