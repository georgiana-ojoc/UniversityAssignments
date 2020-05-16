package gomoku.exceptions;

public class RoomNotFoundException extends NotFoundException {
    public RoomNotFoundException(Long identifier) {
        super("Room with identifier " + identifier);
    }

    public RoomNotFoundException(String randomIdentifier) {
        super("Room with random identifier " + randomIdentifier);
    }
}
