package com.decoder.service;

import com.decoder.dto.UserProfileDTO;
import com.decoder.model.User;

public interface UserProfileService {
	UserProfileDTO getProfileByEmail(String email);
	UserProfileDTO saveOrUpdateProfileByEmail(String email, UserProfileDTO DTO);

	 void updateProfileImageByEmail(String email, String fileName); 
}
