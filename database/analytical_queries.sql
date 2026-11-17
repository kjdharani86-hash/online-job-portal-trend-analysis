-- ============================================================================
-- ONLINE JOB PORTAL AND EMPLOYMENT TREND ANALYSIS SYSTEM
-- Scenario 22 | Analytical & Reporting SQL Queries
-- Course: 25ITT31 Database Management Systems & 25ITC31 Foundations of Data Science
-- Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
-- ============================================================================

-- ----------------------------------------------------------------------------
-- QUERY 1: Active Job Vacancies with Company Details & Package Range
-- (Demonstrates INNER JOIN, String Formatting, Ordering)
-- ----------------------------------------------------------------------------
SELECT 
    v.vacancy_id,
    c.company_name,
    c.industry,
    v.job_title,
    v.min_cgpa,
    v.eligible_departments,
    v.location,
    'INR ' || (v.salary_min / 100000.0) || ' - ' || (v.salary_max / 100000.0) || ' LPA' AS salary_package,
    v.openings,
    v.status
FROM vacancies v
INNER JOIN companies c ON v.company_id = c.company_id
WHERE v.status = 'OPEN'
ORDER BY v.salary_max DESC;

-- ----------------------------------------------------------------------------
-- QUERY 2: Student Application Status with Vacancy & Company Info
-- (Demonstrates Multi-table INNER JOIN, Date Filtering)
-- ----------------------------------------------------------------------------
SELECT 
    a.application_id,
    s.student_id,
    s.name AS student_name,
    s.department,
    s.cgpa,
    c.company_name,
    v.job_title,
    a.apply_date,
    a.status,
    a.interview_round
FROM applications a
INNER JOIN students s ON a.student_id = s.student_id
INNER JOIN vacancies v ON a.vacancy_id = v.vacancy_id
INNER JOIN companies c ON v.company_id = c.company_id
ORDER BY a.apply_date DESC;

-- ----------------------------------------------------------------------------
-- QUERY 3: Company-Wise Recruitment & Selection Counts
-- (Demonstrates Aggregation, COUNT CASE WHEN, GROUP BY, HAVING)
-- ----------------------------------------------------------------------------
SELECT 
    c.company_name,
    c.industry,
    COUNT(a.application_id) AS total_applicants,
    SUM(CASE WHEN a.status = 'SELECTED' THEN 1 ELSE 0 END) AS selected_count,
    SUM(CASE WHEN a.status = 'SHORTLISTED' THEN 1 ELSE 0 END) AS shortlisted_count,
    SUM(CASE WHEN a.status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected_count,
    ROUND((SUM(CASE WHEN a.status = 'SELECTED' THEN 1.0 ELSE 0.0 END) / COUNT(a.application_id)) * 100, 2) AS selection_rate_pct
FROM companies c
JOIN vacancies v ON c.company_id = v.company_id
JOIN applications a ON v.vacancy_id = a.vacancy_id
GROUP BY c.company_id, c.company_name, c.industry
ORDER BY selected_count DESC, total_applicants DESC;

-- ----------------------------------------------------------------------------
-- QUERY 4: Department-Wise Placement Trends & Selection Statistics
-- (Demonstrates Grouping, Percentage Calculations, Conditional Aggregations)
-- ----------------------------------------------------------------------------
SELECT 
    s.department,
    COUNT(DISTINCT s.student_id) AS total_registered_students,
    COUNT(a.application_id) AS total_applications_submitted,
    COUNT(DISTINCT CASE WHEN a.status = 'SELECTED' THEN s.student_id END) AS unique_placed_students,
    ROUND(
        (COUNT(DISTINCT CASE WHEN a.status = 'SELECTED' THEN s.student_id END) * 100.0) / 
        COUNT(DISTINCT s.student_id), 2
    ) AS placement_percentage,
    ROUND(AVG(s.cgpa), 2) AS dept_avg_cgpa
FROM students s
LEFT JOIN applications a ON s.student_id = a.student_id
GROUP BY s.department
ORDER BY placement_percentage DESC;

-- ----------------------------------------------------------------------------
-- QUERY 5: Top Paying Job Vacancies Above Overall Average Salary
-- (Demonstrates Nested Subquery / Scalar Subquery)
-- ----------------------------------------------------------------------------
SELECT 
    c.company_name,
    v.job_title,
    v.location,
    v.salary_max,
    v.min_cgpa
FROM vacancies v
JOIN companies c ON v.company_id = c.company_id
WHERE v.salary_max > (SELECT AVG(salary_max) FROM vacancies)
ORDER BY v.salary_max DESC;

-- ----------------------------------------------------------------------------
-- QUERY 6: Students Eligible for a High-Paying Role (CGPA >= 8.0 & IT/CSE)
-- (Demonstrates Set Operations / Complex Boolean Logic)
-- ----------------------------------------------------------------------------
SELECT 
    student_id,
    name,
    department,
    cgpa,
    skills,
    email
FROM students
WHERE cgpa >= 8.0 
  AND department IN ('IT', 'CSE')
ORDER BY cgpa DESC;

-- ----------------------------------------------------------------------------
-- QUERY 7: Verification of Duplicate Application Prevention
-- (Demonstrates Integrity Check for Business Rule #4)
-- ----------------------------------------------------------------------------
SELECT 
    student_id, 
    vacancy_id, 
    COUNT(*) AS application_count
FROM applications
GROUP BY student_id, vacancy_id
HAVING COUNT(*) > 1;
-- Expected output: 0 rows (Validates uniqueness constraint)
