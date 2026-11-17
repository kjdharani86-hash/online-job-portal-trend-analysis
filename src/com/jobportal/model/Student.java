package com.jobportal.model;

/**
 * Model entity representing a Student / Job Seeker.
 * Demonstrates Encapsulation, Constructors, Getters/Setters, and Data Formatting.
 * Course: 25ITT32 Java Programming (Unit 1 & Unit 2)
 * Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
 */
public class Student {
    private String studentId;
    private String name;
    private String department;
    private String qualification;
    private double cgpa;
    private String skills;
    private String email;
    private String phone;

    public Student() {}

    public Student(String studentId, String name, String department, String qualification,
                   double cgpa, String skills, String email, String phone) {
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.qualification = qualification;
        this.cgpa = cgpa;
        this.skills = skills;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return String.format("| %-14s | %-16s | %-6s | %-10s | %4.2f | %-28s | %-25s |",
                studentId, name, department, qualification, cgpa,
                (skills.length() > 28 ? skills.substring(0, 25) + "..." : skills), email);
    }
}
