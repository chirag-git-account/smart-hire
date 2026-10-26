package com.smarthire.dto;

public class AdminStatsDto {
    private Long totalUsers;
    private Long totalJobSeekers;
    private Long totalRecruiters;
    private Long totalCompanies;
    private Long totalJobs;
    private Long totalApplications;
    private Long totalAiAnalyses;

    public AdminStatsDto() {}

    public AdminStatsDto(Long totalUsers, Long totalJobSeekers, Long totalRecruiters, Long totalCompanies, Long totalJobs, Long totalApplications, Long totalAiAnalyses) {
        this.totalUsers = totalUsers;
        this.totalJobSeekers = totalJobSeekers;
        this.totalRecruiters = totalRecruiters;
        this.totalCompanies = totalCompanies;
        this.totalJobs = totalJobs;
        this.totalApplications = totalApplications;
        this.totalAiAnalyses = totalAiAnalyses;
    }

    public static AdminStatsDtoBuilder builder() {
        return new AdminStatsDtoBuilder();
    }

    public static class AdminStatsDtoBuilder {
        private Long totalUsers;
        private Long totalJobSeekers;
        private Long totalRecruiters;
        private Long totalCompanies;
        private Long totalJobs;
        private Long totalApplications;
        private Long totalAiAnalyses;

        public AdminStatsDtoBuilder totalUsers(Long totalUsers) { this.totalUsers = totalUsers; return this; }
        public AdminStatsDtoBuilder totalJobSeekers(Long totalJobSeekers) { this.totalJobSeekers = totalJobSeekers; return this; }
        public AdminStatsDtoBuilder totalRecruiters(Long totalRecruiters) { this.totalRecruiters = totalRecruiters; return this; }
        public AdminStatsDtoBuilder totalCompanies(Long totalCompanies) { this.totalCompanies = totalCompanies; return this; }
        public AdminStatsDtoBuilder totalJobs(Long totalJobs) { this.totalJobs = totalJobs; return this; }
        public AdminStatsDtoBuilder totalApplications(Long totalApplications) { this.totalApplications = totalApplications; return this; }
        public AdminStatsDtoBuilder totalAiAnalyses(Long totalAiAnalyses) { this.totalAiAnalyses = totalAiAnalyses; return this; }

        public AdminStatsDto build() {
            return new AdminStatsDto(totalUsers, totalJobSeekers, totalRecruiters, totalCompanies, totalJobs, totalApplications, totalAiAnalyses);
        }
    }

    public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }

    public Long getTotalJobSeekers() { return totalJobSeekers; }
    public void setTotalJobSeekers(Long totalJobSeekers) { this.totalJobSeekers = totalJobSeekers; }

    public Long getTotalRecruiters() { return totalRecruiters; }
    public void setTotalRecruiters(Long totalRecruiters) { this.totalRecruiters = totalRecruiters; }

    public Long getTotalCompanies() { return totalCompanies; }
    public void setTotalCompanies(Long totalCompanies) { this.totalCompanies = totalCompanies; }

    public Long getTotalJobs() { return totalJobs; }
    public void setTotalJobs(Long totalJobs) { this.totalJobs = totalJobs; }

    public Long getTotalApplications() { return totalApplications; }
    public void setTotalApplications(Long totalApplications) { this.totalApplications = totalApplications; }

    public Long getTotalAiAnalyses() { return totalAiAnalyses; }
    public void setTotalAiAnalyses(Long totalAiAnalyses) { this.totalAiAnalyses = totalAiAnalyses; }
}
