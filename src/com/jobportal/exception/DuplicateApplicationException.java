package com.jobportal.exception;

/**
 * Custom Exception thrown when a student attempts to apply for the same vacancy more than once.
 * Implements Functional Requirement 4: "Prevent duplicate applications by the same student for the same vacancy."
 * Course: 25ITT32 Java Programming (Unit 3: Exception Handling)
 * Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
 */
public class DuplicateApplicationException extends Exception {
    private final String studentId;
    private final int vacancyId;

    public DuplicateApplicationException(String studentId, int vacancyId) {
        super(String.format("Duplicate Application Rejected: Student [%s] has already applied for Vacancy ID [%d].",
                studentId, vacancyId));
        this.studentId = studentId;
        this.vacancyId = vacancyId;
    }

    public String getStudentId() { return studentId; }
    public int getVacancyId() { return vacancyId; }
}
