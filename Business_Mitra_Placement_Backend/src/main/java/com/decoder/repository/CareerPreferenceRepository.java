package com.decoder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.decoder.model.CareerPreference;
import com.decoder.model.User;

public interface CareerPreferenceRepository extends JpaRepository<CareerPreference, Long> {
    Optional<CareerPreference> findByUser(User user);
}

