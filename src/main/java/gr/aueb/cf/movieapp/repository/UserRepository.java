package gr.aueb.cf.movieapp.repository;

import gr.aueb.cf.movieapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.function.Predicate;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);
    boolean existsByUsername(String username);

}
