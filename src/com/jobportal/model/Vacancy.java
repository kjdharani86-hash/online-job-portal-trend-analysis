package com.jobportal.model;

/**
 * Model entity representing a Job Vacancy published by a company.
 * Course: 25ITT32 Java Programming
 * Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
 */
public class Vacancy {
    private int vacancyId;
    private int companyId;
    private String companyName;
    private String jobTitle;
    private double minCgpa;
    private String requiredSkills;
    private String eligibleDepartments;
    private String location;
    private int salaryMin;
    private int salaryMax;
    private int openings;
    private String status;
    private String postedDate;

    public Vacancy() {}

    public Vacancy(int vacancyId, int companyId, String companyName, String jobTitle, double minCgpa,
                   String requiredSkills, String eligibleDepartments, String location,
                   int salaryMin, int salaryMax, int openings, String status, String postedDate) {
        this.vacancyId = vacancyId;
        this.companyId = companyId;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.minCgpa = minCgpa;
        this.requiredSkills = requiredSkills;
        this.eligibleDepartments = eligibleDepartments;
        this.location = location;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.openings = openings;
        this.status = status;
        this.postedDate = postedDate;
    }

    // Getters and Setters
    public int getVacancyId() { return vacancyId; }
    public void setVacancyId(int vacancyId) { this.vacancyId = vacancyId; }

    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public double getMinCgpa() { return minCgpa; }
    public void setMinCgpa(double minCgpa) { this.minCgpa = minCgpa; }

    public String getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(String requiredSkills) { this.requiredSkills = requiredSkills; }

    public String getEligibleDepartments() { return eligibleDepartments; }
    public void setEligibleDepartments(String eligibleDepartments) { this.eligibleDepartments = eligibleDepartments; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getSalaryMin() { return salaryMin; }
    public void setSalaryMin(int salaryMin) { this.salaryMin = salaryMin; }

    public int getSalaryMax() { return salaryMax; }
    public void setSalaryMax(int salaryMax) { this.salaryMax = salaryMax; }

    public int getOpenings() { return openings; }
    public void setOpenings(int openings) { this.openings = openings; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPostedDate() { return postedDate; }
    public void setPostedDate(String postedDate) { this.postedDate = postedDate; }

    public String getSalaryFormatted() {
        return String.format("INR %.1f - %.1f LPA", salaryMin / 100000.0, salaryMax / 100000.0);
    }

    @Override
    public String toString() {
        return String.format("| %-4d | %-24s | %-26s | %4.1f | %-12s | %-10s | %-16s | %-4d | %-6s |",
                vacancyId,
                (companyName != null && companyName.length() > 24 ? companyName.substring(0, 21) + "..." : companyName),
                (jobTitle.length() > 26 ? jobTitle.substring(0, 23) + "..." : jobTitle),
                minCgpa, eligibleDepartments, location, getSalaryFormatted(), openings, status);
    }
}
