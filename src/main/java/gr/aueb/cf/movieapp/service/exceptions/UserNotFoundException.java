package gr.aueb.cf.movieapp.service.exceptions;

public class UserNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String username) {
        super("User with username " + username + " does not exist.");
    }
}
