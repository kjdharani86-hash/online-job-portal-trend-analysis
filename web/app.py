"""
Online Job Portal and Employment Trend Analysis System - Web Application
Scenario 22 | Department of Information Technology
Velalar College of Engineering and Technology, Erode - 12
Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
"""

import os
import sqlite3
from flask import Flask, render_template, request, redirect, url_for, flash, jsonify

app = Flask(__name__)
app.secret_key = "vcet_job_portal_secret_key_2026"

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DB_PATH = os.path.join(BASE_DIR, "database", "job_portal.db")


def get_db():
    conn = sqlite3.connect(DB_PATH)
    conn.row_factory = sqlite3.Row
    conn.execute("PRAGMA foreign_keys = ON;")
    return conn


@app.route("/")
def index():
    conn = get_db()
    cursor = conn.cursor()

    # Metrics
    cursor.execute("SELECT COUNT(*) FROM students;")
    total_students = cursor.fetchone()[0]

    cursor.execute("SELECT COUNT(*) FROM companies;")
    total_companies = cursor.fetchone()[0]

    cursor.execute("SELECT COUNT(*) FROM vacancies WHERE status = 'OPEN';")
    total_vacancies = cursor.fetchone()[0]

    cursor.execute("SELECT COUNT(*) FROM applications WHERE status = 'SELECTED';")
    total_selected = cursor.fetchone()[0]

    # Recent Vacancies
    cursor.execute("""
        SELECT v.*, c.company_name, c.industry
        FROM vacancies v
        JOIN companies c ON v.company_id = c.company_id
        WHERE v.status = 'OPEN'
        ORDER BY v.salary_max DESC
        LIMIT 6;
    """)
    recent_vacancies = cursor.fetchall()

    # Department placement snapshot
    cursor.execute("""
        SELECT s.department, 
               COUNT(DISTINCT s.student_id) as total_students,
               COUNT(DISTINCT CASE WHEN a.status = 'SELECTED' THEN s.student_id END) as placed_students
        FROM students s
        LEFT JOIN applications a ON s.student_id = a.student_id
        GROUP BY s.department
        ORDER BY placed_students DESC;
    """)
    dept_stats = cursor.fetchall()

    conn.close()

    return render_template("index.html",
                           total_students=total_students,
                           total_companies=total_companies,
                           total_vacancies=total_vacancies,
                           total_selected=total_selected,
                           recent_vacancies=recent_vacancies,
                           dept_stats=dept_stats)


@app.route("/vacancies")
def vacancies():
    dept_filter = request.args.get("dept", "").strip().upper()
    conn = get_db()
    cursor = conn.cursor()

    if dept_filter and dept_filter != "ALL":
        cursor.execute("""
            SELECT v.*, c.company_name, c.industry, c.location AS company_loc, c.contact_email
            FROM vacancies v
            JOIN companies c ON v.company_id = c.company_id
            WHERE v.status = 'OPEN' AND (v.eligible_departments LIKE ? OR v.eligible_departments = 'ALL')
            ORDER BY v.salary_max DESC;
        """, (f"%{dept_filter}%",))
    else:
        cursor.execute("""
            SELECT v.*, c.company_name, c.industry, c.location AS company_loc, c.contact_email
            FROM vacancies v
            JOIN companies c ON v.company_id = c.company_id
            WHERE v.status = 'OPEN'
            ORDER BY v.salary_max DESC;
        """)
    all_vacancies = cursor.fetchall()

    # Get student list for apply dropdown modal
    cursor.execute("SELECT student_id, name, department, cgpa FROM students ORDER BY name;")
    students = cursor.fetchall()

    conn.close()
    return render_template("vacancies.html", vacancies=all_vacancies, students=students, current_dept=dept_filter)


