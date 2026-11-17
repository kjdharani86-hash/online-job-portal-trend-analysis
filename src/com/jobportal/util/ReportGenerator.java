package com.jobportal.util;

import com.jobportal.model.Application;
import com.jobportal.model.ApplicationStatus;
import com.jobportal.model.Student;
import com.jobportal.model.Vacancy;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Report generation engine implementing Functional Requirement 6.
 * Extensively leverages Collections Framework, Generics, Lambdas, and Stream API.
 * Course: 25ITT32 Java Programming (Unit 5: Generics, Collections, Lambdas & Streams)
 * Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
 */
public class ReportGenerator {

    /**
     * Report 1: Job Vacancy Summary Report
     */
    public static void printVacancyReport(List<Vacancy> vacancies) {
        System.out.println("\n" + "=".repeat(128));
        System.out.println("                         OFFICIAL REPORT: CURRENT JOB VACANCIES & ELIGIBILITY CRITERIA");
        System.out.println("=".repeat(128));
        System.out.printf("| %-4s | %-24s | %-26s | %-4s | %-12s | %-10s | %-16s | %-4s | %-6s |%n",
                "ID", "Company Name", "Job Role", "CGPA", "Allowed Dept", "Location", "Salary Package", "Open", "Status");
        System.out.println("-".repeat(128));

        vacancies.forEach(System.out::println);

        System.out.println("-".repeat(128));
        long totalOpenings = vacancies.stream().mapToInt(Vacancy::getOpenings).sum();
        long openRoles = vacancies.stream().filter(v -> "OPEN".equalsIgnoreCase(v.getStatus())).count();
        System.out.printf("Total Vacancy Listings: %d | Active Open Listings: %d | Cumulative Open Positions: %d%n",
                vacancies.size(), openRoles, totalOpenings);
        System.out.println("=".repeat(128));
    }

    /**
     * Report 2: Student Applications Pipeline Report
     */
    public static void printApplicationReport(List<Application> applications) {
        System.out.println("\n" + "=".repeat(116));
        System.out.println("                         OFFICIAL REPORT: STUDENT APPLICATIONS RECRUITMENT PIPELINE");
        System.out.println("=".repeat(116));
        System.out.printf("| %-4s | %-14s | %-16s | %-22s | %-22s | %-12s | %-7s |%n",
                "App#", "Student ID", "Candidate Name", "Company", "Job Position", "Status", "Round");
        System.out.println("-".repeat(116));

        applications.forEach(System.out::println);

        System.out.println("-".repeat(116));
        System.out.printf("Total Applications Processed: %d%n", applications.size());
        System.out.println("=".repeat(116));
    }

    /**
     * Report 3: Selection Counts Breakdown Report (Streams + GroupingBy)
     */
    public static void printSelectionCountReport(List<Application> applications) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("        RECRUITMENT SUMMARY: APPLICATION STATUS DISTRIBUTION");
        System.out.println("=".repeat(70));

        // Group by ApplicationStatus using Stream API
        Map<ApplicationStatus, Long> countsByStatus = applications.stream()
                .collect(Collectors.groupingBy(Application::getStatus, Collectors.counting()));

        System.out.printf("| %-20s | %-10s | %-28s |%n", "Application Status", "Count", "Percentage of Total");
        System.out.println("-".repeat(70));

        long total = applications.size();
        for (ApplicationStatus status : ApplicationStatus.values()) {
            long count = countsByStatus.getOrDefault(status, 0L);
            double pct = total > 0 ? (count * 100.0 / total) : 0.0;
            System.out.printf("| %-20s | %-10d | %6.2f%% %-20s |%n",
                    status.name(), count, pct, "#".repeat((int) (pct / 5)));
        }

        System.out.println("-".repeat(70));
        long selected = countsByStatus.getOrDefault(ApplicationStatus.SELECTED, 0L);
        System.out.printf("Total Decisions Recorded: %d | Total Offers / Selected: %d (%4.1f%%)%n",
                total, selected, total > 0 ? (selected * 100.0 / total) : 0.0);
        System.out.println("=".repeat(70));
    }

    /**
     * Report 4: Company-Wise Recruitment and Selection Report
     */
    public static void printCompanyRecruitmentReport(List<Application> applications) {
        System.out.println("\n" + "=".repeat(95));
        System.out.println("                   COMPANY-WISE CANDIDATE RECRUITMENT & SELECTION AUDIT");
        System.out.println("=".repeat(95));
        System.out.printf("| %-30s | %-12s | %-10s | %-12s | %-16s |%n",
                "Company Name", "Applicants", "Selected", "Shortlisted", "Selection Rate");
        System.out.println("-".repeat(95));

        // Group applications by Company Name
        Map<String, List<Application>> appsByCompany = applications.stream()
                .collect(Collectors.groupingBy(Application::getCompanyName));

        appsByCompany.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()))
                .forEach(entry -> {
                    String company = entry.getKey();
                    List<Application> list = entry.getValue();
                    int totalApps = list.size();
                    long selected = list.stream().filter(a -> a.getStatus() == ApplicationStatus.SELECTED).count();
                    long shortlisted = list.stream().filter(a -> a.getStatus() == ApplicationStatus.SHORTLISTED).count();
                    double rate = totalApps > 0 ? (selected * 100.0 / totalApps) : 0.0;

                    System.out.printf("| %-30s | %-12d | %-10d | %-12d | %12.2f%%   |%n",
                            company, totalApps, selected, shortlisted, rate);
                });

        System.out.println("=".repeat(95));
    }

    /**
     * Report 5: Department-Wise Recruitment Performance & Placement Ratio
     */
    public static void printDepartmentTrendReport(List<Student> students, List<Application> applications) {
        System.out.println("\n" + "=".repeat(95));
        System.out.println("                   DEPARTMENT-WISE PLACEMENT & EMPLOYMENT TREND REPORT");
        System.out.println("=".repeat(95));
        System.out.printf("| %-12s | %-16s | %-18s | %-16s | %-16s |%n",
                "Department", "Total Students", "Applications Made", "Placed Students", "Placement Rate");
        System.out.println("-".repeat(95));

        // Placed student IDs
        Set<String> placedStudentIds = applications.stream()
                .filter(a -> a.getStatus() == ApplicationStatus.SELECTED)
                .map(Application::getStudentId)
                .collect(Collectors.toSet());

        // Group students by Department
        Map<String, List<Student>> studentsByDept = students.stream()
                .collect(Collectors.groupingBy(Student::getDepartment));

        // Map applications count by Department
        Map<String, Long> appsByDept = applications.stream()
                .collect(Collectors.groupingBy(app -> {
                    return students.stream()
                            .filter(s -> s.getStudentId().equals(app.getStudentId()))
                            .map(Student::getDepartment)
                            .findFirst().orElse("UNKNOWN");
                }, Collectors.counting()));

        studentsByDept.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String dept = entry.getKey();
                    List<Student> deptStudents = entry.getValue();
                    int totalInDept = deptStudents.size();
                    long appsCount = appsByDept.getOrDefault(dept, 0L);
                    long placedCount = deptStudents.stream()
                            .filter(s -> placedStudentIds.contains(s.getStudentId()))
                            .count();
                    double placementRate = totalInDept > 0 ? (placedCount * 100.0 / totalInDept) : 0.0;

                    System.out.printf("| %-12s | %-16d | %-18d | %-16d | %12.2f%%   |%n",
                            dept, totalInDept, appsCount, placedCount, placementRate);
                });

        System.out.println("=".repeat(95));
    }
}
