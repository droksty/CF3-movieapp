package gr.aueb.cf.movieapp.repository;

import gr.aueb.cf.movieapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, Long> {
    User findUserByUsername(String username);
//    User findUserById(String id);


}
