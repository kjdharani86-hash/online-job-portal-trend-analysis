package com.jobportal.config;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton Database Connection Manager for SQLite DBMS.
 * Course: 25ITT32 Java Programming (Unit 4: Database Connection)
 * Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
 */
public class DatabaseConnection {
    private static Connection connection = null;
    private static final String RELATIVE_DB_PATH = "database/job_portal.db";

    private DatabaseConnection() {}

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Ensure SQLite JDBC driver is present
                Class.forName("org.sqlite.JDBC");

                // Resolve path relative to current working directory
                File dbFile = new File(RELATIVE_DB_PATH);
                String dbUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();

                connection = DriverManager.getConnection(dbUrl);

                // Enforce foreign key constraints
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON;");
                }
            } catch (ClassNotFoundException e) {
                System.err.println("[ERROR] SQLite JDBC Driver not found in classpath: " + e.getMessage());
                throw new SQLException("Driver class org.sqlite.JDBC not found.", e);
            }
        }
        return connection;
    }

    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("[ERROR] Error closing database connection: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }
}
