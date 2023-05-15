package gr.aueb.cf.movieapp.service.exceptions;


import java.math.BigInteger;

public class EntityNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(Class<?> entityClass, String id) {
        super("Entity " + entityClass.getSimpleName() + " with id " + id + " does not exist");
    }
}
