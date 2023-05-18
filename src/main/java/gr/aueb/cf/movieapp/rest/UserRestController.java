package gr.aueb.cf.movieapp.rest;

import gr.aueb.cf.movieapp.dto.UserDTO;
import gr.aueb.cf.movieapp.dto.UserCreationDTO;
import gr.aueb.cf.movieapp.model.User;
import gr.aueb.cf.movieapp.service.IUserService;
import gr.aueb.cf.movieapp.service.exceptions.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserRestController {

    private final IUserService userService;

    @Autowired
    public UserRestController(IUserService userService) {
        this.userService = userService;
    }


    @PostMapping(value = "/signup")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreationDTO userDTO) throws UserAlreadyExistsException {

        if (userService.userAlreadyExists(userDTO.getUsername())) {
            throw new UserAlreadyExistsException(userDTO.getUsername());
        }

        User registeredUser = userService.registerUser(userDTO);
        UserDTO registeredUserDTO = new UserDTO(registeredUser.getUsername(), registeredUser.getFavoriteList());

        return new ResponseEntity<>(registeredUserDTO, HttpStatus.CREATED);
    }


    @PutMapping(value = "/movieapp/favorites")
    public ResponseEntity<UserDTO> addFavorite(@RequestBody Object imdbID) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentLoggedInUser = userService.getUser(authentication.getName());

        if (currentLoggedInUser == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User updatedUser = userService.updateUser(currentLoggedInUser.getUsername(), imdbID);
        UserDTO updatedUserDTO = new UserDTO(updatedUser.getUsername(), updatedUser.getFavoriteList());

        return new ResponseEntity<>(updatedUserDTO, HttpStatus.OK);
    }


    @GetMapping("/movieapp/favorites")
    public ResponseEntity<UserDTO> getFavorites() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentLoggedInUser = userService.getUser(authentication.getName());

        if (currentLoggedInUser == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User user = userService.getUser(currentLoggedInUser.getUsername());
        UserDTO userDTO = new UserDTO(user.getUsername(), user.getFavoriteList());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

}
