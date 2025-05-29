package com.decoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.decoder.dto.ApplyJobRequest;
import com.decoder.dto.UserApplicationResponse;
import com.decoder.model.User;
import com.decoder.model.UserApplication;
import com.decoder.repository.UserRepository;
import com.decoder.service.UserApplicationService;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:5173")
public class UserApplicationController {

    @Autowired
    private UserApplicationService service;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public UserApplication applyForJob(@RequestBody ApplyJobRequest request, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        return service.applyForJob(request.getJobId(), user.getId());
    }

//    @GetMapping
//    public List<UserApplication> getUserApplications(Authentication authentication) {
//        String username = authentication.getName();
//        User user = userRepository.findByEmail(username)
//                        .orElseThrow(() -> new RuntimeException("User not found"));
//        return service.getUserApplications(user.getId());
//    }
    @GetMapping
    public List<UserApplicationResponse> getUserApplications(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        return service.getUserApplications(user.getId());
    }
    
}