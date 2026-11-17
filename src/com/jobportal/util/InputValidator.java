package com.jobportal.util;

import java.util.regex.Pattern;

/**
 * Utility class providing validation for user inputs.
 * Demonstrates String manipulation, regular expressions, and defensive programming.
 * Course: 25ITT32 Java Programming (Unit 4: Strings)
 * Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
 */
public class InputValidator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{10}$");

    private static final Pattern STUDENT_ID_PATTERN =
            Pattern.compile("^[0-9]{6}[A-Z]{2,4}[0-9]{3}$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidCgpa(double cgpa) {
        return cgpa >= 0.0 && cgpa <= 10.0;
    }

    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }

    public static boolean isValidStudentId(String id) {
        return isNotEmpty(id);
    }
}
