package main.exception;


import main.dto.response.BadResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.validation.ValidationException;


@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<BadResponse> postNotFoundExceptionHandler(PostNotFoundException exception) {
        return new ResponseEntity<>(new BadResponse(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<BadResponse> validationExceptionHandler(ValidationException exception) {
        return new ResponseEntity<>(new BadResponse(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<BadResponse> validationExceptionHandler(DataBaseException exception) {
        return new ResponseEntity<>(new BadResponse(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
