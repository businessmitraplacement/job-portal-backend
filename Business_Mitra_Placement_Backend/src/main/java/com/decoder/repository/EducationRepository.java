package com.decoder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.decoder.model.Education;
import com.decoder.model.User;

public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findByUser(User user);
}

