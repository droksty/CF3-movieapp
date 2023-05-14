package gr.aueb.cf.movieapp.repository;

import gr.aueb.cf.movieapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;
import java.util.List;

public interface IUserRepository extends MongoRepository<User, Long> {
    User findUserByUsername(String username);
    User findUserById(BigInteger id);
    List<User> findByUsernameStartingWith(String username);


}
