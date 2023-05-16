package gr.aueb.cf.movieapp.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Username already exists")
public class UserAlreadyExistsException extends Exception {
    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException(String username) {
        super("User with username: \"" + username + "\" already exists.");
    }
}
