package com.jobportal.exception;

/**
 * Custom Exception thrown when a student does not meet the eligibility criteria
 * (e.g., CGPA below cutoff or department not in allowed list).
 * Implements Functional Requirement 3: "Allow eligible students to apply for available job vacancies."
 * Course: 25ITT32 Java Programming (Unit 3: Exception Handling)
 * Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
 */
public class IneligibleCandidateException extends Exception {
    private final String studentId;
    private final int vacancyId;
    private final String reason;

    public IneligibleCandidateException(String studentId, int vacancyId, String reason) {
        super(String.format("Ineligibility Error: Student [%s] cannot apply for Vacancy [%d]. Reason: %s",
                studentId, vacancyId, reason));
        this.studentId = studentId;
        this.vacancyId = vacancyId;
        this.reason = reason;
    }

    public String getStudentId() { return studentId; }
    public int getVacancyId() { return vacancyId; }
    public String getReason() { return reason; }
}
