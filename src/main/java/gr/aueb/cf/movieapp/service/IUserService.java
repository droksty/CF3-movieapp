package gr.aueb.cf.movieapp.service;

import gr.aueb.cf.movieapp.dto.UserDto;
import gr.aueb.cf.movieapp.model.User;
import gr.aueb.cf.movieapp.service.exceptions.EntityNotFoundException;

import javax.management.InstanceAlreadyExistsException;
import java.math.BigInteger;
import java.util.List;

public interface IUserService {
    User registerUser(UserDto userDto) throws InstanceAlreadyExistsException;
//    void addFavoriteMovie(UserDto userDto, String imdbId);
    List<User> getAllUsers();
    User getUserByUsername(String username);
    User getById(BigInteger id);
    User updateUser(UserDto dto) throws EntityNotFoundException;

}
