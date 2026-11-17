package com.jobportal.service;

import com.jobportal.exception.DuplicateApplicationException;
import com.jobportal.exception.IneligibleCandidateException;
import com.jobportal.exception.RecordNotFoundException;
import com.jobportal.model.Application;
import com.jobportal.model.ApplicationStatus;
import com.jobportal.model.Company;
import com.jobportal.model.Student;
import com.jobportal.model.Vacancy;

import java.sql.SQLException;
import java.util.List;

/**
 * Service Layer Interface specifying all business logic operations.
 * Demonstrates Interface definition, Abstraction, and Decoupling.
 * Course: 25ITT32 Java Programming (Unit 2: Interfaces)
 * Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
 */
public interface JobPortalService {

    // Student Operations
    boolean registerStudent(Student student) throws SQLException;
    Student getStudent(String studentId) throws SQLException, RecordNotFoundException;
    List<Student> getAllStudents() throws SQLException;

    // Company & Vacancy Operations
    int registerCompany(Company company) throws SQLException;
    List<Company> getAllCompanies() throws SQLException;
    int postJobVacancy(Vacancy vacancy) throws SQLException, RecordNotFoundException;
    List<Vacancy> getAllVacancies() throws SQLException;
    List<Vacancy> getOpenVacancies() throws SQLException;
    List<Vacancy> getEligibleVacanciesForStudent(String studentId) throws SQLException, RecordNotFoundException;

    // Application & Recruitment Lifecycle Operations
    int applyForJob(String studentId, int vacancyId, String remarks)
            throws SQLException, DuplicateApplicationException, IneligibleCandidateException, RecordNotFoundException;

    boolean updateApplicationStatus(int applicationId, ApplicationStatus status, int interviewRound, String remarks)
            throws SQLException, RecordNotFoundException;

    List<Application> getStudentApplications(String studentId) throws SQLException;
    List<Application> getVacancyApplications(int vacancyId) throws SQLException;
    List<Application> getAllApplications() throws SQLException;
}
