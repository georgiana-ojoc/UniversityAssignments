package gomoku.dtos.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class JwtRequestDTO {
    private String username;
    private String password;
}
