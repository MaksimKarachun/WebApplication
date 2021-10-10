package main.exception;


import main.dto.response.AddPostErrorDTO;
import main.dto.response.AddPostResponse;
import main.dto.response.BadResponse;
import main.dto.response.LoginResponse;
import main.dto.response.RegisterErrorDTO;
import main.dto.response.RegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<BadResponse> postNotFoundExceptionHandler(PostNotFoundException exception) {
        return new ResponseEntity<>(new BadResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AddNewPostValidationException.class)
    public ResponseEntity<AddPostResponse> addNewPostValidationExceptionHandler(AddNewPostValidationException exception) {
        AddPostResponse addPostResponse = new AddPostResponse(false);
        AddPostErrorDTO addPostErrorDTO = new AddPostErrorDTO();
        exception.errorList.getFieldErrors().forEach(fieldError -> {
            if (fieldError.getField().equals("text")) {
                addPostErrorDTO.setText(fieldError.getDefaultMessage());
            } else {
                addPostErrorDTO.setTitle(fieldError.getDefaultMessage());
            }
        });
        addPostResponse.setErrors(addPostErrorDTO);
        return new ResponseEntity<>(addPostResponse, HttpStatus.OK);
    }

    @ExceptionHandler(RegisterValidationException.class)
    public ResponseEntity<RegisterResponse> registerValidationExceptionHandler(RegisterValidationException exception) {
        RegisterResponse registerResponse = new RegisterResponse();
        RegisterErrorDTO registerErrorDTO = new RegisterErrorDTO();
        exception.errorList.getFieldErrors().forEach(fieldError -> {
            if (fieldError.getField().equals("name")) {
                registerErrorDTO.setName(fieldError.getDefaultMessage());
            }
            if (fieldError.getField().equals("password")) {
                registerErrorDTO.setPassword(fieldError.getDefaultMessage());
            }
        });
        registerResponse.setErrors(registerErrorDTO);
        return new ResponseEntity<>(registerResponse, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BadResponse> runtimeExceptionHandler(RuntimeException exception) {
        return new ResponseEntity<>(new BadResponse(exception.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<LoginResponse> runtimeExceptionHandler() {
        return new ResponseEntity<>(new LoginResponse(false), HttpStatus.OK);
    }

}
