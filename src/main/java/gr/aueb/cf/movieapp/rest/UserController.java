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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


    @PutMapping(value = "/movieapp/favorites")
    public ResponseEntity<User> addFavorite(@RequestBody Object imdbID) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentLoggedInUser = userService.getUser(authentication.getName());

        if (currentLoggedInUser == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        UserDTO currentUserDTO = new UserDTO(
                currentLoggedInUser.getId(),
                currentLoggedInUser.getUsername(),
                currentLoggedInUser.getPassword(),
                currentLoggedInUser.getFavoriteList()
        );

        User updatedUser = userService.addToFavorites(currentUserDTO, imdbID);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


    @GetMapping("/movieapp/favorites")
    public ResponseEntity<List<Object>> getFavorites() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentLoggedInUser = userService.getUser(authentication.getName());

        if (currentLoggedInUser == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        User currentUser = userService.getUser(username);

//        List<Object> favorites = userService.getUserFavorites(currentLoggedInUser.getUsername());
        List<Object> favorites = currentLoggedInUser.getFavoriteList();
        return new ResponseEntity<>(favorites, HttpStatus.OK);
    }

}
