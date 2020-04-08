package dms.command;

import dms.resource.Catalog;

public class ListCommand extends Command {
    public ListCommand(Catalog catalog) {
        this.catalog = catalog;
        this.args = null;
    }

    @Override
    public String execute() { return catalog.toString(); }
}
