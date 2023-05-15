package gr.aueb.cf.movieapp.rest;

import com.fasterxml.jackson.core.JsonParser;
import gr.aueb.cf.movieapp.dto.MovieDto;
import gr.aueb.cf.movieapp.dto.UserDto;
import gr.aueb.cf.movieapp.model.User;
import gr.aueb.cf.movieapp.service.IUserService;
import gr.aueb.cf.movieapp.service.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        try {
            User user = userService.registerUser(userDto);
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

    @RequestMapping(value = "user/favorites", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> addFavorite(@RequestBody MovieDto imdbDto) {

        // User login + User authorization >>>> TBI
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User loggedUser = userService.getUserByUsername(currentPrincipalName);
        UserDto dto = entityToDto(loggedUser);
        String imdbId = imdbDto.getImdbID();

        try {
            userService.addFavoriteMovie(loggedUser, imdbId);
        } catch (InstanceAlreadyExistsException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Movie with id " + imdbDto.getImdbID() + " added to " + loggedUser.getUsername() + "'s favorite list");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "user/favorites", method = RequestMethod.DELETE)
    public ResponseEntity<UserDto> deleteFavorite(@RequestBody MovieDto imdbDto) {

        // User login + User authorization >>>> TBI
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User loggeduser = userService.getUserByUsername(currentPrincipalName);
        UserDto dto = entityToDto(loggeduser);
        String imdbId = imdbDto.getImdbID();

        try {
            userService.removeFavoriteMovie(loggeduser, imdbId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Movie with id " + imdbDto.getImdbID() + " was removed from " + loggeduser.getUsername() + "'s favorite list");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("user/favorites/{username}")
    public ResponseEntity<List<String>> getFavorites(@PathVariable("username") String username) {
        // User login + User authorization >>>> TBI
        User loggedUser = userService.getUserByUsername(username);
        List<String> favorites = loggedUser.getFavoriteList();
        return new ResponseEntity<>(favorites, HttpStatus.OK);
    }



    //Get principal
    @GetMapping("/user/username")
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getName());
        return auth.getName();
    }


    private UserDto entityToDto(User user){
        return new UserDto(user.getId(), user.getUsername(), user.getPassword(), user.getFavoriteList());
    }
}
