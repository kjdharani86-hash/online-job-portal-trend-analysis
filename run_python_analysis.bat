@echo off
REM ============================================================================
REM PYTHON DATA SCIENCE PIPELINE & VISUALIZATION GENERATOR
REM Course: 25ITC31 Foundations of Data Science
REM Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
REM ============================================================================

echo ============================================================================
echo   EXECUTING PYTHON DATA EXTRACTION, PREPROCESSING ^& VISUALIZATIONS
echo ============================================================================
echo.

REM 1. Database Retrieval
echo [1/3] Extracting relational dataset from SQLite database...
python python\db_retrieval.py
echo.

REM 2. Data Preprocessing & Feature Engineering
echo [2/3] Performing data wrangling, cleaning and IQR outlier detection...
python python\data_preprocessing.py
echo.

REM 3. Generate Visualizations
echo [3/3] Generating publication-quality charts...
python python\visualizations.py
echo.

echo ============================================================================
echo [✓] SUCCESS: All 8 visualization charts generated in output\visualizations\
echo ============================================================================
pause
