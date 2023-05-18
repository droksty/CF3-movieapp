package gr.aueb.cf.movieapp.service;

import gr.aueb.cf.movieapp.dto.UserCreationDTO;
import gr.aueb.cf.movieapp.model.User;

import java.util.List;

public interface IUserService {

    User registerUser(UserCreationDTO userDTO);
    //User changePassword(UserDTO userDTO);
    //void deleteUser(String username);
    User getUser(String username);
    User addToFavorites(UserCreationDTO userDTO, Object imdbId);
    User updateUser(String username, Object imdbId);
    List<Object> getUserFavorites(String username);
    boolean userAlreadyExists(String username);

}
