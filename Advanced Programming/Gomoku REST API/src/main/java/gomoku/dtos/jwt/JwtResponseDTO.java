package gomoku.dtos.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponseDTO {
    private String token;

    public JwtResponseDTO(String token) {
        this.token = token;
    }
}
