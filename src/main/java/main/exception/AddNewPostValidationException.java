package main.exception;

import org.springframework.validation.Errors;

public class AddNewPostValidationException extends Exception {

  Errors errorList;

  public AddNewPostValidationException(Errors errorList){
    this.errorList = errorList;
  }

}
