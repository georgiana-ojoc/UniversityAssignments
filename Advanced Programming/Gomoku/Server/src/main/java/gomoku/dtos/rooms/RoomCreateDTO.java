package gomoku.dtos.rooms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomCreateDTO {
    private String randomIdentifier;
    public RoomCreateDTO(String randomIdentifier) {
        this.randomIdentifier = randomIdentifier;
    }
}
