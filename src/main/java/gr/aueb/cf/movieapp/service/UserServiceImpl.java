package gr.aueb.cf.movieapp.service;

import gr.aueb.cf.movieapp.dto.UserDTO;
import gr.aueb.cf.movieapp.model.User;
import gr.aueb.cf.movieapp.repository.UserRepository;
import gr.aueb.cf.movieapp.service.exceptions.UserAlreadyExistsException;
import gr.aueb.cf.movieapp.service.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // Public API
    @Transactional
    @Override
    public User registerUser(UserDTO userDTO) {
        return userRepository.save(convertToUser(userDTO));
    }


    @Transactional
    @Override
    public User addToFavorites(UserDTO userDTO, Object imdbId) {

        User user = convertToUser(userDTO);
        user.addFavorite(imdbId);
        userRepository.save(user);

        return user;
    }


    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public boolean userAlreadyExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public List<Object> getUserFavorites(String username) {
        //return userRepository.findByUsername(username).getFavoriteList();
        return getUser(username).getFavoriteList();
    }


    // Util
    private User convertToUser(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getUsername(), userDTO.getPassword(), userDTO.getFavoriteList());
    }
}
