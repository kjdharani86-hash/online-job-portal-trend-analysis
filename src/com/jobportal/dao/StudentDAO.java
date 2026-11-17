package com.jobportal.dao;

import com.jobportal.config.DatabaseConnection;
import com.jobportal.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Student entities.
 * Handles CRUD operations using JDBC PreparedStatements.
 * Course: 25ITT32 Java Programming (Unit 4: Database Connection)
 * Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
 */
public class StudentDAO {

    public boolean insertStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (student_id, name, department, qualification, cgpa, skills, email, phone) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getDepartment().toUpperCase());
            pstmt.setString(4, student.getQualification());
            pstmt.setDouble(5, student.getCgpa());
            pstmt.setString(6, student.getSkills());
            pstmt.setString(7, student.getEmail());
            pstmt.setString(8, student.getPhone());
            return pstmt.executeUpdate() > 0;
        }
    }

    public Student getStudentById(String studentId) throws SQLException {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToStudent(rs);
                }
            }
        }
        return null;
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY department, student_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRowToStudent(rs));
            }
        }
        return list;
    }

    public boolean updateStudent(Student student) throws SQLException {
        String sql = "UPDATE students SET name = ?, department = ?, qualification = ?, cgpa = ?, " +
                     "skills = ?, email = ?, phone = ? WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getDepartment().toUpperCase());
            pstmt.setString(3, student.getQualification());
            pstmt.setDouble(4, student.getCgpa());
            pstmt.setString(5, student.getSkills());
            pstmt.setString(6, student.getEmail());
            pstmt.setString(7, student.getPhone());
            pstmt.setString(8, student.getStudentId());
            return pstmt.executeUpdate() > 0;
        }
    }

    private Student mapRowToStudent(ResultSet rs) throws SQLException {
        return new Student(
                rs.getString("student_id"),
                rs.getString("name"),
                rs.getString("department"),
                rs.getString("qualification"),
                rs.getDouble("cgpa"),
                rs.getString("skills"),
                rs.getString("email"),
                rs.getString("phone")
        );
    }
}
