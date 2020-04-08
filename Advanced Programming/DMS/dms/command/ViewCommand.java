package dms.command;

import dms.exception.InvalidDocumentException;
import dms.resource.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class ViewCommand extends Command {
    public ViewCommand(Catalog catalog, List<String> args) {
        this.catalog = catalog;
        this.args = args;
    }

    @Override
    public String execute() {
        try {
            Document document = catalog.findById(args.get(0));
            CatalogUtil.view(document);
        }
        catch (InvalidDocumentException exception) {
            return "";
        }
        catch (IOException | URISyntaxException e) {
            return e.getMessage();
        }
        return "";
    }
}
