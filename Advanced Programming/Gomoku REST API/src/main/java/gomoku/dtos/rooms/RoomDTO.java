package gomoku.dtos.rooms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class RoomDTO {
    private Long identifier;
    private String randomIdentifier;
    private String firstPlayer;
    private String secondPlayer;
    private String winner;
    private Date createdAt;
}
