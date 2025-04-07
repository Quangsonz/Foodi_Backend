package G9.Foodi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import G9.Foodi.dto.UserDto;
import G9.Foodi.model.User;
import G9.Foodi.model.User.Role;
import G9.Foodi.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("User already exists with email: " + userDto.getEmail());
        }
        
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Mã hóa password
        user.setPhotoURL(userDto.getPhotoURL());
        user.setRole(Role.USER); // Default role
        
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean isAdmin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        
        return Role.ADMIN.equals(user.getRole());
    }

    @Override
    public User makeAdmin(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        
        user.setRole(Role.ADMIN);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserDto userDto) {
        User existingUser = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userDto.getEmail()));
        
        // Update user information
        existingUser.setName(userDto.getName());
        if (userDto.getPhotoURL() != null) {
            existingUser.setPhotoURL(userDto.getPhotoURL());
        }
        
        // Only update password if a new one is provided
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        
        return userRepository.save(existingUser);
    }
}