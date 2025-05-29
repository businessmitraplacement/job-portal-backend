package com.decoder.service;

import com.decoder.model.User;
import com.decoder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email already registered";
        }

        if (userRepository.findByMobile(user.getMobile()).isPresent()) {
            return "Mobile number already registered";
        }

        userRepository.save(user);
        return "User registered successfully";
    }

    public Optional<User> login(String username, String password) {
        Optional<User> user = userRepository.findByEmailOrMobile(username, username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }

    public boolean forgotPassword(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
