package gomoku.dtos.rooms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RoomUpdateDTO {
    private String firstPlayer;
    private String secondPlayer;
    private String winner;
}
