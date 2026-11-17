package com.jobportal.dao;

import com.jobportal.config.DatabaseConnection;
import com.jobportal.model.Application;
import com.jobportal.model.ApplicationStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Application tracking.
 * Implements Duplicate Prevention check and Recruitment Pipeline updates.
 * Course: 25ITT32 Java Programming (Unit 4: Database Connection)
 * Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
 */
public class ApplicationDAO {

    /**
     * Checks if a student has already submitted an application for the specified vacancy.
     * Enforces Functional Requirement 4: Prevent duplicate applications.
     */
    public boolean hasAlreadyApplied(String studentId, int vacancyId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM applications WHERE student_id = ? AND vacancy_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setInt(2, vacancyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public int submitApplication(String studentId, int vacancyId, String remarks) throws SQLException {
        String sql = "INSERT INTO applications (student_id, vacancy_id, status, interview_round, remarks) " +
                     "VALUES (?, ?, 'APPLIED', 1, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, studentId);
            pstmt.setInt(2, vacancyId);
            pstmt.setString(3, remarks != null ? remarks : "Application submitted");
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public boolean updateApplicationStatus(int applicationId, ApplicationStatus status, int round, String remarks) throws SQLException {
        String sql = "UPDATE applications SET status = ?, interview_round = ?, remarks = ? WHERE application_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status.name());
            pstmt.setInt(2, round);
            pstmt.setString(3, remarks);
            pstmt.setInt(4, applicationId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public Application getApplicationById(int applicationId) throws SQLException {
        String sql = "SELECT a.*, s.name AS student_name, v.job_title, c.company_name " +
                     "FROM applications a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN vacancies v ON a.vacancy_id = v.vacancy_id " +
                     "JOIN companies c ON v.company_id = c.company_id " +
                     "WHERE a.application_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, applicationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToApplication(rs);
                }
            }
        }
        return null;
    }

    public List<Application> getApplicationsByStudent(String studentId) throws SQLException {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT a.*, s.name AS student_name, v.job_title, c.company_name " +
                     "FROM applications a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN vacancies v ON a.vacancy_id = v.vacancy_id " +
                     "JOIN companies c ON v.company_id = c.company_id " +
                     "WHERE a.student_id = ? ORDER BY a.apply_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToApplication(rs));
                }
            }
        }
        return list;
    }

    public List<Application> getApplicationsByVacancy(int vacancyId) throws SQLException {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT a.*, s.name AS student_name, v.job_title, c.company_name " +
                     "FROM applications a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN vacancies v ON a.vacancy_id = v.vacancy_id " +
                     "JOIN companies c ON v.company_id = c.company_id " +
                     "WHERE a.vacancy_id = ? ORDER BY a.application_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vacancyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToApplication(rs));
                }
            }
        }
        return list;
    }

    public List<Application> getAllApplications() throws SQLException {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT a.*, s.name AS student_name, v.job_title, c.company_name " +
                     "FROM applications a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN vacancies v ON a.vacancy_id = v.vacancy_id " +
                     "JOIN companies c ON v.company_id = c.company_id " +
                     "ORDER BY a.application_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRowToApplication(rs));
            }
        }
        return list;
    }

    private Application mapRowToApplication(ResultSet rs) throws SQLException {
        return new Application(
                rs.getInt("application_id"),
                rs.getString("student_id"),
                rs.getString("student_name"),
                rs.getInt("vacancy_id"),
                rs.getString("job_title"),
                rs.getString("company_name"),
                rs.getString("apply_date"),
                ApplicationStatus.fromString(rs.getString("status")),
                rs.getInt("interview_round"),
                rs.getString("remarks")
        );
    }
}
