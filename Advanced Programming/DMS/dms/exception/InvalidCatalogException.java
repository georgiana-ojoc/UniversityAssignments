package dms.exception;

public class InvalidCatalogException extends Exception {
    public InvalidCatalogException() { System.out.println("No catalog created or loaded."); }
    public InvalidCatalogException(Exception e) { super("Invalid catalog file.", e); }
}
