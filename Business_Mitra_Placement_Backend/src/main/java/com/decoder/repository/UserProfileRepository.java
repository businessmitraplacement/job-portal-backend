package com.decoder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.decoder.model.CareerPreference;
import com.decoder.model.User;
import com.decoder.model.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

	Optional<UserProfile> findByUser(User user);
}


