# SYSTEM DESIGN & UML DIAGRAMS SPECIFICATION
## Online Job Portal and Employment Trend Analysis System
### Scenario 22 | Department of Information Technology
**Velalar College of Engineering and Technology, Erode - 12**  
**Student:** Dharaneesh.K | **Reg No:** 732925ITR022 | **Class:** 25IT3A  
**Course:** 25ITT33 Software Engineering (Unit 2 & Unit 3)

---

## 1. OVERALL SYSTEM ARCHITECTURE

The system follows an enterprise **N-Tier Architectural Pattern** ensuring separation of concerns, high cohesion, low coupling, and maintainability.

```mermaid
graph TD
    subgraph Presentation_Layer ["Presentation & Interaction Layer"]
        CLI["Interactive Console Menu (Main.java)"]
        DEMO["Automated Demo Test Runner"]
    end

    subgraph Service_Layer ["Business Logic & Service Layer"]
        SVC["JobPortalService (Interface)"]
        SVC_IMPL["JobPortalServiceImpl"]
        RULES["Eligibility Validator & Business Rules"]
        NOTIFIER["Async Notification Dispatcher (Multithreading)"]
        REPORT["ReportGenerator (Stream API & Collections)"]
    end

    subgraph DAO_Layer ["Data Access Object (DAO) Layer"]
        S_DAO["StudentDAO"]
        C_DAO["CompanyDAO"]
        V_DAO["VacancyDAO"]
        A_DAO["ApplicationDAO"]
    end

    subgraph Persistence_Layer ["Persistence & Storage Layer"]
        DBCONN["DatabaseConnection (Singleton JDBC)"]
        SQLITE[("SQLite DBMS (job_portal.db)")]
        RAW_SQL["schema.sql / seed_data.sql"]
    end

    subgraph Analytics_Layer ["Python Data Science & Visualization Layer"]
        RETRIEVE["Database Retrieval (db_retrieval.py)"]
        PREPROC["Data Preprocessing & IQR (data_preprocessing.py)"]
        VIZ["Visualizations Generator (visualizations.py)"]
        NOTEBOOK["Jupyter Notebook (employment_trend_analysis.ipynb)"]
        CHARTS["8 High-Resolution Charts (.png) in output/visualizations"]
    end

    CLI --> SVC
    DEMO --> SVC
    SVC --> SVC_IMPL
    SVC_IMPL --> RULES
    SVC_IMPL --> NOTIFIER
    SVC_IMPL --> S_DAO
    SVC_IMPL --> C_DAO
    SVC_IMPL --> V_DAO
    SVC_IMPL --> A_DAO
    SVC_IMPL --> REPORT

    S_DAO --> DBCONN
    C_DAO --> DBCONN
    V_DAO --> DBCONN
    A_DAO --> DBCONN
    DBCONN --> SQLITE

    SQLITE --> RETRIEVE
    RETRIEVE --> PREPROC
    PREPROC --> VIZ
    PREPROC --> NOTEBOOK
    VIZ --> CHARTS
```

---

## 2. UML USE CASE DIAGRAM

The Use Case Diagram illustrates the functional interactions between the primary actors (**Student**, **Corporate Recruiter**, **Placement Officer / Admin**, and **System Background Notifier**) and the system functions.

```mermaid
flowchart LR
    Student(["Student / Job Seeker"])
    Recruiter(["Corporate Recruiter"])
    Admin(["Placement Officer / Admin"])
    Notifier(["System Async Notifier"])

    subgraph JobPortalSystem ["Online Job Portal System Boundaries"]
        UC1["Register / Update Student Profile"]
        UC2["Browse All Open Vacancies"]
        UC3["Filter Matching Eligible Vacancies"]
        UC4["Apply for Job Vacancy"]
        UC5["Validate Minimum CGPA Cutoff"]
        UC6["Validate Eligible Departments"]
        UC7["Prevent Duplicate Applications"]
        UC8["View Personal Application History"]
        UC9["Register Company Profile"]
        UC10["Publish New Job Vacancy"]
        UC11["Review Applicants for Vacancy"]
        UC12["Update Application Status & Round"]
        UC13["Dispatch Email / SMS Notification"]
        UC14["View Registered Students Directory"]
        UC15["Generate Institutional Placement Reports"]
        UC16["Run Python Trend Analytics & Visualizations"]
    end

    Student --> UC1
    Student --> UC2
    Student --> UC3
    Student --> UC4
    Student --> UC8

    UC4 -.->|"<<includes>>"| UC5
    UC4 -.->|"<<includes>>"| UC6
    UC4 -.->|"<<includes>>"| UC7
    UC4 -.->|"<<triggers>>"| UC13

    Notifier --- UC13

    Recruiter --> UC9
    Recruiter --> UC10
    Recruiter --> UC11
    Recruiter --> UC12

    Admin --> UC14
    Admin --> UC15
    Admin --> UC16
```

---

## 3. UML CLASS DIAGRAM

The Class Diagram documents the static object-oriented structure, class attributes, method signatures, visibility modifiers, and design relationships.

