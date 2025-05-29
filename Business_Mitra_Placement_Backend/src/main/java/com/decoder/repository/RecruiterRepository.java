package com.decoder.repository;

import com.decoder.model.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    Optional<Recruiter> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByName(String name);
}
