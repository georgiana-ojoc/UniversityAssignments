package dms.command;

import dms.exception.InvalidCatalogException;
import dms.resource.*;

import java.io.IOException;

public class SaveCommand extends Command {
    public SaveCommand(Catalog catalog) {
        this.catalog = catalog;
        this.args = null;
    }

    @Override
    public String execute() {
        try {
            CatalogUtil.save(catalog, SaveLoadType.SERIALIZED_OBJECT);
        }
        catch (InvalidCatalogException | IOException e) {
            return e.getMessage();
        }
        return "Catalog saved successfully.";
    }
}