@app.route("/students")
def students():
    search = request.args.get("q", "").strip()
    conn = get_db()
    cursor = conn.cursor()

    if search:
        cursor.execute("""
            SELECT * FROM students 
            WHERE name LIKE ? OR student_id LIKE ? OR department LIKE ? OR skills LIKE ?
            ORDER BY department, student_id;
        """, (f"%{search}%", f"%{search}%", f"%{search}%", f"%{search}%"))
    else:
        cursor.execute("SELECT * FROM students ORDER BY department, student_id;")
    all_students = cursor.fetchall()
    conn.close()

    return render_template("students.html", students=all_students, search_query=search)


@app.route("/applications")
def applications():
    status_filter = request.args.get("status", "").strip().upper()
    conn = get_db()
    cursor = conn.cursor()

    if status_filter and status_filter in ["APPLIED", "SHORTLISTED", "SELECTED", "REJECTED"]:
        cursor.execute("""
            SELECT a.*, s.name AS student_name, s.department, s.cgpa,
                   v.job_title, c.company_name
            FROM applications a
            JOIN students s ON a.student_id = s.student_id
            JOIN vacancies v ON a.vacancy_id = v.vacancy_id
            JOIN companies c ON v.company_id = c.company_id
            WHERE a.status = ?
            ORDER BY a.application_id DESC;
        """, (status_filter,))
    else:
        cursor.execute("""
            SELECT a.*, s.name AS student_name, s.department, s.cgpa,
                   v.job_title, c.company_name
            FROM applications a
            JOIN students s ON a.student_id = s.student_id
            JOIN vacancies v ON a.vacancy_id = v.vacancy_id
            JOIN companies c ON v.company_id = c.company_id
            ORDER BY a.application_id DESC;
        """)
    all_apps = cursor.fetchall()
    conn.close()

    return render_template("applications.html", applications=all_apps, current_status=status_filter)


@app.route("/apply", methods=["POST"])
def apply_job():
    student_id = request.form.get("student_id", "").strip()
    vacancy_id = request.form.get("vacancy_id", "").strip()
    remarks = request.form.get("remarks", "Applied via Web Portal").strip()

    if not student_id or not vacancy_id:
        flash("Please select both a student and a vacancy.", "danger")
        return redirect(url_for("vacancies"))

    conn = get_db()
    cursor = conn.cursor()

    # 1. Check student exists
    cursor.execute("SELECT * FROM students WHERE student_id = ?;", (student_id,))
    student = cursor.fetchone()
    if not student:
        flash(f"Student ID '{student_id}' does not exist.", "danger")
        conn.close()
        return redirect(url_for("vacancies"))

    # 2. Check vacancy exists and is OPEN
    cursor.execute("SELECT * FROM vacancies WHERE vacancy_id = ?;", (vacancy_id,))
    vacancy = cursor.fetchone()
    if not vacancy:
        flash(f"Vacancy ID '{vacancy_id}' does not exist.", "danger")
        conn.close()
        return redirect(url_for("vacancies"))

    if vacancy["status"] != "OPEN":
        flash("This vacancy is currently CLOSED.", "danger")
        conn.close()
        return redirect(url_for("vacancies"))

    # 3. Check duplicate application (Functional Requirement 4)
    cursor.execute("SELECT COUNT(*) FROM applications WHERE student_id = ? AND vacancy_id = ?;",
                   (student_id, vacancy_id))
    if cursor.fetchone()[0] > 0:
        flash(f"Duplicate Application Rejected! Student [{student_id}] has already applied for this vacancy.", "warning")
        conn.close()
        return redirect(url_for("vacancies"))

    # 4. Check eligibility criteria (Functional Requirement 3)
    if student["cgpa"] < vacancy["min_cgpa"]:
        flash(f"Ineligibility Error: Student CGPA ({student['cgpa']:.2f}) is below the required cutoff ({vacancy['min_cgpa']:.2f}).", "danger")
        conn.close()
        return redirect(url_for("vacancies"))

    allowed_depts = [d.strip().upper() for d in vacancy["eligible_departments"].split(",")]
    if "ALL" not in allowed_depts and student["department"].upper() not in allowed_depts:
        flash(f"Ineligibility Error: Student Department '{student['department']}' is not eligible for this role ({vacancy['eligible_departments']}).", "danger")
        conn.close()
        return redirect(url_for("vacancies"))

    # 5. Insert application
    try:
        cursor.execute("""
            INSERT INTO applications (student_id, vacancy_id, status, interview_round, remarks)
            VALUES (?, ?, 'APPLIED', 1, ?);
        """, (student_id, vacancy_id, remarks))
        conn.commit()
        app_id = cursor.lastrowid
        flash(f"Application #{app_id} submitted successfully for {vacancy['job_title']}!", "success")
    except sqlite3.IntegrityError as e:
        flash(f"Database Integrity Error: {e}", "danger")
    finally:
        conn.close()

    return redirect(url_for("applications"))


