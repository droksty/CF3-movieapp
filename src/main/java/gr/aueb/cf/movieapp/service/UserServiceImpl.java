package gr.aueb.cf.movieapp.service;

import gr.aueb.cf.movieapp.dto.UserDto;
import gr.aueb.cf.movieapp.model.User;
import gr.aueb.cf.movieapp.repository.IUserRepository;
import gr.aueb.cf.movieapp.service.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService{

    private final IUserRepository iUserRepository;

    @Autowired
    public UserServiceImpl(IUserRepository userRepository) {
        this.iUserRepository = userRepository;
    }

    @Override
    public User registerUser(UserDto userDto) throws InstanceAlreadyExistsException {

        User existingUser = iUserRepository.findUserByUsername(userDto.getUsername());

        if (existingUser != null) {
            throw new InstanceAlreadyExistsException();
        }
        return iUserRepository.save(dtoToEntity(userDto));
    }

//    @Override
//    public void addFavoriteMovie(User user, String imdbId) {
//        User user = iUserRepository.findUserByUsername(dto.getUsername());
//        user.getFavoriteList().add(imdbId);
//        iUserRepository.save(user);
//    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;
        users = iUserRepository.findAll();
        return users;
    }

    @Override
    public User getUserByUsername(String username) {
        User user;
        user = iUserRepository.findUserByUsername(username);
        return user;
    }

    @Override
    public User getById(String id) {
        return iUserRepository.findUserById(id);
    }

    @Override
    public User updateUser(UserDto dto) throws EntityNotFoundException {
        User user = dtoToEntity(dto);
        if (user == null) throw new EntityNotFoundException(User.class, user.getId());

        return iUserRepository.save(user);
    }

    private User dtoToEntity(UserDto dto) {
        return new User(dto.getId(), dto.getUsername(), dto.getPassword(), dto.getFavoriteMovies());
    }
}
