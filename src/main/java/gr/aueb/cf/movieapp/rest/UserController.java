package gr.aueb.cf.movieapp.rest;

import gr.aueb.cf.movieapp.dto.UserDto;
import gr.aueb.cf.movieapp.model.User;
import gr.aueb.cf.movieapp.service.IUserService;
import gr.aueb.cf.movieapp.service.exceptions.EntityNotFoundException;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private final IUserService userService;

    public UserController(IUserService userService){
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        UserDto userDto1;
        try {
            User user = userService.registerUser(userDto);
            userDto1 = entityToDto(user);
        } catch (InstanceAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> userList = userService.getAllUsers();
        if (userList.isEmpty()) {
            return new ResponseEntity<>(userList, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @RequestMapping(value = "user/favorites/{imdbID}", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> addFavorite(@RequestBody UserDto userDto, @PathVariable("imdbID") String imdbID) {
        // User login + User authorization >>>> TBI
        try {
            User loggedUser = userService.getUserByUsername(userDto.getUsername());
            loggedUser.addFavorite(imdbID);
            UserDto userDto1 = entityToDto(loggedUser);
            userService.updateUser(userDto1);
        } catch (InstanceAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user/favorites/{username}")
    public ResponseEntity<List<String>> getFavorites(@PathVariable("username") String username) {
        // User login + User authorization >>>> TBI
        User loggedUser = userService.getUserByUsername(username);
        List<String> favorites = loggedUser.getFavoriteList();
        return new ResponseEntity<>(favorites, HttpStatus.OK);
    }

    @RequestMapping(value = "user/addmovie/{imdbID}", method = RequestMethod.DELETE)
    public ResponseEntity<UserDto> deleteFavorite(@RequestBody UserDto userDto, @PathVariable("imdbID") String imdbID) {
        // User login + User authorization >>>> TBI
        User loggedUser = userService.getUserByUsername(userDto.getUsername());
        loggedUser.removeFavorite(imdbID);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    private UserDto entityToDto(User user){
        return new UserDto(user.getId(), user.getUsername(), user.getPassword(), user.getFavoriteList());
    }
}
