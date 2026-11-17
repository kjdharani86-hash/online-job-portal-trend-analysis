package com.jobportal.exception;

/**
 * Custom Exception thrown when an entity record is not found in the database.
 * Course: 25ITT32 Java Programming (Unit 3: Exception Handling)
 * Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
 */
public class RecordNotFoundException extends Exception {
    public RecordNotFoundException(String message) {
        super(message);
    }
}
