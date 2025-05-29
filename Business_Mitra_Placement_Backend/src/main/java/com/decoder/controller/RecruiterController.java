package com.decoder.controller;

import com.decoder.config.JwtUtil;
import com.decoder.model.JwtResponse;
import com.decoder.model.RecruiterLoginRequest;
import com.decoder.model.Recruiter;
import com.decoder.service.RecruiterService;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/recruiter")
public class RecruiterController {
	

    private RecruiterService service;
    
    @PostConstruct
    public void init() {
        System.out.println("RecruiterController loaded ✅");
    }

    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    @Qualifier("recruiterAuthenticationManager")
    private AuthenticationManager authenticationManager;
    

    public RecruiterController(RecruiterService service) {
        this.service = service;
    }

//    @Autowired
//    public RecruiterController(RecruiterService service, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
//        this.service = service;
//        this.jwtUtil = jwtUtil;
//        this.authenticationManager = authenticationManager;
//    }

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Recruiter> register(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("image") MultipartFile image
    ) {
        Recruiter recruiter = new Recruiter();
        recruiter.setName(name);
        recruiter.setEmail(email);
        recruiter.setPassword(password);

        try {
            if (image != null && !image.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename(); // Unique file name
                Path uploadPath = Paths.get("uploads");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath); // Create upload folder if not exist
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                recruiter.setImage(fileName); // Save only the file name to database
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        Recruiter savedRecruiter = service.register(recruiter);
        return ResponseEntity.ok(savedRecruiter);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RecruiterLoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtil.generateToken(loginRequest.getEmail(), "RECRUITER");

            Optional<Recruiter> optionalRecruiter = service.findByEmail(loginRequest.getEmail());
            if (optionalRecruiter.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recruiter not found");
            }

            Recruiter recruiter = optionalRecruiter.get();
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("email", recruiter.getEmail());
            response.put("recruiterName", recruiter.getName());
            response.put("recruiterImage", recruiter.getImage());
            response.put("type", "Bearer");

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid recruiter credentials");
        }
    }


    
    @PostMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmailAndName(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String name = request.get("name");

        boolean exists = service.isEmailOrNameExists(email, name);

        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);

        return ResponseEntity.ok(response);
    }
}
