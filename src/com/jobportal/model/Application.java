package com.jobportal.model;

/**
 * Model entity representing an Application submitted by a student for a vacancy.
 * Course: 25ITT32 Java Programming
 * Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
 */
public class Application {
    private int applicationId;
    private String studentId;
    private String studentName;
    private int vacancyId;
    private String jobTitle;
    private String companyName;
    private String applyDate;
    private ApplicationStatus status;
    private int interviewRound;
    private String remarks;

    public Application() {}

    public Application(int applicationId, String studentId, String studentName, int vacancyId,
                       String jobTitle, String companyName, String applyDate,
                       ApplicationStatus status, int interviewRound, String remarks) {
        this.applicationId = applicationId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.vacancyId = vacancyId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.applyDate = applyDate;
        this.status = status;
        this.interviewRound = interviewRound;
        this.remarks = remarks;
    }

    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public int getVacancyId() { return vacancyId; }
    public void setVacancyId(int vacancyId) { this.vacancyId = vacancyId; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getApplyDate() { return applyDate; }
    public void setApplyDate(String applyDate) { this.applyDate = applyDate; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public int getInterviewRound() { return interviewRound; }
    public void setInterviewRound(int interviewRound) { this.interviewRound = interviewRound; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    @Override
    public String toString() {
        return String.format("| %-4d | %-14s | %-16s | %-22s | %-22s | %-12s | Rnd %-1d |",
                applicationId, studentId,
                (studentName != null && studentName.length() > 16 ? studentName.substring(0, 13) + "..." : studentName),
                (companyName != null && companyName.length() > 22 ? companyName.substring(0, 19) + "..." : companyName),
                (jobTitle != null && jobTitle.length() > 22 ? jobTitle.substring(0, 19) + "..." : jobTitle),
                (status != null ? status.name() : "N/A"), interviewRound);
    }
}
