package koodjohvi.buyit.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Optional<User> findUserById(String id) {
        return userRepository.findById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(String id, User updatedUser){
        return userRepository.findById(id).map(existingUser ->{
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setRole(updatedUser.getRole());
            existingUser.setAvatar(updatedUser.getAvatar());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found"));
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
