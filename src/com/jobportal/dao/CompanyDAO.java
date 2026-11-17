package com.jobportal.dao;

import com.jobportal.config.DatabaseConnection;
import com.jobportal.model.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Company entities.
 * Course: 25ITT32 Java Programming (Unit 4: Database Connection)
 * Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
 */
public class CompanyDAO {

    public int insertCompany(Company company) throws SQLException {
        String sql = "INSERT INTO companies (company_name, industry, location, contact_email, website) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, company.getCompanyName());
            pstmt.setString(2, company.getIndustry());
            pstmt.setString(3, company.getLocation());
            pstmt.setString(4, company.getContactEmail());
            pstmt.setString(5, company.getWebsite());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1;
    }

    public Company getCompanyById(int companyId) throws SQLException {
        String sql = "SELECT * FROM companies WHERE company_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, companyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToCompany(rs);
                }
            }
        }
        return null;
    }

    public List<Company> getAllCompanies() throws SQLException {
        List<Company> list = new ArrayList<>();
        String sql = "SELECT * FROM companies ORDER BY company_name";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRowToCompany(rs));
            }
        }
        return list;
    }

    private Company mapRowToCompany(ResultSet rs) throws SQLException {
        return new Company(
                rs.getInt("company_id"),
                rs.getString("company_name"),
                rs.getString("industry"),
                rs.getString("location"),
                rs.getString("contact_email"),
                rs.getString("website")
        );
    }
}
