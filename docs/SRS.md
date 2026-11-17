# SOFTWARE REQUIREMENTS SPECIFICATION (SRS)
## Online Job Portal and Employment Trend Analysis System
### Scenario 22 | Activity: Project-Based Assessment

**Institution:** Velalar College of Engineering and Technology (Autonomous), Erode - 638012  
**Department:** Department of Information Technology  
**Academic Year:** 2026 - 2027 (ODD Semester)  
**Class & Section:** 25IT3A  
**Student Name:** Dharaneesh.K  
**Register Number:** 732925ITR022  
**Document Standard:** IEEE Std 830-1998 Format  

---

## 1. INTRODUCTION

### 1.1 Purpose
The purpose of this Software Requirements Specification (SRS) document is to provide a complete, formal, and structured specification of the **Online Job Portal and Employment Trend Analysis System** (Scenario 22). This document serves as the foundational agreement between stakeholders (Placement Cell, Recruiters, Students, and System Developers) and outlines both the functional and non-functional requirements, data models, and constraints governing the system.

### 1.2 Scope
Manual campus recruitment and placement cell management are prone to administrative overhead, delayed communication, difficulty in tracking job vacancies across hundreds of corporate recruiters, missed eligibility checks, duplicate applications, and a complete lack of empirical insights into department-level hiring trends.

The developed system provides an integrated software solution combining:
1. A **Java Application** implementing Object-Oriented Software Engineering principles, database transactions, input validation, custom exceptions, and multithreading.
2. A **Relational Database Management System (DBMS)** maintaining student records, employer profiles, vacancies, and application pipelines normalized up to Boyce-Codd Normal Form (BCNF).
3. A **Python Data Science & Visualization Pipeline** leveraging NumPy and Matplotlib to extract placement datasets, perform Exploratory Data Analysis (EDA), and render actionable visual trends for placement officers and academic departments.

### 1.3 Definitions, Acronyms, and Abbreviations
- **SRS**: Software Requirements Specification
- **DBMS**: Database Management System
- **JDBC**: Java Database Connectivity
- **DAO**: Data Access Object
- **EDA**: Exploratory Data Analysis
- **CGPA**: Cumulative Grade Point Average (Scale: 0.00 – 10.00)
- **BCNF**: Boyce-Codd Normal Form
- **LPA**: Lakhs Per Annum (INR Currency Unit for Annual Compensation)

### 1.4 References
- IEEE Std 830-1998: *IEEE Recommended Practice for Software Requirements Specifications*.
- Syllabus Guidelines: *25ITT33 Software Engineering, 25ITT32 Java Programming, 25ITT31 DBMS, 25ITC31 Foundations of Data Science*.
- Velalar College of Engineering and Technology Assessment Guidelines (ODD 2026-27).

---

## 2. OVERALL DESCRIPTION

### 2.1 Product Perspective
The system operates as a unified three-tier enterprise information system:
- **Presentation & Application Layer (Java)**: Interactive CLI Menu providing role-based user navigation for Students, Corporate Recruiters, and Placement Officers.
- **Data Persistence Layer (SQLite / SQL)**: ACID-compliant relational database enforcing primary keys, foreign key cascades, check constraints, and unique compound constraints.
- **Analytics & Reporting Layer (Python)**: Standalone automated data science engine extracting database records, conducting data wrangling, IQR outlier analysis, and generating publication-ready charts.

### 2.2 Product Functions
1. **Student Profile Management**: Registering and managing student profiles including Register Number, Name, Department, Qualification, CGPA, Technical Skills, Email, and Phone.
2. **Company & Vacancy Publishing**: Managing employer profiles and posting structured vacancies containing minimum CGPA thresholds, eligible departments, required skill tags, location, and salary packages.
3. **Smart Eligibility Filtering**: Automatically matching student profiles with open vacancies based on CGPA and department eligibility.
4. **Application Submission & Duplicate Prevention**: Enabling students to apply for verified vacancies while programmatically preventing duplicate applications for the same opening.
5. **Recruitment Pipeline Tracking**: Tracking progression through recruitment stages (`APPLIED`, `SHORTLISTED`, `SELECTED`, `REJECTED`) with interview round tracking and evaluator remarks.
6. **Administrative Reporting**: Real-time generation of tabular placement reports, vacancy summaries, selection counts, and company hiring audits.
7. **Employment Trend Analytics**: Processing placement data to reveal hiring patterns, salary distributions, skill demand rankings, and department-wise success ratios.

### 2.3 User Classes and Characteristics
- **Students / Job Seekers**: Undergraduate students seeking internships and full-time employment. Expected to register profiles, view matching vacancies, apply, and monitor interview status.
- **Recruiters / Employers**: Corporate talent acquisition teams who post job vacancies, define cutoffs, review applicants, and update candidate evaluation statuses.
- **Placement Cell Officers / Administrators**: College placement directors overseeing campus drives, monitoring institutional statistics, and generating compliance reports.

