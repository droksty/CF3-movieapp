package gr.aueb.cf.movieapp.repository;

import gr.aueb.cf.movieapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface IUserRepository extends MongoRepository<User, Long> {
    User findUserByUsername(String username);
    User findUserById(String id);
    List<User> findByUsernameStartingWith(String username);

//    @Query("{'username': ?0, 'password': ?1}")
//    boolean isUserValid(String username, String password);

}
