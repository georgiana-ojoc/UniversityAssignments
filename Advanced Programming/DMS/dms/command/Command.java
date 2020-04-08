package dms.command;

import dms.resource.Catalog;

import java.util.List;

public abstract class Command {
    protected Catalog catalog;
    protected List<String> args;

    public abstract String execute();
}
