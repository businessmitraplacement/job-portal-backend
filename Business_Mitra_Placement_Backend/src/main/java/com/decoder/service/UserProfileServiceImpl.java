package com.decoder.service;

import com.decoder.dto.UserProfileDTO;
import com.decoder.model.User;
import com.decoder.model.UserProfile;
import com.decoder.repository.UserProfileRepository;
import com.decoder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserRepository userRepository;

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
}
