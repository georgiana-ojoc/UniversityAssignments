package gomoku.exceptions;

public class PlayerNotFoundException extends NotFoundException {
    public PlayerNotFoundException(Long identifier) {
        super("Player with identifier " + identifier);
    }

    public PlayerNotFoundException(String username) {
        super("Player with username " + username);
    }
}
