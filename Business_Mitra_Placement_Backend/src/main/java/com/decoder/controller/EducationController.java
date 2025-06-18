package com.decoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.decoder.model.Education;
import com.decoder.model.User;
import com.decoder.repository.EducationRepository;
import com.decoder.repository.UserRepository;

@RestController
@RequestMapping("/api/education")
@CrossOrigin(origins = "http://localhost:5173") // adjust frontend origin if needed
public class EducationController {

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> saveEducation(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody Education education) {

        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                     .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        education.setUser(user);
        Education savedEducation = educationRepository.save(education);

        return ResponseEntity.ok(savedEducation);
    }

    @GetMapping
    public ResponseEntity<?> getEducations(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                     .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Education> educations = educationRepository.findByUser(user);
        return ResponseEntity.ok(educations);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEducation(@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable Long id,
                                             @RequestBody Education updatedEducation) {

        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                     .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Education existingEducation = educationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Education not found"));

        if (!existingEducation.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        existingEducation.setEducation(updatedEducation.getEducation());
        existingEducation.setUniversity(updatedEducation.getUniversity());
        existingEducation.setCourse(updatedEducation.getCourse());
        existingEducation.setSpecialization(updatedEducation.getSpecialization());
        existingEducation.setCourseType(updatedEducation.getCourseType());
        existingEducation.setStartYear(updatedEducation.getStartYear());
        existingEducation.setEndYear(updatedEducation.getEndYear());
        existingEducation.setGrading(updatedEducation.getGrading());
        existingEducation.setMarks(updatedEducation.getMarks());

        educationRepository.save(existingEducation);

        return ResponseEntity.ok(existingEducation);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEducation(@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable Long id) {

        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                     .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Education education = educationRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Education not found"));

        // Check if the logged-in user owns this education record
        if (!education.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        educationRepository.delete(education);
        return ResponseEntity.ok("Education record deleted successfully.");
    }
}

