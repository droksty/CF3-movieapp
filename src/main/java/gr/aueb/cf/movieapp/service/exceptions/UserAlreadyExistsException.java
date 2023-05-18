package gr.aueb.cf.movieapp.service.exceptions;

public class UserAlreadyExistsException extends Exception {
    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException(String username) {
        super("User with username: \"" + username + "\" already exists.");
    }
}