### 2.4 Operating Environment
- **Operating System**: Microsoft Windows 10/11, Linux, or macOS.
- **Java Runtime**: OpenJDK / Oracle JDK 17 LTS or higher.
- **Database Engine**: SQLite 3.x (with full SQL scripts compatible with MySQL 8.x).
- **Python Environment**: Python 3.9+ with NumPy and Matplotlib libraries.

### 2.5 Design and Implementation Constraints
- Compliance with Java naming conventions, OOP encapsulation, and layered architecture (Model-DAO-Service-Controller).
- Database tables must be normalized up to 3NF/BCNF with zero data redundancy.
- Memory-efficient file handling and non-blocking asynchronous email/SMS notifications.
- All visualizations must be output as high-resolution PNG images (300 DPI).

---

## 3. SPECIFIC SYSTEM REQUIREMENTS

### 3.1 Functional Requirements

#### FR-01: Student Profile Management
- **Description**: The system shall capture, validate, and store comprehensive student profiles.
- **Inputs**: Student ID (Reg No), Full Name, Department (`IT`, `CSE`, `ECE`, `EEE`, `MECH`), Qualification (`B.Tech`, `B.E`), CGPA (0.00 – 10.00), Skills, Email Address, 10-digit Phone.
- **Processing**: Validate email syntax using regular expressions, verify phone digits, enforce CGPA range, and persist in the database.
- **Outputs**: Unique registered student record with confirmation message.

#### FR-02: Company and Job Vacancy Management
- **Description**: The system shall maintain employer profiles and active vacancy postings.
- **Inputs**: Company Name, Industry, Location, HR Email, Website, Job Title, Minimum CGPA cutoff, Required Skills, Eligible Departments, Salary Range (Min - Max), Openings Count.
- **Processing**: Establish foreign key relationship between Vacancies and Companies. Verify salary integrity (`salary_max >= salary_min`).
- **Outputs**: Unique vacancy identifier and listing in open vacancy repository.

#### FR-03: Eligibility-Based Application Processing
- **Description**: The system shall allow only eligible candidates to apply for job vacancies.
- **Processing**: Compare candidate's CGPA against `min_cgpa`. Verify candidate's department against allowed departments (`eligible_departments`). If criteria are unmet, throw `IneligibleCandidateException`.
- **Outputs**: Successful application registration or descriptive rejection notification.

#### FR-04: Prevention of Duplicate Applications
- **Description**: The system shall strictly prevent multiple applications from the same student for the same vacancy.
- **Processing**: Query database for compound key `(student_id, vacancy_id)`. If an entry exists, abort submission and throw `DuplicateApplicationException`.
- **Outputs**: Error message notifying candidate of existing application.

#### FR-05: Recruitment Pipeline Status Tracking
- **Description**: The system shall enable tracking candidate progression through recruitment rounds.
- **Processing**: Update application state among predefined stages: `APPLIED`, `SHORTLISTED`, `SELECTED`, `REJECTED`, accompanied by interview round number and evaluator notes.
- **Outputs**: Real-time status report accessible by both candidate and placement coordinator.

#### FR-06: Consolidated Reporting Engine
- **Description**: The system shall generate comprehensive management reports using Java Collections and Stream API.
- **Outputs**:
  - Available Vacancies Report (with salary formatting and openings count).
  - Student Applications Pipeline Report.
  - Status Breakdown and Selection Counts Report (with percentage bars).
  - Company-wise Recruitment and Hiring Ratio Audit.
  - Department-wise Placement Trends Report.

#### FR-07: Python Data Retrieval, Preprocessing & Visualization
- **Description**: The system shall extract database tables into Python, perform data cleaning and feature engineering, and render visual analytics charts.
- **Outputs**: 8 high-resolution charts representing hiring funnels, salary distributions, department placement ratios, and skills demand rankings.

---

### 3.2 Non-Functional Requirements

#### NFR-01: Performance Requirements
- Console queries and report generation shall execute in less than 200 milliseconds.
- Asynchronous notification simulation shall run in background daemon threads without blocking the user interface.

#### NFR-02: Data Integrity and Security
- Enforce relational foreign keys with `ON DELETE CASCADE`.
- Enforce domain integrity constraints (e.g., `CHECK (cgpa >= 0.0 AND cgpa <= 10.0)`).
- Use `PreparedStatement` across all database operations to eliminate SQL Injection vulnerabilities.

#### NFR-03: Usability and Ergonomics
- Clear, numbered console menus with input validation and defensive error recovery.
- Professional tabular report formatting with fixed-width column alignments.

#### NFR-04: Maintainability and Extensibility
- Decoupled architecture separating business logic (`JobPortalService`) from data access (`DAO`) and data presentation (`Main`).
- Self-contained SQLite database allowing zero-configuration deployment across evaluation environments.

---

## 4. VERIFICATION & VALIDATION SUMMARY
All functional requirements FR-01 through FR-07 have been implemented and verified via automated test suites, sample data benchmarks, and end-to-end integration walkthroughs.
