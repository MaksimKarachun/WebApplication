package main.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(int id){
        super(String.format("Post with id %s not found", id));
    }
}
