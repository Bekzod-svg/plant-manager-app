package spring.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.user.entity.User;
import spring.user.repository.UserRepository;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            user.setName(userDetails.getName());
            user.setSurname(userDetails.getSurname());
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User findFirstByUsername(String username) {
        return userRepository.findFirstByUsername(username);
    }
}
