package com.decoder.mapper;

import com.decoder.dto.JobDTO;
import com.decoder.model.Job;
import com.decoder.model.Recruiter;

public class JobMapper {

    public static JobDTO toDTO(Job job) {
        JobDTO dto = new JobDTO();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setCategory(job.getCategory());
        dto.setLocation(job.getLocation());
        dto.setLevel(job.getLevel());
        dto.setSalary(job.getSalary());
        dto.setApplicants(job.getApplicants());
        dto.setDate(job.getDate());
        dto.setVisible(job.isVisible());

        Recruiter recruiter = job.getCompanyId();
        if (recruiter != null) {
            dto.setCompanyName(recruiter.getName());
            dto.setCompanyId(recruiter.getId()); 
            dto.setCompanyImage(recruiter.getImage());
           

        }

        return dto;
    }
}
