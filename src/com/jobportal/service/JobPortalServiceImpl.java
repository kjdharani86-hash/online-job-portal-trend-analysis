package com.jobportal.service;

import com.jobportal.dao.ApplicationDAO;
import com.jobportal.dao.CompanyDAO;
import com.jobportal.dao.StudentDAO;
import com.jobportal.dao.VacancyDAO;
import com.jobportal.exception.DuplicateApplicationException;
import com.jobportal.exception.IneligibleCandidateException;
import com.jobportal.exception.RecordNotFoundException;
import com.jobportal.model.Application;
import com.jobportal.model.ApplicationStatus;
import com.jobportal.model.Company;
import com.jobportal.model.Student;
import com.jobportal.model.Vacancy;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Layer Implementation containing core business rules, validation,
 * eligibility algorithms, multithreaded notification dispatching, and Stream API operations.
 * Course: 25ITT32 Java Programming (Units 2, 3, 4, 5)
 * Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
 */
public class JobPortalServiceImpl implements JobPortalService {

    private final StudentDAO studentDAO;
    private final CompanyDAO companyDAO;
    private final VacancyDAO vacancyDAO;
    private final ApplicationDAO applicationDAO;

    public JobPortalServiceImpl() {
        this.studentDAO = new StudentDAO();
        this.companyDAO = new CompanyDAO();
        this.vacancyDAO = new VacancyDAO();
        this.applicationDAO = new ApplicationDAO();
    }

    @Override
    public boolean registerStudent(Student student) throws SQLException {
        if (student == null) {
            throw new IllegalArgumentException("Student object cannot be null.");
        }
        return studentDAO.insertStudent(student);
    }

    @Override
    public Student getStudent(String studentId) throws SQLException, RecordNotFoundException {
        Student s = studentDAO.getStudentById(studentId);
        if (s == null) {
            throw new RecordNotFoundException("Student with ID '" + studentId + "' was not found.");
        }
        return s;
    }

    @Override
    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    @Override
    public int registerCompany(Company company) throws SQLException {
        if (company == null) {
            throw new IllegalArgumentException("Company object cannot be null.");
        }
        return companyDAO.insertCompany(company);
    }

    @Override
    public List<Company> getAllCompanies() throws SQLException {
        return companyDAO.getAllCompanies();
    }

    @Override
    public int postJobVacancy(Vacancy vacancy) throws SQLException, RecordNotFoundException {
        if (companyDAO.getCompanyById(vacancy.getCompanyId()) == null) {
            throw new RecordNotFoundException("Company ID " + vacancy.getCompanyId() + " does not exist.");
        }
        return vacancyDAO.insertVacancy(vacancy);
    }

    @Override
    public List<Vacancy> getAllVacancies() throws SQLException {
        return vacancyDAO.getAllVacancies();
    }

    @Override
    public List<Vacancy> getOpenVacancies() throws SQLException {
        return vacancyDAO.getOpenVacancies();
    }

    @Override
    public List<Vacancy> getEligibleVacanciesForStudent(String studentId) throws SQLException, RecordNotFoundException {
        Student student = getStudent(studentId);
        List<Vacancy> openVacancies = vacancyDAO.getOpenVacancies();

        // Using Java Streams to filter vacancies based on eligibility rules
        return openVacancies.stream()
                .filter(v -> isEligible(student, v))
                .collect(Collectors.toList());
    }

    private boolean isEligible(Student student, Vacancy vacancy) {
        // 1. CGPA Criterion
        if (student.getCgpa() < vacancy.getMinCgpa()) {
            return false;
        }

        // 2. Department Criterion
        String eligibleDepts = vacancy.getEligibleDepartments().toUpperCase().trim();
        if ("ALL".equalsIgnoreCase(eligibleDepts)) {
            return true;
        }

        List<String> allowedList = Arrays.stream(eligibleDepts.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        return allowedList.contains(student.getDepartment().toUpperCase().trim());
    }

    @Override
    public int applyForJob(String studentId, int vacancyId, String remarks)
            throws SQLException, DuplicateApplicationException, IneligibleCandidateException, RecordNotFoundException {

        // Step 1: Verify student exists
        Student student = getStudent(studentId);

        // Step 2: Verify vacancy exists and is OPEN
        Vacancy vacancy = vacancyDAO.getVacancyById(vacancyId);
        if (vacancy == null) {
            throw new RecordNotFoundException("Job Vacancy with ID " + vacancyId + " does not exist.");
        }
        if (!"OPEN".equalsIgnoreCase(vacancy.getStatus())) {
            throw new IneligibleCandidateException(studentId, vacancyId, "This vacancy is currently CLOSED.");
        }

        // Step 3: Prevent duplicate applications (Functional Requirement 4)
        if (applicationDAO.hasAlreadyApplied(studentId, vacancyId)) {
            throw new DuplicateApplicationException(studentId, vacancyId);
        }

        // Step 4: Validate eligibility criteria (Functional Requirement 3)
        if (student.getCgpa() < vacancy.getMinCgpa()) {
            throw new IneligibleCandidateException(studentId, vacancyId,
                    String.format("CGPA %.2f is below the required cutoff of %.2f", student.getCgpa(), vacancy.getMinCgpa()));
        }

        String allowedDepts = vacancy.getEligibleDepartments().toUpperCase().trim();
        if (!"ALL".equalsIgnoreCase(allowedDepts)) {
            List<String> deptList = Arrays.stream(allowedDepts.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            if (!deptList.contains(student.getDepartment().toUpperCase().trim())) {
                throw new IneligibleCandidateException(studentId, vacancyId,
                        String.format("Department '%s' is not in allowed list (%s)", student.getDepartment(), allowedDepts));
            }
        }

        // Step 5: Persist application in database
        int applicationId = applicationDAO.submitApplication(studentId, vacancyId, remarks);

        // Step 6: Asynchronous Notification Dispatch (Unit 3: Multithreading)
        dispatchNotificationAsync(student, vacancy, applicationId);

        return applicationId;
    }

    /**
     * Spawns a background worker thread to simulate asynchronous notification delivery.
     * Demonstrates Java Thread Model, Runnable interface, and Multithreading.
     */
    private void dispatchNotificationAsync(Student student, Vacancy vacancy, int applicationId) {
        Thread notificationThread = new Thread(() -> {
            try {
                // Simulate asynchronous network/email delivery latency
                Thread.sleep(600);
                System.out.printf("%n[ASYNC NOTIFIER] Application #%d submitted successfully!%n" +
                                "[ASYNC NOTIFIER] Email dispatched to %s for '%s' at %s.%n",
                        applicationId, student.getEmail(), vacancy.getJobTitle(), vacancy.getCompanyName());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        notificationThread.setDaemon(true);
        notificationThread.start();
    }

    @Override
    public boolean updateApplicationStatus(int applicationId, ApplicationStatus status, int interviewRound, String remarks)
            throws SQLException, RecordNotFoundException {
        Application app = applicationDAO.getApplicationById(applicationId);
        if (app == null) {
            throw new RecordNotFoundException("Application ID " + applicationId + " was not found.");
        }
        return applicationDAO.updateApplicationStatus(applicationId, status, interviewRound, remarks);
    }

    @Override
    public List<Application> getStudentApplications(String studentId) throws SQLException {
        return applicationDAO.getApplicationsByStudent(studentId);
    }

    @Override
    public List<Application> getVacancyApplications(int vacancyId) throws SQLException {
        return applicationDAO.getApplicationsByVacancy(vacancyId);
    }

    @Override
    public List<Application> getAllApplications() throws SQLException {
        return applicationDAO.getAllApplications();
    }
}
