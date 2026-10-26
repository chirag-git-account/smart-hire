package com.smarthire.dto;

public class SeekerStatsDto {
    private Long totalApplications;
    private Long shortlistedCount;
    private Long interviewCount;
    private Long savedJobsCount;

    public SeekerStatsDto() {}

    public SeekerStatsDto(Long totalApplications, Long shortlistedCount, Long interviewCount, Long savedJobsCount) {
        this.totalApplications = totalApplications;
        this.shortlistedCount = shortlistedCount;
        this.interviewCount = interviewCount;
        this.savedJobsCount = savedJobsCount;
    }

    public static SeekerStatsDtoBuilder builder() {
        return new SeekerStatsDtoBuilder();
    }

    public static class SeekerStatsDtoBuilder {
        private Long totalApplications;
        private Long shortlistedCount;
        private Long interviewCount;
        private Long savedJobsCount;

        public SeekerStatsDtoBuilder totalApplications(Long totalApplications) { this.totalApplications = totalApplications; return this; }
        public SeekerStatsDtoBuilder shortlistedCount(Long shortlistedCount) { this.shortlistedCount = shortlistedCount; return this; }
        public SeekerStatsDtoBuilder interviewCount(Long interviewCount) { this.interviewCount = interviewCount; return this; }
        public SeekerStatsDtoBuilder savedJobsCount(Long savedJobsCount) { this.savedJobsCount = savedJobsCount; return this; }

        public SeekerStatsDto build() {
            return new SeekerStatsDto(totalApplications, shortlistedCount, interviewCount, savedJobsCount);
        }
    }

    public Long getTotalApplications() { return totalApplications; }
    public void setTotalApplications(Long totalApplications) { this.totalApplications = totalApplications; }

    public Long getShortlistedCount() { return shortlistedCount; }
    public void setShortlistedCount(Long shortlistedCount) { this.shortlistedCount = shortlistedCount; }

    public Long getInterviewCount() { return interviewCount; }
    public void setInterviewCount(Long interviewCount) { this.interviewCount = interviewCount; }

    public Long getSavedJobsCount() { return savedJobsCount; }
    public void setSavedJobsCount(Long savedJobsCount) { this.savedJobsCount = savedJobsCount; }
}
