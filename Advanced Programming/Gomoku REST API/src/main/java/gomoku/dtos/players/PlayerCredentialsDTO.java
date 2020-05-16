package gomoku.dtos.players;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PlayerCredentialsDTO {
    private String username;
    private String password;
}
