package main.exception;


import main.DTO.response.BadResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<BadResponse> badRequestHandler(PostNotFoundException exception) {
        return new ResponseEntity<>(new BadResponse(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
