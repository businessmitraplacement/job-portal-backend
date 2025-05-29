package com.decoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.decoder.model.UserApplication;

import java.util.List;

public interface UserApplicationRepository extends JpaRepository<UserApplication, Long> {
    List<UserApplication> findByUserId(Long userId);

//	boolean existsByUserIdAndJobId(Long userId, Long jobId);
    boolean existsByUserIdAndJob_Id(Long userId, Long jobId);

}