package com.jobtracker.dto;

public class DashboardResponse {

    private long totalApplications;
    private long applied;
    private long interview;
    private long rejected;
    private long selected;

    public DashboardResponse(
            long totalApplications,
            long applied,
            long interview,
            long rejected,
            long selected) {

        this.totalApplications = totalApplications;
        this.applied = applied;
        this.interview = interview;
        this.rejected = rejected;
        this.selected = selected;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public long getApplied() {
        return applied;
    }

    public long getInterview() {
        return interview;
    }

    public long getRejected() {
        return rejected;
    }

    public long getSelected() {
        return selected;
    }
}