package com.smarthire.dto;

public class RecruiterStatsDto {
    private Long totalJobs;
    private Long totalApplications;
    private Long highMatchCount;

    public RecruiterStatsDto() {}

    public RecruiterStatsDto(Long totalJobs, Long totalApplications, Long highMatchCount) {
        this.totalJobs = totalJobs;
        this.totalApplications = totalApplications;
        this.highMatchCount = highMatchCount;
    }

    public static RecruiterStatsDtoBuilder builder() {
        return new RecruiterStatsDtoBuilder();
    }

    public static class RecruiterStatsDtoBuilder {
        private Long totalJobs;
        private Long totalApplications;
        private Long highMatchCount;

        public RecruiterStatsDtoBuilder totalJobs(Long totalJobs) { this.totalJobs = totalJobs; return this; }
        public RecruiterStatsDtoBuilder totalApplications(Long totalApplications) { this.totalApplications = totalApplications; return this; }
        public RecruiterStatsDtoBuilder highMatchCount(Long highMatchCount) { this.highMatchCount = highMatchCount; return this; }

        public RecruiterStatsDto build() {
            return new RecruiterStatsDto(totalJobs, totalApplications, highMatchCount);
        }
    }

    public Long getTotalJobs() { return totalJobs; }
    public void setTotalJobs(Long totalJobs) { this.totalJobs = totalJobs; }

    public Long getTotalApplications() { return totalApplications; }
    public void setTotalApplications(Long totalApplications) { this.totalApplications = totalApplications; }

    public Long getHighMatchCount() { return highMatchCount; }
    public void setHighMatchCount(Long highMatchCount) { this.highMatchCount = highMatchCount; }
}
