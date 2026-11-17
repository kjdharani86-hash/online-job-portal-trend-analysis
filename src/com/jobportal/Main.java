package com.jobportal;

import com.jobportal.config.DatabaseConnection;
import com.jobportal.exception.DuplicateApplicationException;
import com.jobportal.exception.IneligibleCandidateException;
import com.jobportal.exception.RecordNotFoundException;
import com.jobportal.model.Application;
import com.jobportal.model.ApplicationStatus;
import com.jobportal.model.Company;
import com.jobportal.model.Student;
import com.jobportal.model.Vacancy;
import com.jobportal.service.JobPortalService;
import com.jobportal.service.JobPortalServiceImpl;
import com.jobportal.util.InputValidator;
import com.jobportal.util.ReportGenerator;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the Online Job Portal and Employment Trend Analysis System.
 * Scenario 22 | Department of Information Technology
 * Velalar College of Engineering and Technology, Erode - 12
 * Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final JobPortalService service = new JobPortalServiceImpl();

    public static void main(String[] args) {
        // If run with '--auto-demo', immediately execute the automated scenario walkthrough
        if (args.length > 0 && "--auto-demo".equalsIgnoreCase(args[0])) {
            runAutomatedDemonstration();
            return;
        }

        printBanner();

        boolean running = true;
        while (running) {
            printMainMenu();
            System.out.print("Enter your choice [1-5]: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    handleStudentMenu();
                    break;
                case "2":
                    handleRecruiterMenu();
                    break;
                case "3":
                    handlePlacementOfficerMenu();
                    break;
                case "4":
                    runAutomatedDemonstration();
                    break;
                case "5":
                    running = false;
                    System.out.println("\n[!] Exiting Online Job Portal. Goodbye!");
                    DatabaseConnection.closeConnection();
                    break;
                default:
                    System.out.println("[ERROR] Invalid choice. Please select an option between 1 and 5.");
            }
        }
    }

    private static void printBanner() {
        System.out.println("==========================================================================================");
        System.out.println("          VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY, ERODE - 12 (AUTONOMOUS)         ");
        System.out.println("                               DEPARTMENT OF INFORMATION TECHNOLOGY                       ");
        System.out.println("             Scenario 22: Online Job Portal & Employment Trend Analysis System            ");
        System.out.println("               Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A               ");
        System.out.println("==========================================================================================");
    }

    private static void printMainMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                     MAIN DASHBOARD");
        System.out.println("=".repeat(60));
        System.out.println("  1. Student / Job Seeker Portal");
        System.out.println("  2. Company / Recruiter Portal");
        System.out.println("  3. Placement Cell / Admin Analytics");
        System.out.println("  4. Run Full Scenario Automated Walkthrough (Demo)");
        System.out.println("  5. Exit System");
        System.out.println("=".repeat(60));
    }

    // =========================================================================
    // 1. STUDENT MODULE
    // =========================================================================
    private static void handleStudentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- STUDENT / JOB SEEKER PORTAL ---");
            System.out.println("1. Register New Student Profile");
            System.out.println("2. View Student Profile Details");
            System.out.println("3. Browse All Open Job Vacancies");
            System.out.println("4. Check Eligible Vacancies for My Profile");
            System.out.println("5. Apply for a Job Vacancy (Validates Eligibility & Duplicate Check)");
            System.out.println("6. View My Submitted Applications & Status");
            System.out.println("7. Return to Main Dashboard");
            System.out.print("Select an option [1-7]: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    registerStudent();
                    break;
                case "2":
                    viewStudentProfile();
                    break;
                case "3":
                    browseOpenVacancies();
                    break;
                case "4":
                    checkEligibleVacancies();
                    break;
                case "5":
                    applyForVacancy();
                    break;
                case "6":
                    viewStudentApplications();
                    break;
                case "7":
                    back = true;
                    break;
                default:
                    System.out.println("[ERROR] Invalid selection. Choose 1-7.");
            }
        }
    }

    private static void registerStudent() {
        System.out.println("\n--- Register Student Profile ---");
        try {
            System.out.print("Enter Student Register Number (e.g. 732925ITR099): ");
            String id = scanner.nextLine().trim();
            if (!InputValidator.isNotEmpty(id)) {
                System.out.println("[ERROR] Student ID cannot be empty.");
                return;
            }

            System.out.print("Enter Full Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter Department (IT/CSE/ECE/EEE/MECH): ");
            String dept = scanner.nextLine().trim().toUpperCase();

            System.out.print("Enter Qualification (e.g. B.Tech IT, B.E CSE): ");
            String qual = scanner.nextLine().trim();

            System.out.print("Enter CGPA (0.00 - 10.00): ");
            double cgpa = Double.parseDouble(scanner.nextLine().trim());
            if (!InputValidator.isValidCgpa(cgpa)) {
                System.out.println("[ERROR] Invalid CGPA! Must be between 0.00 and 10.00.");
                return;
            }

            System.out.print("Enter Skills (comma-separated, e.g. Java, Python, SQL): ");
            String skills = scanner.nextLine().trim();

            System.out.print("Enter Email Address: ");
            String email = scanner.nextLine().trim();
            if (!InputValidator.isValidEmail(email)) {
                System.out.println("[ERROR] Invalid email address format.");
                return;
            }

            System.out.print("Enter 10-digit Phone Number: ");
            String phone = scanner.nextLine().trim();
            if (!InputValidator.isValidPhone(phone)) {
                System.out.println("[ERROR] Phone number must be 10 digits.");
                return;
            }

            Student student = new Student(id, name, dept, qual, cgpa, skills, email, phone);
            boolean created = service.registerStudent(student);
            if (created) {
                System.out.println("[SUCCESS] Student registered successfully with ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Numeric format error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] Failed to register student: " + e.getMessage());
        }
    }

    private static void viewStudentProfile() {
        System.out.print("\nEnter Student Register Number: ");
        String id = scanner.nextLine().trim();
        try {
            Student s = service.getStudent(id);
            System.out.println("\n+-------------------------------------------------------+");
            System.out.println("|                   STUDENT PROFILE                     |");
            System.out.println("+-------------------------------------------------------+");
            System.out.printf("| Register No   : %-37s |%n", s.getStudentId());
            System.out.printf("| Full Name     : %-37s |%n", s.getName());
            System.out.printf("| Department    : %-37s |%n", s.getDepartment());
            System.out.printf("| Qualification : %-37s |%n", s.getQualification());
            System.out.printf("| CGPA          : %-37.2f |%n", s.getCgpa());
            System.out.printf("| Skills        : %-37s |%n", (s.getSkills().length() > 37 ? s.getSkills().substring(0, 34) + "..." : s.getSkills()));
            System.out.printf("| Email         : %-37s |%n", s.getEmail());
            System.out.printf("| Phone         : %-37s |%n", s.getPhone());
            System.out.println("+-------------------------------------------------------+");
        } catch (RecordNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] " + e.getMessage());
        }
    }

    private static void browseOpenVacancies() {
        try {
            List<Vacancy> vacancies = service.getOpenVacancies();
            ReportGenerator.printVacancyReport(vacancies);
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] Failed to fetch vacancies: " + e.getMessage());
        }
    }

    private static void checkEligibleVacancies() {
        System.out.print("\nEnter Student Register Number: ");
        String id = scanner.nextLine().trim();
        try {
            List<Vacancy> eligible = service.getEligibleVacanciesForStudent(id);
            Student s = service.getStudent(id);
            System.out.printf("\n[INFO] Found %d matching open vacancies for %s (Dept: %s, CGPA: %.2f)%n",
                    eligible.size(), s.getName(), s.getDepartment(), s.getCgpa());
            ReportGenerator.printVacancyReport(eligible);
        } catch (RecordNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] " + e.getMessage());
        }
    }

    private static void applyForVacancy() {
        System.out.println("\n--- Submit Job Application ---");
        try {
            System.out.print("Enter Student Register Number: ");
            String studentId = scanner.nextLine().trim();

            System.out.print("Enter Vacancy ID: ");
            int vacancyId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Application Remarks / Note (optional): ");
            String remarks = scanner.nextLine().trim();

            int appId = service.applyForJob(studentId, vacancyId, remarks);
            System.out.println("\n[SUCCESS] Application registered successfully! Generated Application ID: #" + appId);
            System.out.println("[STATUS] Application status is marked as: APPLIED");
        } catch (DuplicateApplicationException e) {
            System.out.println("\n[VALIDATION FAILED - DUPLICATE DETECTED]");
            System.out.println("[!] " + e.getMessage());
        } catch (IneligibleCandidateException e) {
            System.out.println("\n[VALIDATION FAILED - INELIGIBLE CANDIDATE]");
            System.out.println("[!] " + e.getMessage());
        } catch (RecordNotFoundException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid Vacancy ID number.");
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] " + e.getMessage());
        }
    }

    private static void viewStudentApplications() {
        System.out.print("\nEnter Student Register Number: ");
        String id = scanner.nextLine().trim();
        try {
            List<Application> apps = service.getStudentApplications(id);
            if (apps.isEmpty()) {
                System.out.println("[INFO] No applications found for Student: " + id);
            } else {
                ReportGenerator.printApplicationReport(apps);
            }
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] " + e.getMessage());
        }
    }

    // =========================================================================
    // 2. RECRUITER / COMPANY MODULE
    // =========================================================================
    private static void handleRecruiterMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- COMPANY / RECRUITER PORTAL ---");
            System.out.println("1. Register New Company Profile");
            System.out.println("2. Browse Registered Companies");
            System.out.println("3. Publish New Job Vacancy");
            System.out.println("4. View All Applicants for a Specific Vacancy");
            System.out.println("5. Update Candidate Application Status (Track Recruitment)");
            System.out.println("6. Return to Main Dashboard");
            System.out.print("Select an option [1-6]: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    registerCompany();
                    break;
                case "2":
                    browseCompanies();
                    break;
                case "3":
                    publishVacancy();
                    break;
                case "4":
                    viewApplicantsForVacancy();
                    break;
                case "5":
                    updateCandidateStatus();
                    break;
                case "6":
                    back = true;
                    break;
                default:
                    System.out.println("[ERROR] Invalid selection. Choose 1-6.");
            }
        }
    }

    private static void registerCompany() {
        System.out.println("\n--- Register Company ---");
        try {
            System.out.print("Enter Company Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter Industry Domain (e.g. IT Services, FinTech, Automotive): ");
            String industry = scanner.nextLine().trim();

            System.out.print("Enter Location (e.g. Chennai, Bangalore, Coimbatore): ");
            String loc = scanner.nextLine().trim();

            System.out.print("Enter HR Contact Email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Enter Website URL: ");
            String website = scanner.nextLine().trim();

            Company c = new Company(name, industry, loc, email, website);
            int id = service.registerCompany(c);
            System.out.println("[SUCCESS] Company registered with ID: " + id);
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] " + e.getMessage());
        }
    }

    private static void browseCompanies() {
        try {
            List<Company> list = service.getAllCompanies();
            System.out.println("\n" + "=".repeat(88));
            System.out.println("                          REGISTERED CORPORATE RECRUITERS");
            System.out.println("=".repeat(88));
            System.out.printf("| %-4s | %-28s | %-24s | %-12s | %-24s |%n", "ID", "Company Name", "Industry", "Location", "Contact Email");
            System.out.println("-".repeat(88));
            list.forEach(System.out::println);
            System.out.println("=".repeat(88));
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] " + e.getMessage());
        }
    }

    private static void publishVacancy() {
        System.out.println("\n--- Publish New Job Vacancy ---");
        try {
            System.out.print("Enter Registered Company ID: ");
            int companyId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Job Title: ");
            String title = scanner.nextLine().trim();

            System.out.print("Enter Minimum CGPA Required (0.0 - 10.0): ");
            double minCgpa = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter Required Skills: ");
            String skills = scanner.nextLine().trim();

            System.out.print("Enter Allowed Departments (comma-separated, e.g. IT,CSE,ECE or ALL): ");
            String depts = scanner.nextLine().trim();

            System.out.print("Enter Job Location: ");
            String loc = scanner.nextLine().trim();

            System.out.print("Enter Minimum Salary (INR per annum, e.g. 500000): ");
            int salMin = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Maximum Salary (INR per annum, e.g. 800000): ");
            int salMax = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Number of Openings: ");
            int openings = Integer.parseInt(scanner.nextLine().trim());

            Vacancy v = new Vacancy(0, companyId, null, title, minCgpa, skills, depts, loc, salMin, salMax, openings, "OPEN", null);
            int vId = service.postJobVacancy(v);
            System.out.println("[SUCCESS] Vacancy posted successfully! Vacancy ID: " + vId);
        } catch (RecordNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid numeric format entered.");
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] " + e.getMessage());
        }
    }

    private static void viewApplicantsForVacancy() {
        System.out.print("\nEnter Vacancy ID: ");
        try {
            int vId = Integer.parseInt(scanner.nextLine().trim());
            List<Application> apps = service.getVacancyApplications(vId);
            if (apps.isEmpty()) {
                System.out.println("[INFO] No applications received yet for Vacancy ID: " + vId);
            } else {
                ReportGenerator.printApplicationReport(apps);
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid number.");
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] " + e.getMessage());
        }
    }

    private static void updateCandidateStatus() {
        System.out.println("\n--- Update Application Status (Recruitment Tracking) ---");
        try {
            System.out.print("Enter Application ID: ");
            int appId = Integer.parseInt(scanner.nextLine().trim());

            System.out.println("Select New Status:");
            System.out.println("  1. APPLIED");
            System.out.println("  2. SHORTLISTED");
            System.out.println("  3. SELECTED");
            System.out.println("  4. REJECTED");
            System.out.print("Choose status [1-4]: ");
            String sChoice = scanner.nextLine().trim();
            ApplicationStatus newStatus;
            switch (sChoice) {
                case "1": newStatus = ApplicationStatus.APPLIED; break;
                case "2": newStatus = ApplicationStatus.SHORTLISTED; break;
                case "3": newStatus = ApplicationStatus.SELECTED; break;
                case "4": newStatus = ApplicationStatus.REJECTED; break;
                default:
                    System.out.println("[ERROR] Invalid status choice.");
                    return;
            }

            System.out.print("Enter Current Interview Round (e.g. 1, 2, 3): ");
            int round = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Evaluator Remarks: ");
            String remarks = scanner.nextLine().trim();

            boolean ok = service.updateApplicationStatus(appId, newStatus, round, remarks);
            if (ok) {
                System.out.printf("[SUCCESS] Application #%d updated to status: %s (Round %d)%n",
                        appId, newStatus.name(), round);
            }
        } catch (RecordNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid numeric input.");
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] " + e.getMessage());
        }
    }

    // =========================================================================
    // 3. PLACEMENT OFFICER / ADMIN ANALYTICS MODULE
    // =========================================================================
    private static void handlePlacementOfficerMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- PLACEMENT CELL / INSTITUTIONAL ANALYTICS ---");
            System.out.println("1. View All Registered Students");
            System.out.println("2. Generate Vacancies Report");
            System.out.println("3. Generate Application Pipeline Report");
            System.out.println("4. Generate Application Status Breakdown Report");
            System.out.println("5. Generate Company-Wise Recruitment Report");
            System.out.println("6. Generate Department-Wise Placement Trends Report");
            System.out.println("7. Return to Main Dashboard");
            System.out.print("Select an option [1-7]: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    viewAllStudents();
                    break;
                case "2":
                    generateVacancyReport();
                    break;
                case "3":
                    generateApplicationReport();
                    break;
                case "4":
                    generateSelectionBreakdownReport();
                    break;
                case "5":
                    generateCompanyRecruitmentReport();
                    break;
                case "6":
                    generateDepartmentPlacementReport();
                    break;
                case "7":
                    back = true;
                    break;
                default:
                    System.out.println("[ERROR] Invalid choice. Choose 1-7.");
            }
        }
    }

    private static void viewAllStudents() {
        try {
            List<Student> students = service.getAllStudents();
            System.out.println("\n" + "=".repeat(125));
            System.out.println("                                REGISTERED STUDENTS DIRECTORY");
            System.out.println("=".repeat(125));
            System.out.printf("| %-14s | %-16s | %-6s | %-10s | %-4s | %-28s | %-25s |%n",
                    "Student ID", "Full Name", "Dept", "Degree", "CGPA", "Key Competencies", "Email Address");
            System.out.println("-".repeat(125));
            students.forEach(System.out::println);
            System.out.println("=".repeat(125));
            System.out.printf("Total Enrolled Candidates: %d%n", students.size());
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] " + e.getMessage());
        }
    }

    private static void generateVacancyReport() {
        try {
            ReportGenerator.printVacancyReport(service.getAllVacancies());
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] " + e.getMessage());
        }
    }

    private static void generateApplicationReport() {
        try {
            ReportGenerator.printApplicationReport(service.getAllApplications());
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] " + e.getMessage());
        }
    }

    private static void generateSelectionBreakdownReport() {
        try {
            ReportGenerator.printSelectionCountReport(service.getAllApplications());
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] " + e.getMessage());
        }
    }

    private static void generateCompanyRecruitmentReport() {
        try {
            ReportGenerator.printCompanyRecruitmentReport(service.getAllApplications());
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] " + e.getMessage());
        }
    }

    private static void generateDepartmentPlacementReport() {
        try {
            ReportGenerator.printDepartmentTrendReport(service.getAllStudents(), service.getAllApplications());
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] " + e.getMessage());
        }
    }

    // =========================================================================
    // 4. AUTOMATED SCENARIO DEMONSTRATION (TEST WALKTHROUGH)
    // =========================================================================
    public static void runAutomatedDemonstration() {
        System.out.println("\n" + "*".repeat(80));
        System.out.println("      AUTOMATED SYSTEM TEST & SCENARIO DEMONSTRATION WALKTHROUGH");
        System.out.println("      Scenario 22: Online Job Portal & Employment Trend Analysis");
        System.out.println("*".repeat(80));

        try {
            // Step 1: Display Author and System Details
            System.out.println("\n[DEMO 1] Registering a Test Student Profile...");
            String testStudentId = "732925ITR999";
            Student testStudent = new Student(
                    testStudentId, "Rithanya M", "IT", "B.Tech IT",
                    8.25, "Java, Python, Cloud, SQL", "rithanya.test@vcet.ac.in", "9876543299"
            );
            // Handle if already present from previous run
            try {
                service.registerStudent(testStudent);
                System.out.println("[OK] Test Student Registered: " + testStudent.getName() + " (" + testStudentId + ")");
            } catch (SQLException e) {
                System.out.println("[INFO] Test student already registered in database.");
            }

            // Step 2: Show Eligible Vacancies
            System.out.println("\n[DEMO 2] Checking Eligible Vacancies for Candidate (IT, CGPA 8.25)...");
            List<Vacancy> eligibleVacancies = service.getEligibleVacanciesForStudent(testStudentId);
            System.out.printf("[OK] Found %d matching open vacancies.%n", eligibleVacancies.size());

            // Step 3: Test Ineligible Application Attempt
            System.out.println("\n[DEMO 3] Testing Eligibility Enforcement (Negative Test)...");
            // Vacancy 11 is Embedded Software Engineer (min CGPA 7.5, depts: ECE,EEE) - Candidate is IT!
            try {
                System.out.println("[...] Attempting to apply for Vacancy #11 (Restricted to ECE,EEE)...");
                service.applyForJob(testStudentId, 11, "Testing department check");
                System.out.println("[!] ERROR: Ineligibility check failed!");
            } catch (IneligibleCandidateException e) {
                System.out.println("[OK EXPECTED EXCEPTION CAUGHT] " + e.getMessage());
            }

            // Step 4: Test Valid Application Submission
            System.out.println("\n[DEMO 4] Testing Valid Job Application Submission...");
            int appId = -1;
            try {
                appId = service.applyForJob(testStudentId, 1, "Top candidate application for Zoho SDE");
                System.out.println("[SUCCESS] Successfully applied for Vacancy #1! Application ID: #" + appId);
            } catch (DuplicateApplicationException e) {
                System.out.println("[INFO] Student had already applied for Vacancy #1. Using existing application.");
            }

            // Step 5: Test Duplicate Application Prevention
            System.out.println("\n[DEMO 5] Testing Duplicate Application Prevention (Requirement 4)...");
            try {
                System.out.println("[...] Attempting duplicate application for Vacancy #1 by same student...");
                service.applyForJob(testStudentId, 1, "Duplicate submission attempt");
                System.out.println("[!] ERROR: Duplicate was NOT prevented!");
            } catch (DuplicateApplicationException e) {
                System.out.println("[OK EXPECTED EXCEPTION CAUGHT] " + e.getMessage());
            }

            // Step 6: Test Recruitment Status Progression
            if (appId > 0) {
                System.out.println("\n[DEMO 6] Tracking Recruitment Pipeline Updates...");
                service.updateApplicationStatus(appId, ApplicationStatus.SHORTLISTED, 2, "Cleared round 1 coding test");
                System.out.println("[OK] Status updated to: SHORTLISTED (Round 2)");

                service.updateApplicationStatus(appId, ApplicationStatus.SELECTED, 3, "Selected after executive interview");
                System.out.println("[OK] Status updated to: SELECTED (Offer Extended)");
            }

            // Step 7: Generate Official Reports
            System.out.println("\n[DEMO 7] Generating Management and Analytics Reports...");
            ReportGenerator.printSelectionCountReport(service.getAllApplications());
            ReportGenerator.printCompanyRecruitmentReport(service.getAllApplications());
            ReportGenerator.printDepartmentTrendReport(service.getAllStudents(), service.getAllApplications());

            System.out.println("\n[COMPLETE] Automated demonstration executed successfully without errors!");

        } catch (Exception e) {
            System.out.println("[EXCEPTION IN DEMO] " + e.getMessage());
            e.printStackTrace();
        }
    }
}
