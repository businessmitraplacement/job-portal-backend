package com.decoder.controller;

import com.decoder.config.JwtUtil;
import com.decoder.model.AuthResponse;
import com.decoder.model.RecruiterLoginRequest;
import com.decoder.model.RegisterRequest;
import com.decoder.model.User;
import com.decoder.model.UserLoginRequest;
import com.decoder.repository.UserRepository;
import com.decoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("userAuthenticationManager")
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(new AuthResponse("Email already exists", null));
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new AuthResponse("Passwords do not match", null));
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepo.save(user);

        return ResponseEntity.ok(new AuthResponse("Registration successful", null));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
        try {
            Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Extract user details from authentication
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails.getUsername(), "USER");

            return ResponseEntity.ok(new AuthResponse("Login successful", jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Invalid credentials", null));
        }
    }



    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");

        if (userService.forgotPassword(email)) {
            return ResponseEntity.ok(Map.of("message", "Password reset link sent to your email"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Email not found"));
        }
    }
}
