package com.decoder.service;

import com.decoder.dto.JobDTO;
import com.decoder.exception.ResourceNotFoundException;
import com.decoder.mapper.JobMapper;
import com.decoder.model.Job;
import com.decoder.repository.JobRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }
    
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
    
    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }
//    public void deleteJobById(Long id) {
//        if (!jobRepository.existsById(id)) {
//            throw new RuntimeException("Job not found with ID: " + id);
//        }
//        jobRepository.deleteById(id);
//    }
    
    public void deleteJobById(Long id) {
        if (!jobRepository.existsById(id)) {
            throw new ResourceNotFoundException("Job not found with id " + id);
        }
        jobRepository.deleteById(id);
    }
    
    public void updateJobVisibility(Long id, boolean visible) {
        Job job = jobRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setVisible(visible);
        jobRepository.save(job);
    }
    

    public List<JobDTO> getJobsByRecruiter(String recruiterEmail) {
        return jobRepository.findByRecruiterEmail(recruiterEmail)
                .stream()
                .map(JobMapper::toDTO)
                .collect(Collectors.toList());
    }

}
