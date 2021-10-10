package main.exception;

import org.springframework.validation.Errors;

public class RegisterValidationException extends Exception {

  Errors errorList;

  public RegisterValidationException(Errors errorList){
    this.errorList = errorList;
  }
}
