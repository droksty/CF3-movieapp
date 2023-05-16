package gr.aueb.cf.movieapp.service;

import gr.aueb.cf.movieapp.dto.UserDTO;
import gr.aueb.cf.movieapp.model.User;
import gr.aueb.cf.movieapp.service.exceptions.UserNotFoundException;

import java.util.List;

public interface IUserService {

    User registerUser(UserDTO userDTO);
    //User changePassword(UserDTO userDTO);
    //void deleteUser(String username);
    User getUser(String username);
    User addToFavorites(UserDTO userDTO, Object imdbId);
    List<Object> getUserFavorites(String username);
    boolean userAlreadyExists(String username);

}
