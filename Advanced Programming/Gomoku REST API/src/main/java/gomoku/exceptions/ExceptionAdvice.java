package gomoku.exceptions;

import gomoku.dtos.exceptions.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(value = DuplicateUsernameException.class)
    public ResponseEntity<?> handleDuplicateUsernameException(DuplicateUsernameException exception) {
        ErrorDTO error = new ErrorDTO(exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException exception) {
        ErrorDTO error = new ErrorDTO(exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
