package com.nahid.main.service;

import com.nahid.main.model.User;
import com.nahid.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user in the system with secure password encryption.
     * 
     * <p>This method creates a new user account with the provided credentials.
     * The password is automatically encrypted using BCrypt hashing before being
     * stored in the database. New users are assigned the default "ROLE_USER"
     * role for standard application access.</p>
     * 
     * <p>Registration process:</p>
     * <ul>
     *   <li>Validates that the username is unique (not already taken)</li>
     *   <li>Encrypts the password using BCrypt for secure storage</li>
     *   <li>Assigns default user role (ROLE_USER)</li>
     *   <li>Persists the new user to the database</li>
     * </ul>
     * 
     * <p><strong>Security Note:</strong> Passwords are never stored in plain text.
     * BCrypt hashing is a one-way encryption that protects user credentials even
     * if the database is compromised.</p>
     *
     * @param username the desired username for the new account (must be unique)
     * @param password the plain text password (will be encrypted before storage)
     * @throws Exception if the username is already taken by another user
     * @see PasswordEncoder#encode(CharSequence)
     */
    public void registerUser(String username, String password) throws Exception {
        User user = userRepository.findByUsername(username);
        if(user != null){
            throw new Exception("Username is already taken");
        }

        user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }

}
