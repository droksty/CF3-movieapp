package gr.aueb.cf.movieapp.rest;

import gr.aueb.cf.movieapp.dto.UserDTO;
import gr.aueb.cf.movieapp.model.User;
import gr.aueb.cf.movieapp.repository.UserRepository;
import gr.aueb.cf.movieapp.service.IUserService;
import gr.aueb.cf.movieapp.service.exceptions.UserAlreadyExistsException;
import gr.aueb.cf.movieapp.service.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }


    @PostMapping(value = "/signup")
    public ResponseEntity<User> createUser(@RequestBody UserDTO dto) throws UserAlreadyExistsException {

        if (userService.userAlreadyExists(dto.getUsername())) throw new UserAlreadyExistsException(dto.getUsername());

        User newUser = userService.registerUser(dto);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }


    @PutMapping(value = "/favorites/{username}")
    public ResponseEntity<User> addFavorite(@PathVariable String username, @RequestBody Object imdbID) {

        User currentUser = userService.getUser(username);
        if (currentUser == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        UserDTO currentUserDTO = new UserDTO(
                currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getPassword(),
                currentUser.getFavoriteList()
        );

        User updatedUser = userService.addToFavorites(currentUserDTO, imdbID);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


    @GetMapping("/favorites")
    public ResponseEntity<List<Object>> getFavorites(@RequestBody String username) {

        User currentUser = userService.getUser(username);

        List<Object> favorites = userService.getUserFavorites(currentUser.getUsername());
        return new ResponseEntity<>(favorites, HttpStatus.OK);
    }


    //
    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity handleUserAlreadyExistsException(UserAlreadyExistsException userAlreadyExistsException) {
        userAlreadyExistsException.printStackTrace();
        return new ResponseEntity(userAlreadyExistsException.getMessage(), HttpStatus.CONFLICT);
    }
}
