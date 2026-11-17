package com.jobportal.dao;

import com.jobportal.config.DatabaseConnection;
import com.jobportal.model.Vacancy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Vacancy entities.
 * Handles job postings and retrieval with multi-table JOINs to include Company Name.
 * Course: 25ITT32 Java Programming (Unit 4: Database Connection)
 * Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
 */
public class VacancyDAO {

    public int insertVacancy(Vacancy vacancy) throws SQLException {
        String sql = "INSERT INTO vacancies (company_id, job_title, min_cgpa, required_skills, " +
                     "eligible_departments, location, salary_min, salary_max, openings, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, vacancy.getCompanyId());
            pstmt.setString(2, vacancy.getJobTitle());
            pstmt.setDouble(3, vacancy.getMinCgpa());
            pstmt.setString(4, vacancy.getRequiredSkills());
            pstmt.setString(5, vacancy.getEligibleDepartments().toUpperCase());
            pstmt.setString(6, vacancy.getLocation());
            pstmt.setInt(7, vacancy.getSalaryMin());
            pstmt.setInt(8, vacancy.getSalaryMax());
            pstmt.setInt(9, vacancy.getOpenings());
            pstmt.setString(10, vacancy.getStatus() != null ? vacancy.getStatus() : "OPEN");
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public Vacancy getVacancyById(int vacancyId) throws SQLException {
        String sql = "SELECT v.*, c.company_name FROM vacancies v " +
                     "JOIN companies c ON v.company_id = c.company_id WHERE v.vacancy_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vacancyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToVacancy(rs);
                }
            }
        }
        return null;
    }

    public List<Vacancy> getAllVacancies() throws SQLException {
        List<Vacancy> list = new ArrayList<>();
        String sql = "SELECT v.*, c.company_name FROM vacancies v " +
                     "JOIN companies c ON v.company_id = c.company_id ORDER BY v.vacancy_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRowToVacancy(rs));
            }
        }
        return list;
    }

    public List<Vacancy> getOpenVacancies() throws SQLException {
        List<Vacancy> list = new ArrayList<>();
        String sql = "SELECT v.*, c.company_name FROM vacancies v " +
                     "JOIN companies c ON v.company_id = c.company_id " +
                     "WHERE v.status = 'OPEN' ORDER BY v.salary_max DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRowToVacancy(rs));
            }
        }
        return list;
    }

    public List<Vacancy> getVacanciesByCompany(int companyId) throws SQLException {
        List<Vacancy> list = new ArrayList<>();
        String sql = "SELECT v.*, c.company_name FROM vacancies v " +
                     "JOIN companies c ON v.company_id = c.company_id " +
                     "WHERE v.company_id = ? ORDER BY v.vacancy_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, companyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToVacancy(rs));
                }
            }
        }
        return list;
    }

    private Vacancy mapRowToVacancy(ResultSet rs) throws SQLException {
        return new Vacancy(
                rs.getInt("vacancy_id"),
                rs.getInt("company_id"),
                rs.getString("company_name"),
                rs.getString("job_title"),
                rs.getDouble("min_cgpa"),
                rs.getString("required_skills"),
                rs.getString("eligible_departments"),
                rs.getString("location"),
                rs.getInt("salary_min"),
                rs.getInt("salary_max"),
                rs.getInt("openings"),
                rs.getString("status"),
                rs.getString("posted_date")
        );
    }
}
