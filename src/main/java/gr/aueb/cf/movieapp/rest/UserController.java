package gr.aueb.cf.movieapp.rest;

import gr.aueb.cf.movieapp.model.User;
import gr.aueb.cf.movieapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/movieapp")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/create")
    public ResponseEntity<User> createUser() {
        User newUser = new User("user1", "pass1", new ArrayList<>());
        User existingUser = userRepository.findUserByUsername(newUser.getUsername());

        if (existingUser != null) {
            System.out.println("USER ALREADY EXISTS");
            return new ResponseEntity<>(existingUser, HttpStatus.BAD_REQUEST);
        }

        userRepository.save(newUser);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }


    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    @ResponseBody // ?
    public ResponseEntity<User> addFavorite(@RequestBody Object imdbID) {
        // User login + User authorization >>>> TBI
        User loggedUser = userRepository.findUserByUsername("user1");

        if (loggedUser.getFavoriteList().contains(imdbID)) {
            System.out.println("Movie already exists in favorites");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        loggedUser.addFavorite(imdbID);
        User updatedUser = userRepository.save(loggedUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<Object>> getFavorites() {
        // User login + User authorization >>>> TBI
        User loggedUser = userRepository.findUserByUsername("user1");
        List<Object> favorites = loggedUser.getFavoriteList();
        return new ResponseEntity<>(favorites, HttpStatus.OK);
    }


}
