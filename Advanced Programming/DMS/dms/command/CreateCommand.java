package dms.command;

import dms.resource.Catalog;

import java.util.List;

public class CreateCommand extends Command {
    public CreateCommand(Catalog catalog, List<String> args) {
        this.catalog = catalog;
        this.args = args;
    }

    @Override
    public String execute() {
        if (args != null) {
            catalog.setName(args.get(0));
            catalog.setPath(args.get(1) + "\\catalog.ser");
        }
        return "Catalog created successfully.";
    }
}
