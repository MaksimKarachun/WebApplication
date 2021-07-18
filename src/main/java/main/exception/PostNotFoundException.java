package main.exception;

public class PostNotFoundException extends Exception {

    public PostNotFoundException(int id){
        super(String.format("Post with id %s not found", id));
    }
}
