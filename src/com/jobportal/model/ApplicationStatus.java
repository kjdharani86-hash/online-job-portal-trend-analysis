package com.jobportal.model;

/**
 * Enumeration representing the progression lifecycle of a job application.
 * Scenario 22: Online Job Portal and Employment Trend Analysis System
 * Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
 */
public enum ApplicationStatus {
    APPLIED("Applied - Under Initial Review"),
    SHORTLISTED("Shortlisted - Eligible for Technical Assessment"),
    SELECTED("Selected - Offer Extended"),
    REJECTED("Rejected - Does Not Match Criteria");

    private final String description;

    ApplicationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ApplicationStatus fromString(String statusStr) {
        for (ApplicationStatus status : ApplicationStatus.values()) {
            if (status.name().equalsIgnoreCase(statusStr.trim())) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown application status: " + statusStr);
    }
}
