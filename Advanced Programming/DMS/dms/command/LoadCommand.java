package dms.command;

import dms.exception.*;
import dms.resource.*;

import java.util.List;

public class LoadCommand extends Command {
    public LoadCommand(List<String> args) {
        this.catalog = null;
        this.args = args;
    }

    @Override
    public String execute() {
        try {
            catalog = CatalogUtil.load(args.get(0), SaveLoadType.SERIALIZED_OBJECT);
        }
        catch (InvalidExtensionException | InvalidCatalogException e) {
            return e.getMessage();
        }
        return "Catalog loaded successfully.";
    }
}
