package gomoku.dtos.players;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerCredentialsDTO {
    private String username;
    private String password;

    public PlayerCredentialsDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
