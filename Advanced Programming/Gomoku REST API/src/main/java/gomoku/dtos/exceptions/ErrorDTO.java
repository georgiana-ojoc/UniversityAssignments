package gomoku.dtos.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorDTO {
    private String message;
    private LocalDateTime timestamp;

    public ErrorDTO(String message) {
        this.message = message;
        timestamp = LocalDateTime.now();
    }
}
