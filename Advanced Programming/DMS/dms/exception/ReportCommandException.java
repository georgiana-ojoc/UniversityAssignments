package dms.exception;

public class ReportCommandException extends Exception {
    public ReportCommandException(Exception e) {
        super("Error occurred during report command." + e);
    }
}
