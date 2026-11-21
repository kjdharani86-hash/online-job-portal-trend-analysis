# SYSTEM DESIGN & UML DIAGRAMS SPECIFICATION
## Online Job Portal and Employment Trend Analysis System
### Scenario 22 | Department of Information Technology
**Velalar College of Engineering and Technology (Autonomous), Erode - 638012**  
**Student:** Dharaneesh.K | **Reg No:** 732925ITR022 | **Class:** 25IT3A  
**Course:** 25ITT33 Software Engineering (Unit 2: Analysis & Unit 3: System Design)

---

## TABLE OF SYSTEM DIAGRAMS

1. [Overall N-Tier System Architecture Diagram](#1-overall-n-tier-system-architecture)
2. [Data Flow Diagrams (DFD Level 0 & Level 1)](#2-data-flow-diagrams-dfd)
   - 2.1 [DFD Level 0: Context Diagram](#21-dfd-level-0-context-diagram)
   - 2.2 [DFD Level 1: Functional Decomposition Diagram](#22-dfd-level-1-functional-decomposition)
3. [UML Use Case Diagram](#3-uml-use-case-diagram)
4. [UML Class Diagram (Static Structure)](#4-uml-class-diagram)
5. [UML Sequence Diagram (Job Application Execution)](#5-uml-sequence-diagram)
6. [UML Activity Diagram (Application & Eligibility Workflow)](#6-uml-activity-diagram)
7. [UML State Machine / Statechart Diagram (Application Lifecycle)](#7-uml-state-machine-diagram)
8. [UML Component Diagram](#8-uml-component-diagram)
9. [UML Deployment Diagram](#9-uml-deployment-diagram)

---

## 1. OVERALL N-TIER SYSTEM ARCHITECTURE

The system enforces strict separation of concerns through an enterprise **N-Tier Layered Architecture Pattern** ensuring loose coupling, modularity, and high maintainability.

```mermaid
graph TD
    subgraph Presentation_Layer ["1. Presentation & Interaction Layer"]
        CLI["Interactive Console Menu (Main.java)"]
        WEB["Flask Web Portal (app.py & Bootstrap 5)"]
        DEMO["Automated Demo Test Runner"]
    end

    subgraph Service_Layer ["2. Business Logic & Service Layer"]
        SVC["JobPortalService (Interface)"]
        SVC_IMPL["JobPortalServiceImpl"]
        RULES["Smart Eligibility Engine"]
        NOTIFIER["Async Notification Dispatcher (Multithreading)"]
        REPORT["ReportGenerator (Stream API & Collections)"]
    end

    subgraph DAO_Layer ["3. Data Access Object (DAO) Layer"]
        S_DAO["StudentDAO"]
        C_DAO["CompanyDAO"]
        V_DAO["VacancyDAO"]
        A_DAO["ApplicationDAO"]
    end

    subgraph Persistence_Layer ["4. Persistence & Storage Layer"]
        DBCONN["Singleton DatabaseConnection (JDBC / SQLite3)"]
        SQLITE[("job_portal.db (Relational BCNF Tables)")]
        RAW_SQL["schema.sql / seed_data.sql"]
    end

    subgraph Analytics_Layer ["5. Python Data Science & Visualization Layer"]
        RETRIEVE["Database Retrieval (db_retrieval.py)"]
        PREPROC["Data Preprocessing & IQR (data_preprocessing.py)"]
        VIZ["Visualizations Generator (visualizations.py)"]
        NOTEBOOK["Jupyter Notebook (employment_trend_analysis.ipynb)"]
        CHARTS["8 High-Resolution Charts (.png) in output/visualizations"]
    end

    CLI --> SVC
    WEB --> SVC
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

## 2. DATA FLOW DIAGRAMS (DFD)

### 2.1 DFD Level 0: Context Diagram
The Context Diagram establishes the boundary of the Online Job Portal system, depicting external entities and major information flows into and out of the central system process `0.0`.

```mermaid
flowchart LR
    Student["External Entity:<br/><b>Student / Candidate</b>"]
    Recruiter["External Entity:<br/><b>Corporate Recruiter</b>"]
    Admin["External Entity:<br/><b>Placement Officer</b>"]
    System(("<b>0.0</b><br/>Online Job Portal &<br/>Employment Trend<br/>Analysis System"))
    EmailSystem["External Entity:<br/><b>Async Notification Service</b>"]

    Student -- "Profile registration, Job applications" --> System
    System -- "Eligibility alerts, Vacancy listings, App Status" --> Student

    Recruiter -- "Employer profile, Job vacancies, Selection status" --> System
    System -- "Candidate roster, Applicant resumes" --> Recruiter

    Admin -- "Reporting queries, Audit parameters" --> System
    System -- "Department placement rates, 8 Trend charts" --> Admin

    System -- "Email / SMS notification triggers" --> EmailSystem
```

### 2.2 DFD Level 1: Functional Decomposition
Decomposes the system into 5 primary logical subprocesses and illustrates interactions with relational data stores (`D1` through `D4`).

```mermaid
flowchart TD
    Student["Student"]
    Recruiter["Corporate Recruiter"]
    Admin["Placement Officer"]

    P1(("1.0<br/>Student Profile<br/>Management"))
    P2(("2.0<br/>Company & Vacancy<br/>Publishing"))
    P3(("3.0<br/>Eligibility & Application<br/>Processing"))
    P4(("4.0<br/>Recruitment Status<br/>Tracking"))
    P5(("5.0<br/>Reporting & Trend<br/>Analytics"))

    D1[("D1: Students Store")]
    D2[("D2: Companies Store")]
    D3[("D3: Vacancies Store")]
    D4[("D4: Applications Store")]

    Student -->|Profile info| P1
    P1 -->|Write record| D1

    Recruiter -->|Employer info| P2
    P2 -->|Write company| D2
    Recruiter -->|Job vacancy details| P2
    P2 -->|Write vacancy| D3

    Student -->|Apply request (student_id, vacancy_id)| P3
    D1 -->|Read candidate CGPA & Dept| P3
    D3 -->|Read Cutoff & Allowed Depts| P3
    P3 -->|Check duplicate & insert| D4
    P3 -->|Success confirmation / Ineligibility exception| Student

    Recruiter -->|Update interview round & status| P4
    P4 -->|Update record| D4
    P4 -->|Status update alert| Student

    Admin -->|Request institutional analytics| P5
    D1 -->|Student counts| P5
    D4 -->|Selection records| P5
    D3 -->|Salary benchmarks| P5
    P5 -->|Placement tables & 8 Visual Charts| Admin
```

---

## 3. UML USE CASE DIAGRAM

The Use Case Diagram captures all functional interactions between system actors (**Student**, **Corporate Recruiter**, **Placement Officer**, and **Background Async Notifier**) and defines formal `<<includes>>` and `<<triggers>>` dependencies.

```mermaid
flowchart LR
    Student(["Student / Candidate"])
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

## 4. UML CLASS DIAGRAM

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

## 5. UML SEQUENCE DIAGRAM: JOB APPLICATION WORKFLOW

Details runtime message exchange, validation guards, and asynchronous concurrency during a job application attempt.

```mermaid
sequenceDiagram
    autonumber
    actor Student as Student (Candidate)
    participant UI as Presentation (CLI / Web)
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

---

## 6. UML ACTIVITY DIAGRAM

Models the step-by-step procedural control flow and decision diamonds of candidate job application processing.

```mermaid
flowchart TD
    StartNode((Start)) --> InputData["Candidate Selects Vacancy & Submits Profile"]
    InputData --> CheckDuplicate{"Has Candidate<br/>Already Applied?"}

    CheckDuplicate -- "Yes" --> RejectDuplicate["Throw DuplicateApplicationException<br/>and Display Flash Error"]
    RejectDuplicate --> EndNode((End))

    CheckDuplicate -- "No" --> CheckCGPA{"Is Candidate CGPA<br/>>= Minimum Cutoff?"}

    CheckCGPA -- "No" --> RejectCGPA["Throw IneligibleCandidateException<br/>(Low CGPA Reason)"]
    RejectCGPA --> EndNode

    CheckCGPA -- "Yes" --> CheckDept{"Is Department in<br/>Eligible List or 'ALL'?"}

    CheckDept -- "No" --> RejectDept["Throw IneligibleCandidateException<br/>(Dept Mismatch Reason)"]
    RejectDept --> EndNode

    CheckDept -- "Yes" --> ForkNode[=== Fork Parallel Tasks ===]

    ForkNode --> SaveDB["Insert Record into applications table<br/>Status: 'APPLIED', Round: 1"]
    ForkNode --> AsyncNotify["Spawn Background Thread:<br/>Dispatch Email & SMS Confirmation"]

    SaveDB --> JoinNode[=== Join Operations ===]
    AsyncNotify --> JoinNode

    JoinNode --> SuccessMsg["Display Application Success Confirmation<br/>Generate Tracking ID #appId"]
    SuccessMsg --> EndNode
```

---

## 7. UML STATE MACHINE DIAGRAM

Models the dynamic states and transitions of an **Application** entity from submission through multi-round evaluation to final placement.

```mermaid
stateDiagram-v2
    [*] --> APPLIED: Student submits valid application
    
    APPLIED --> SHORTLISTED: Recruiter reviews profile & clears Round 1
    APPLIED --> REJECTED: Candidate fails initial screening
    
    SHORTLISTED --> SHORTLISTED: Advances to subsequent rounds (Round 2, Technical, HR)
    SHORTLISTED --> SELECTED: Successfully clears all evaluation rounds (Offer Extended)
    SHORTLISTED --> REJECTED: Candidate rejected in intermediate interview round
    
    SELECTED --> [*]: Final Placement Confirmed
    REJECTED --> [*]: Candidate Evaluation Terminated
```

---

## 8. UML COMPONENT DIAGRAM

Illustrates the software modular components, interfaces, and physical dependency wiring across tiers.

```mermaid
flowchart TB
    subgraph Presentation_Component ["Presentation Component"]
        FlaskApp["Web Controller (web/app.py)"]
        CLIMenu["Console CLI (src/.../Main.java)"]
        Templates["Bootstrap 5 Templates & HTML Views"]
    end

    subgraph Service_Component ["Business Logic & Service Component"]
        ServiceIf["<<interface>><br/>JobPortalService"]
        ServiceImpl["JobPortalServiceImpl"]
        Validator["InputValidator & Business Rules"]
        AsyncWorker["Async Notifier Thread"]
        ReportGen["ReportGenerator (Stream API)"]
    end

    subgraph DAO_Component ["Data Access Object Component"]
        StudentDAO["StudentDAO"]
        VacancyDAO["VacancyDAO"]
        ApplicationDAO["ApplicationDAO"]
        CompanyDAO["CompanyDAO"]
    end

    subgraph Persistence_Component ["Data Storage Component"]
        DBCreds["Singleton DatabaseConnection"]
        SQLiteFile[("job_portal.db (SQLite3)")]
    end

    subgraph Analytics_Component ["Python Data Science Component"]
        DBRetriever["db_retrieval.py"]
        Preprocessor["data_preprocessing.py (IQR Outliers)"]
        VizEngine["visualizations.py (8 Charts)"]
    end

    FlaskApp --> Templates
    FlaskApp --> ServiceIf
    CLIMenu --> ServiceIf
    ServiceIf <|.. ServiceImpl
    ServiceImpl --> Validator
    ServiceImpl --> AsyncWorker
    ServiceImpl --> ReportGen
    ServiceImpl --> StudentDAO
    ServiceImpl --> VacancyDAO
    ServiceImpl --> ApplicationDAO
    ServiceImpl --> CompanyDAO

    StudentDAO --> DBCreds
    VacancyDAO --> DBCreds
    ApplicationDAO --> DBCreds
    CompanyDAO --> DBCreds
    DBCreds --> SQLiteFile

    SQLiteFile --> DBRetriever
    DBRetriever --> Preprocessor
    Preprocessor --> VizEngine
```

---

## 9. UML DEPLOYMENT DIAGRAM

Details the physical execution nodes, client hardware devices, software runtime environments, and communication protocols.

```mermaid
flowchart TB
    subgraph ClientNode ["<<device>> Client Workstation / Browser"]
        subgraph BrowserEnv ["<<execution environment>> Web Browser"]
            BrowserUI["HTML5 / Bootstrap 5 / JavaScript Web Interface"]
        end
        subgraph TerminalEnv ["<<execution environment>> Terminal Console"]
            CLIConsole["PowerShell / Command Prompt Standard I/O"]
        end
    end

    subgraph ServerNode ["<<device>> Local Application Host / Server"]
        subgraph FlaskRuntime ["<<execution environment>> Python 3.12 Runtime"]
            FlaskServer["Flask WSGI HTTP Server (:5000)"]
            AnalyticsScripts["NumPy / Matplotlib Analytics Engine"]
        end

        subgraph JavaRuntime ["<<execution environment>> OpenJDK 17 LTS"]
            JavaJVM["Java Virtual Machine (JVM)"]
            JDBCDriver["sqlite-jdbc-3.45.1.0.jar Driver"]
        end

        subgraph FileSystem ["<<file system>> Local Relational Storage"]
            SQLiteDB[("<<artifact>><br/>database/job_portal.db")]
            CSVStore["<<artifact>><br/>output/*.csv & output/visualizations/*.png"]
        end
    end

    BrowserUI -- "HTTP GET / POST (:5000)" --> FlaskServer
    CLIConsole -- "Standard In/Out" --> JavaJVM
    FlaskServer -- "sqlite3 Driver" --> SQLiteDB
    JavaJVM -- "JDBC Connection" --> JDBCDriver
    JDBCDriver -- "Direct File Access" --> SQLiteDB
    AnalyticsScripts -- "Read SQLite / Write CSV & PNG" --> FileSystem
```
