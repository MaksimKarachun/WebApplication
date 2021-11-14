package main.exception;

import org.springframework.validation.Errors;

public class EditPostValidationException extends Exception{

  Errors errorList;

  public EditPostValidationException(Errors errorList){
    this.errorList = errorList;
  }

}
