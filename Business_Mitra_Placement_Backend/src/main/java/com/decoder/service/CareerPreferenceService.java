package com.decoder.service;

import com.decoder.dto.CareerPreferenceDTO;
import com.decoder.model.CareerPreference;
import com.decoder.model.User;
import com.decoder.repository.CareerPreferenceRepository;
import com.decoder.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CareerPreferenceService {

    @Autowired
    private CareerPreferenceRepository careerPrefRepo;

    @Autowired
    private UserRepository userRepository;

    public CareerPreferenceDTO getPreferenceByUserEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return careerPrefRepo.findByUser(user)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public CareerPreferenceDTO saveOrUpdatePreference(String email, CareerPreferenceDTO dto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        CareerPreference preference = careerPrefRepo.findByUser(user).orElse(new CareerPreference());
        preference.setUser(user);
        preference.setJobTypeList(dto.getJobType());
        preference.setAvailability(dto.getAvailability());
        preference.setLocationList(dto.getLocations());

        return convertToDTO(careerPrefRepo.save(preference));
    }

    private CareerPreferenceDTO convertToDTO(CareerPreference entity) {
        CareerPreferenceDTO dto = new CareerPreferenceDTO();
        dto.setJobType(entity.getJobTypeList());
        dto.setAvailability(entity.getAvailability());
        dto.setLocations(entity.getLocationList());
        return dto;
    }
}

