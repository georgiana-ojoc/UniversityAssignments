package dms.exception;

public class InvalidDocumentException extends Exception {
    public InvalidDocumentException() { System.out.println("No document added."); }
    public InvalidDocumentException(String message) { System.out.println(message); }
    public InvalidDocumentException(Exception e) {
        super("Invalid document file.", e);
    }
}