@app.route("/analytics")
def analytics():
    charts = [
        {
            "id": "01",
            "title": "Recruitment Status Funnel Distribution",
            "filename": "01_application_status_distribution.png",
            "type": "Donut Chart (Unit 5)",
            "desc": "Breakdown of candidate recruitment stages: Selected (49.2%), Shortlisted (33.3%), Applied (9.5%), Rejected (7.9%)."
        },
        {
            "id": "02",
            "title": "Department Applications vs Selections",
            "filename": "02_applications_per_department.png",
            "type": "Grouped Bar Chart (Unit 5)",
            "desc": "Comparison of application volume against final job selections across IT, CSE, ECE, EEE, and MECH."
        },
        {
            "id": "03",
            "title": "Salary Distribution by Job Designation",
            "filename": "03_salary_distribution_by_job_role.png",
            "type": "Horizontal Box Plot & IQR (Unit 5)",
            "desc": "Median compensation packages and interquartile ranges across product, cloud, and core engineering profiles."
        },
        {
            "id": "04",
            "title": "Hiring Volume by Corporate Recruiter",
            "filename": "04_top_recruiting_companies.png",
            "type": "Clustered Bar Chart (Unit 5)",
            "desc": "Offer distribution across top partners including Zoho, TCS, Infosys, AWS, and Robert Bosch."
        },
        {
            "id": "05",
            "title": "Academic CGPA vs Selection Correlation",
            "filename": "05_cgpa_vs_selection_correlation.png",
            "type": "Histogram & Distribution (Unit 5)",
            "desc": "Statistical proof demonstrating selected candidates achieved an 8.08 mean CGPA vs 7.56 for non-selected applicants."
        },
        {
            "id": "06",
            "title": "Top In-Demand Technical Skills Ranking",
            "filename": "06_skills_demand_frequency.png",
            "type": "Ranking Bar Chart (Unit 5)",
            "desc": "Frequency analysis of required skills across campus postings, led by SQL, Python, Java, Data Structures, and AWS."
        },
        {
            "id": "07",
            "title": "Department Placement Rates vs Benchmark",
            "filename": "07_placement_rate_by_department.png",
            "type": "Target Comparison Bar Chart (Unit 5)",
            "desc": "All 5 departments achieved or surpassed the 80% institutional placement target (EEE: 100%, IT: 88.9%, CSE: 85.7%)."
        },
        {
            "id": "08",
            "title": "Multivariate Pearson Correlation Heatmap",
            "filename": "08_correlation_heatmap.png",
            "type": "Correlation Heat Map (Unit 5)",
            "desc": "Quantitative correlation analysis linking CGPA, salary brackets, and multi-round interview progression."
        }
    ]
    return render_template("analytics.html", charts=charts)


if __name__ == "__main__":
    print("=" * 60)
    print("Online Job Portal Web Dashboard Running...")
    print("Open your browser at: http://127.0.0.1:5000")
    print("=" * 60)
    app.run(host="127.0.0.1", port=5000, debug=False)