```mermaid
classDiagram
    direction TB

    class Student {
        -String studentId
        -String name
        -String department
        -String qualification
        -double cgpa
        -String skills
        -String email
        -String phone
        +Student()
        +Student(...)
        +getStudentId() String
        +getCgpa() double
        +getDepartment() String
        +toString() String
    }

    class Company {
        -int companyId
        -String companyName
        -String industry
        -String location
        -String contactEmail
        -String website
        +Company()
        +Company(...)
        +getCompanyId() int
        +getCompanyName() String
        +toString() String
    }

    class Vacancy {
        -int vacancyId
        -int companyId
        -String companyName
        -String jobTitle
        -double minCgpa
        -String requiredSkills
        -String eligibleDepartments
        -String location
        -int salaryMin
        -int salaryMax
        -int openings
        -String status
        -String postedDate
        +Vacancy()
        +Vacancy(...)
        +getMinCgpa() double
        +getEligibleDepartments() String
        +getSalaryFormatted() String
        +toString() String
    }

    class Application {
        -int applicationId
        -String studentId
        -String studentName
        -int vacancyId
        -String jobTitle
        -String companyName
        -String applyDate
        -ApplicationStatus status
        -int interviewRound
        -String remarks
        +Application()
        +Application(...)
        +getStatus() ApplicationStatus
        +toString() String
    }

    class ApplicationStatus {
        <<enumeration>>
        APPLIED
        SHORTLISTED
        SELECTED
        REJECTED
        -String description
        +getDescription() String
        +fromString(String) ApplicationStatus
    }

    class JobPortalService {
        <<interface>>
        +registerStudent(Student) boolean
        +getStudent(String) Student
        +getAllStudents() List~Student~
        +registerCompany(Company) int
        +postJobVacancy(Vacancy) int
        +getOpenVacancies() List~Vacancy~
        +getEligibleVacanciesForStudent(String) List~Vacancy~
        +applyForJob(String, int, String) int
        +updateApplicationStatus(int, ApplicationStatus, int, String) boolean
        +getAllApplications() List~Application~
    }

    class JobPortalServiceImpl {
        -StudentDAO studentDAO
        -CompanyDAO companyDAO
        -VacancyDAO vacancyDAO
        -ApplicationDAO applicationDAO
        +applyForJob(String, int, String) int
        -isEligible(Student, Vacancy) boolean
        -dispatchNotificationAsync(Student, Vacancy, int) void
    }

    class DuplicateApplicationException {
        -String studentId
        -int vacancyId
        +DuplicateApplicationException(String, int)
    }

    class IneligibleCandidateException {
        -String studentId
        -int vacancyId
        -String reason
        +IneligibleCandidateException(String, int, String)
    }

    class ReportGenerator {
        <<utility>>
        +printVacancyReport(List~Vacancy~) void
        +printApplicationReport(List~Application~) void
        +printSelectionCountReport(List~Application~) void
        +printCompanyRecruitmentReport(List~Application~) void
        +printDepartmentTrendReport(List~Student~, List~Application~) void
    }

    class DatabaseConnection {
        <<singleton>>
        -Connection connection
        +getConnection() Connection
        +closeConnection() void
    }

    JobPortalService <|.. JobPortalServiceImpl : implements
    JobPortalServiceImpl --> Student : manages
    JobPortalServiceImpl --> Company : manages
    JobPortalServiceImpl --> Vacancy : manages
    JobPortalServiceImpl --> Application : manages
    Application o-- ApplicationStatus : uses
    JobPortalServiceImpl ..> DuplicateApplicationException : throws
    JobPortalServiceImpl ..> IneligibleCandidateException : throws
    JobPortalServiceImpl --> ReportGenerator : calls
    JobPortalServiceImpl --> DatabaseConnection : connects via
```

---

## 4. UML SEQUENCE DIAGRAM: JOB APPLICATION WORKFLOW

This sequence diagram details the runtime execution path, validation guards, and exception handling during a job application attempt.

```mermaid
sequenceDiagram
    autonumber
    actor Student as Student (Candidate)
    participant UI as Main (CLI Interface)
    participant SVC as JobPortalServiceImpl
    participant S_DAO as StudentDAO
    participant V_DAO as VacancyDAO
    participant A_DAO as ApplicationDAO
    participant Notifier as Async Notification Thread
    participant DB as SQLite Database

    Student->>UI: Enter Student ID & Vacancy ID
    UI->>SVC: applyForJob(studentId, vacancyId, remarks)
    
    SVC->>S_DAO: getStudentById(studentId)
    S_DAO->>DB: SELECT * FROM students WHERE student_id = ?
    DB-->>S_DAO: Student Record
    S_DAO-->>SVC: Student Object

    SVC->>V_DAO: getVacancyById(vacancyId)
    V_DAO->>DB: SELECT * FROM vacancies WHERE vacancy_id = ?
    DB-->>V_DAO: Vacancy Record
    V_DAO-->>SVC: Vacancy Object

    SVC->>A_DAO: hasAlreadyApplied(studentId, vacancyId)
    A_DAO->>DB: SELECT COUNT(*) FROM applications WHERE student_id=? AND vacancy_id=?
    DB-->>A_DAO: Count
    A_DAO-->>SVC: boolean (exists)

    alt Already Applied (Duplicate Application)
        SVC-->>UI: throw DuplicateApplicationException
        UI-->>Student: Display "Validation Error: Duplicate Application Rejected"
    else Not Duplicate -> Validate Eligibility
        opt Student CGPA < Vacancy min_cgpa OR Department not allowed
            SVC-->>UI: throw IneligibleCandidateException
            UI-->>Student: Display "Validation Error: Criteria Not Met"
        end
        
        SVC->>A_DAO: submitApplication(studentId, vacancyId, remarks)
        A_DAO->>DB: INSERT INTO applications (...)
        DB-->>A_DAO: Generated Application ID (#appId)
        A_DAO-->>SVC: appId

        SVC->>Notifier: spawn new Thread(dispatchNotificationAsync)
        activate Notifier
        Notifier-->>Student: [ASYNC NOTIFIER] Email dispatched for Application #appId
        deactivate Notifier

        SVC-->>UI: return applicationId
        UI-->>Student: Display "[SUCCESS] Application #appId registered successfully"
    end
```
