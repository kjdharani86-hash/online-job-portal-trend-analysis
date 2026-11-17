@echo off
REM ============================================================================
REM ONLINE JOB PORTAL AND EMPLOYMENT TREND ANALYSIS SYSTEM
REM Scenario 22 | Department of Information Technology
REM Velalar College of Engineering and Technology, Erode - 12
REM Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
REM ============================================================================

echo ============================================================================
echo   ONLINE JOB PORTAL AND EMPLOYMENT TREND ANALYSIS SYSTEM
echo   Velalar College of Engineering and Technology (Autonomous)
echo ============================================================================
echo.

REM 1. Initialize / Verify SQLite Database
if not exist "database\job_portal.db" (
    echo [*] Database not detected. Initializing database with schema and seed data...
    python database\init_db.py
)

REM 2. Compile Java Source Code
echo [*] Compiling Java source files...
if not exist "bin" mkdir bin
javac -cp "lib\*" -d "bin" src\com\jobportal\model\*.java src\com\jobportal\exception\*.java src\com\jobportal\config\*.java src\com\jobportal\dao\*.java src\com\jobportal\service\*.java src\com\jobportal\util\*.java src\com\jobportal\Main.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed! Please check errors above.
    pause
    exit /b %ERRORLEVEL%
)

echo [✓] Compilation successful!
echo.
echo [*] Launching Online Job Portal Interactive Application...
echo.

REM 3. Run Java Application
java -cp "bin;lib\*" com.jobportal.Main

pause
