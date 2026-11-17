@echo off
REM ============================================================================
REM ONLINE JOB PORTAL AND EMPLOYMENT TREND ANALYSIS SYSTEM - WEB SERVER
REM Scenario 22 | Department of Information Technology
REM Velalar College of Engineering and Technology, Erode - 12
REM Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
REM ============================================================================

echo ============================================================================
echo   STARTING ONLINE JOB PORTAL ^& TREND ANALYSIS WEB DASHBOARD
echo   Velalar College of Engineering and Technology (Autonomous)
echo ============================================================================
echo.

REM Verify database exists
if not exist "database\job_portal.db" (
    echo [*] Initializing SQLite database...
    python database\init_db.py
)

echo [*] Launching Flask Web Server at http://127.0.0.1:5000...
echo [*] Opening default web browser...
start http://127.0.0.1:5000

python web\app.py

pause
