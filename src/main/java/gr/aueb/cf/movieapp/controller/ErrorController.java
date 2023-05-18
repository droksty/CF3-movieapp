package gr.aueb.cf.movieapp.controller;

import gr.aueb.cf.movieapp.service.exceptions.InvaldUsernameOrPasswordException;
import gr.aueb.cf.movieapp.service.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(value = InvaldUsernameOrPasswordException.class)
    public ResponseEntity<String> badCredentials(InvaldUsernameOrPasswordException e) {
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
}
