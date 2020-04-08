package dms.command;

import dms.exception.InvalidCatalogException;
import dms.exception.ReportCommandException;
import dms.resource.*;
import dms.tool.HTMLReport;

import java.util.List;

public class ReportCommand extends Command {
    public ReportCommand(Catalog catalog, List<String> args) {
        this.catalog = catalog;
        this.args = args;
    }

    @Override
    public String execute() {
        try {
            if (!args.get(0).equals("html")) {
                return "Invalid report type.";
            }
            if (catalog.getName() == null || catalog.getPath() == null) {
                throw new InvalidCatalogException();
            }
            HTMLReport.create(args.get(1), catalog);
        }
        catch (InvalidCatalogException |ReportCommandException e) {
            return e.getMessage();
        }
        return "Report created successfully under the name: \"report.html\".";
    }
}
