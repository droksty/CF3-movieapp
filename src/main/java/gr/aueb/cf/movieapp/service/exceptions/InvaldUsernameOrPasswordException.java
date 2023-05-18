package gr.aueb.cf.movieapp.service.exceptions;

public class InvaldUsernameOrPasswordException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvaldUsernameOrPasswordException() {
        super("Invalid username or password");
    }
}
