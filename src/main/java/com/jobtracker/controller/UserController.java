package com.jobtracker.controller;

import com.jobtracker.entity.User;
import com.jobtracker.exception.GlobalExceptionHandler;
import com.jobtracker.service.JwtService;
import com.jobtracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(
            UserService userService,
            JwtService jwtService) {

        this.userService = userService;
        this.jwtService = jwtService;
    }

    // Add a user
    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable Long id) {

        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Register a user
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody LoginRequest loginRequest) {

        var userOptional =
                userService.findByEmail(loginRequest.getEmail());

        // Check if email exists
        if (userOptional.isEmpty()) {

            return ResponseEntity
                    .status(401)
                    .body(
                            new GlobalExceptionHandler.ErrorResponse(
                                    401,
                                    "Invalid email or password"
                            )
                    );
        }

        User user = userOptional.get();

        // Check password
        boolean passwordCorrect =
                userService.checkPassword(
                        loginRequest.getPassword(),
                        user.getPassword()
                );

        if (!passwordCorrect) {

            return ResponseEntity
                    .status(401)
                    .body(
                            new GlobalExceptionHandler.ErrorResponse(
                                    401,
                                    "Invalid email or password"
                            )
                    );
        }

        // Generate JWT token
        String token =
                jwtService.generateToken(
                        user.getId(),
                        user.getEmail()
                );

        // Successful login response
        return ResponseEntity.ok(
                new LoginResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        token
                )
        );
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    // Login request
    public static class LoginRequest {

        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // Login response
    public static class LoginResponse {

        private Long id;
        private String name;
        private String email;
        private String token;

        public LoginResponse(
                Long id,
                String name,
                String email,
                String token) {

            this.id = id;
            this.name = name;
            this.email = email;
            this.token = token;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getToken() {
            return token;
        }
    }
}