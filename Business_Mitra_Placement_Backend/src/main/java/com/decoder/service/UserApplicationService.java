package com.decoder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.decoder.dto.UserApplicationResponse;
import com.decoder.exception.DuplicateApplicationException;
import com.decoder.model.Job;
import com.decoder.model.Recruiter;
import com.decoder.model.UserApplication;
import com.decoder.repository.JobRepository;
import com.decoder.repository.RecruiterRepository;
import com.decoder.repository.UserApplicationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserApplicationService {

    @Autowired
    private UserApplicationRepository repository;
    
    @Autowired
    private UserApplicationRepository applicationRepository;
    
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    public UserApplication applyForJob(Long jobId, Long userId) {
    	
    	 boolean alreadyApplied = repository.existsByUserIdAndJob_Id(userId, jobId);
    	    if (alreadyApplied) {
    	        throw new DuplicateApplicationException("You have already applied for this job.");
    	    }
    	    
    	    // Fetch the job entity
            Job job = jobRepository.findById(jobId)
                    .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));
    	
        UserApplication application = new UserApplication();
//        application.setJobId(jobId);
        application.setJob(job);
        application.setUserId(userId);
        application.setAppliedDate(LocalDate.now());
        application.setStatus("Pending");
        return repository.save(application);
    }

    
    public List<UserApplicationResponse> getUserApplications(Long userId) {
        List<UserApplication> applications = applicationRepository.findByUserId(userId);

        return applications.stream().map(app -> {
            Job job = app.getJob();
//                    .orElseThrow(() -> new RuntimeException("Job not found with ID: " + app.getJobId()));

//            Recruiter recruiter = recruiterRepository.findById(job.getId())
//                    .orElseThrow(() -> new RuntimeException("Recruiter not found with ID: " + job.getId()));
            
            Recruiter recruiter = job.getCompanyId();
            if (recruiter == null) {
                throw new RuntimeException("Recruiter not found for job ID: " + job.getId());
            }

            return new UserApplicationResponse(
                    recruiter.getName(),
                    job.getTitle(),
                    job.getLocation(),
                    app.getAppliedDate(),
                    app.getStatus()
            );
        }).collect(Collectors.toList());
    }

}