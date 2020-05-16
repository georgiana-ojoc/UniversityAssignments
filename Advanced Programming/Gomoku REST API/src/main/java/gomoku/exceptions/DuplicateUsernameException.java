package gomoku.exceptions;

public class DuplicateUsernameException extends Exception {
    public DuplicateUsernameException(String username) {
        super("Username " + username + " is already taken.");
    }
}
