package org.example.UI.exceptions;

// An exception to represent a case where a post doesn't exist in the posts page
public class PostNotFoundException  extends RuntimeException {
    public PostNotFoundException(String message) {
        super(message);
    }
}
