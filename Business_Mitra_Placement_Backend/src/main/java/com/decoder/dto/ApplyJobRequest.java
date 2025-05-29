package com.decoder.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyJobRequest {
    private Long jobId;
    
    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
}
