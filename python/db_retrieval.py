"""
Database Retrieval Module
Course: 25ITC31 Foundations of Data Science (Unit 2: Python Foundations for Data Analysis)
Scenario 22: Online Job Portal and Employment Trend Analysis System
Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
"""

import os
import sqlite3
import csv

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DB_PATH = os.path.join(BASE_DIR, "database", "job_portal.db")
OUTPUT_DIR = os.path.join(BASE_DIR, "output")


def get_db_connection():
    """Establish and return SQLite connection."""
    if not os.path.exists(DB_PATH):
        raise FileNotFoundError(f"Database file not found at: {DB_PATH}")
    conn = sqlite3.connect(DB_PATH)
    conn.row_factory = sqlite3.Row
    return conn


def fetch_all_tables():
    """
    Fetch raw entities as lists of dict records.
    """
    conn = get_db_connection()
    tables = {}
    table_names = ["students", "companies", "vacancies", "applications"]

    for name in table_names:
        cursor = conn.cursor()
        cursor.execute(f"SELECT * FROM {name};")
        rows = [dict(row) for row in cursor.fetchall()]
        tables[name] = rows
        print(f"[+] Retrieved '{name}': {len(rows)} records")

    conn.close()
    return tables


def fetch_master_analytical_dataset():
    """
    Execute a comprehensive multi-table JOIN query to construct
    the master placement analytical dataset. Exports to raw_placement_data.csv.
    """
    conn = get_db_connection()
    cursor = conn.cursor()

    sql = """
    SELECT 
        a.application_id,
        a.apply_date,
        a.status AS application_status,
        a.interview_round,
        a.remarks,
        s.student_id,
        s.name AS student_name,
        s.department AS student_department,
        s.qualification,
        s.cgpa AS student_cgpa,
        s.skills AS student_skills,
        s.email AS student_email,
        v.vacancy_id,
        v.job_title,
        v.min_cgpa AS vacancy_min_cgpa,
        v.required_skills AS vacancy_required_skills,
        v.eligible_departments AS vacancy_eligible_depts,
        v.location AS job_location,
        v.salary_min,
        v.salary_max,
        v.openings,
        v.status AS vacancy_status,
        c.company_id,
        c.company_name,
        c.industry AS company_industry,
        c.location AS company_headquarters
    FROM applications a
    JOIN students s ON a.student_id = s.student_id
    JOIN vacancies v ON a.vacancy_id = v.vacancy_id
    JOIN companies c ON v.company_id = c.company_id
    ORDER BY a.application_id;
    """

    cursor.execute(sql)
    rows = [dict(row) for row in cursor.fetchall()]
    conn.close()

    os.makedirs(OUTPUT_DIR, exist_ok=True)
    csv_path = os.path.join(OUTPUT_DIR, "raw_placement_data.csv")

    if rows:
        fieldnames = list(rows[0].keys())
        with open(csv_path, "w", newline="", encoding="utf-8") as f:
            writer = csv.DictWriter(f, fieldnames=fieldnames)
            writer.writeheader()
            writer.writerows(rows)

    print(f"\n[✓] Master analytical dataset extracted: {len(rows)} records with {len(rows[0])} features.")
    print(f"[✓] Saved raw dataset to: {csv_path}")

    return rows


if __name__ == "__main__":
    print("=" * 60)
    print("Executing Database Retrieval for Trend Analysis...")
    print("=" * 60)
    tables = fetch_all_tables()
    master_records = fetch_master_analytical_dataset()
    print("\nDataset Preview (First 3 Records):")
    for r in master_records[:3]:
        print(f"  • App #{r['application_id']}: {r['student_name']} ({r['student_department']}) -> "
              f"{r['company_name']} [{r['job_title']}] | Status: {r['application_status']}")
    print("=" * 60)
