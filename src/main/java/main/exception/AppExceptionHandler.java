package main.exception;


import main.dto.response.AddPostErrorDTO;
import main.dto.response.AddPostResponse;
import main.dto.response.BadResponse;
import main.dto.response.EditPostErrorDTO;
import main.dto.response.EditPostResponse;
import main.dto.response.LoginResponse;
import main.dto.response.ModerationPostResponse;
import main.dto.response.RegisterErrorDTO;
import main.dto.response.RegisterResponse;
import main.dto.response.UploadImageErrorDto;
import main.dto.response.UploadImageErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(EditPostValidationException.class)
    public ResponseEntity<EditPostResponse> editPostValidationExceptionHandler(EditPostValidationException exception) {
        EditPostResponse editPostResponse = new EditPostResponse(false);
        EditPostErrorDTO editPostErrorDTO = new EditPostErrorDTO();
        exception.errorList.getFieldErrors().forEach(fieldError -> {
            if (fieldError.getField().equals("text")) {
                editPostErrorDTO.setText(fieldError.getDefaultMessage());
            } else {
                editPostErrorDTO.setTitle(fieldError.getDefaultMessage());
            }
        });
        editPostResponse.setErrors(editPostErrorDTO);
        return new ResponseEntity<>(editPostResponse, HttpStatus.OK);
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

    @ExceptionHandler(UploadImageException.class)
    public ResponseEntity<UploadImageErrorResponse> notAllowedImageTypeException(
        UploadImageException exception){
        UploadImageErrorResponse response = new UploadImageErrorResponse();
        response.setResult(false);
        response.setErrors(new UploadImageErrorDto(exception.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<LoginResponse> runtimeExceptionHandler() {
        return new ResponseEntity<>(new LoginResponse(false), HttpStatus.OK);
    }

    @ExceptionHandler(ModerationPostException.class)
    public ResponseEntity<ModerationPostResponse> moderationPostExceptionHandler() {
        return new ResponseEntity<>(new ModerationPostResponse(false), HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BadResponse> runtimeExceptionHandler(RuntimeException exception) {
        return new ResponseEntity<>(new BadResponse(exception.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
