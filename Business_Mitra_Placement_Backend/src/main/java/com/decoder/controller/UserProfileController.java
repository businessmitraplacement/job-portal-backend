package com.decoder.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.decoder.config.JwtUtil;
import com.decoder.dto.UserProfileDTO;
import com.decoder.service.UserProfileService;

//import jakarta.persistence.criteria.Path;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService profileService;

    private static final String UPLOAD_DIR = "uploads/";
    
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/user_id")
    public ResponseEntity<UserProfileDTO> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
    	String email = userDetails.getUsername();
        return ResponseEntity.ok(profileService.getProfileByEmail(email));
    }

    @PostMapping
    public ResponseEntity<UserProfileDTO> saveOrUpdateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                                               @RequestBody UserProfileDTO dto) {
    	String email = userDetails.getUsername();
        return ResponseEntity.ok(profileService.saveOrUpdateProfileByEmail(email, dto));
    }
    
 // 🔥 Add this: Profile Image Upload Endpoint
    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadProfileImage(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestParam("profileImage") MultipartFile file) {
        String email = userDetails.getUsername();

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            // Ensure upload directory exists
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            // Create a unique file name
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

            // Save the file to disk
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.copy(file.getInputStream(), filePath);

            // Save image file name in database
            profileService.updateProfileImageByEmail(email, fileName);

            return ResponseEntity.ok("Image uploaded successfully");

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Could not upload image");
        }
    }
}
