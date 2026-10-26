package com.smarthire.dto;

import jakarta.validation.constraints.NotNull;

public class ApplyJobRequest {
    @NotNull(message = "Job ID is required")
    private Long jobId;

    private Long resumeId;

    private String coverLetter;
    private String coverNote;

    public ApplyJobRequest() {}

    public ApplyJobRequest(Long jobId, Long resumeId, String coverLetter, String coverNote) {
        this.jobId = jobId;
        this.resumeId = resumeId;
        this.coverLetter = coverLetter;
        this.coverNote = coverNote;
    }

    public static ApplyJobRequestBuilder builder() {
        return new ApplyJobRequestBuilder();
    }

    public static class ApplyJobRequestBuilder {
        private Long jobId;
        private Long resumeId;
        private String coverLetter;
        private String coverNote;

        public ApplyJobRequestBuilder jobId(Long jobId) { this.jobId = jobId; return this; }
        public ApplyJobRequestBuilder resumeId(Long resumeId) { this.resumeId = resumeId; return this; }
        public ApplyJobRequestBuilder coverLetter(String coverLetter) { this.coverLetter = coverLetter; return this; }
        public ApplyJobRequestBuilder coverNote(String coverNote) { this.coverNote = coverNote; return this; }

        public ApplyJobRequest build() {
            return new ApplyJobRequest(jobId, resumeId, coverLetter, coverNote);
        }
    }

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public Long getResumeId() { return resumeId; }
    public void setResumeId(Long resumeId) { this.resumeId = resumeId; }

    public String getCoverLetter() { return coverLetter != null ? coverLetter : coverNote; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    public String getCoverNote() { return coverNote != null ? coverNote : coverLetter; }
    public void setCoverNote(String coverNote) { this.coverNote = coverNote; }
}
