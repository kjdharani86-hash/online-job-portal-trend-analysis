-- ============================================================================
-- ONLINE JOB PORTAL AND EMPLOYMENT TREND ANALYSIS SYSTEM
-- Scenario 22 | Department of Information Technology
-- Velalar College of Engineering and Technology, Erode - 12
-- Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
-- Database Schema: DDL Definitions (Compatible with SQLite & MySQL)
-- ============================================================================

-- 1. Table: students
-- Maintains student / job seeker profiles, academic qualifications, and competencies
CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    qualification VARCHAR(50) NOT NULL,
    cgpa REAL NOT NULL CHECK(cgpa >= 0.0 AND cgpa <= 10.0),
    skills TEXT NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Table: companies
-- Maintains company profiles, headquarters location, and industry domains
CREATE TABLE IF NOT EXISTS companies (
    company_id INTEGER PRIMARY KEY AUTOINCREMENT,
    company_name VARCHAR(100) UNIQUE NOT NULL,
    industry VARCHAR(50) NOT NULL,
    location VARCHAR(100) NOT NULL,
    contact_email VARCHAR(100) NOT NULL,
    website VARCHAR(100)
);

-- 3. Table: vacancies
-- Maintains job vacancy details, eligibility criteria, salary package, and location
CREATE TABLE IF NOT EXISTS vacancies (
    vacancy_id INTEGER PRIMARY KEY AUTOINCREMENT,
    company_id INTEGER NOT NULL,
    job_title VARCHAR(100) NOT NULL,
    min_cgpa REAL NOT NULL DEFAULT 6.0 CHECK(min_cgpa >= 0.0 AND min_cgpa <= 10.0),
    required_skills TEXT NOT NULL,
    eligible_departments VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    salary_min INTEGER NOT NULL CHECK(salary_min >= 0),
    salary_max INTEGER NOT NULL CHECK(salary_max >= salary_min),
    openings INTEGER NOT NULL DEFAULT 1 CHECK(openings > 0),
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN' CHECK(status IN ('OPEN', 'CLOSED')),
    posted_date DATE DEFAULT (DATE('now')),
    FOREIGN KEY (company_id) REFERENCES companies(company_id) ON DELETE CASCADE
);

-- 4. Table: applications
-- Records student job applications, enforces uniqueness, and tracks recruitment pipeline status
CREATE TABLE IF NOT EXISTS applications (
    application_id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id VARCHAR(20) NOT NULL,
    vacancy_id INTEGER NOT NULL,
    apply_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'APPLIED' CHECK(status IN ('APPLIED', 'SHORTLISTED', 'SELECTED', 'REJECTED')),
    interview_round INTEGER DEFAULT 1,
    remarks TEXT,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (vacancy_id) REFERENCES vacancies(vacancy_id) ON DELETE CASCADE,
    CONSTRAINT unique_student_vacancy UNIQUE (student_id, vacancy_id)
);

-- ============================================================================
-- INDEXES FOR QUERY OPTIMIZATION
-- ============================================================================
CREATE INDEX IF NOT EXISTS idx_students_dept ON students(department);
CREATE INDEX IF NOT EXISTS idx_vacancies_company ON vacancies(company_id);
CREATE INDEX IF NOT EXISTS idx_vacancies_status ON vacancies(status);
CREATE INDEX IF NOT EXISTS idx_applications_student ON applications(student_id);
CREATE INDEX IF NOT EXISTS idx_applications_vacancy ON applications(vacancy_id);
CREATE INDEX IF NOT EXISTS idx_applications_status ON applications(status);
