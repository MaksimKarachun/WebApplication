package main.exception;

public class AddPostCommentException extends RuntimeException{

  private String errorMessage;

  public AddPostCommentException(String errorMessage) {
    super(errorMessage);
  }
}
