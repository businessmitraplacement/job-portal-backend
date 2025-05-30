package com.decoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.decoder.config.JwtUtil;
import com.decoder.dto.UserProfileDTO;
import com.decoder.service.UserProfileService;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService profileService;

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
}
