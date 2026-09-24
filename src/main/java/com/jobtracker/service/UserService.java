package com.jobtracker.service;

import com.jobtracker.entity.User;
import com.jobtracker.exception.DuplicateEmailException;
import com.jobtracker.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {

        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {

            throw new DuplicateEmailException(
                    "Email already exists"
            );
        }

        // Encrypt password before saving
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean checkPassword(
            String rawPassword,
            String encodedPassword) {

        return passwordEncoder.matches(
                rawPassword,
                encodedPassword
        );
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}