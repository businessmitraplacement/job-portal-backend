package com.decoder.service;

import com.decoder.dto.UserProfileDTO;
import com.decoder.model.User;
import com.decoder.model.UserProfile;
import com.decoder.repository.UserProfileRepository;
import com.decoder.repository.UserRepository;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserProfileRepository profileRepo;

    @Autowired
    private UserProfileRepository profileRepository;

    @Override
    public UserProfileDTO getProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserProfile profile = profileRepository.findByUser(user)
                .orElseGet(() -> {
                    UserProfile emptyProfile = new UserProfile();
                    emptyProfile.setUser(user);
                    return emptyProfile;
                });

        return convertToDTO(profile);
    }

    @Override
    public UserProfileDTO saveOrUpdateProfileByEmail(String email, UserProfileDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserProfile profile = profileRepository.findByUser(user)
                .orElseGet(() -> {
                    UserProfile newProfile = new UserProfile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        profile.setName(dto.getName());
        profile.setGender(dto.getGender());
        profile.setDob(dto.getDob());
        profile.setLocation(dto.getLocation());
        profile.setPhone(dto.getPhone());
        profile.setEmail(dto.getEmail());

        return convertToDTO(profileRepository.save(profile));
    }

    private UserProfileDTO convertToDTO(UserProfile profile) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setName(profile.getName());
        dto.setGender(profile.getGender());
        dto.setDob(profile.getDob());
        dto.setLocation(profile.getLocation());
        dto.setPhone(profile.getPhone());
        dto.setEmail(profile.getEmail());
        return dto;
    }
    
//    @Override
//    public void updateProfileImageByEmail(String email, String fileName) {
//        UserProfile profile = profileRepo.findByEmail(email)
//            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        profile.setProfileImage(fileName); // assuming your entity has this field
//        profileRepo.save(profile);
//    }
    
    @Override
    public void updateProfileImageByEmail(String email, String fileName) {
        UserProfile profile = profileRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Directory where images are stored
        String uploadDir = "uploads/";

        // Delete old image if exists
        String oldFileName = profile.getProfileImage();
        if (oldFileName != null) {
            File oldFile = new File(uploadDir + oldFileName);
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }

        // Update with new image
        profile.setProfileImage(fileName);
        profileRepo.save(profile);
    }

}
