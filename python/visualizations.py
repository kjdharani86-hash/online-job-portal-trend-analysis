"""
Data Visualization Module
Course: 25ITC31 Foundations of Data Science (Unit 5: EDA and Visualization Techniques)
Scenario 22: Online Job Portal and Employment Trend Analysis System
Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
"""

import os
import csv
from collections import Counter, defaultdict
import numpy as np
import matplotlib.pyplot as plt

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
OUTPUT_DIR = os.path.join(BASE_DIR, "output", "visualizations")
CLEANED_CSV = os.path.join(BASE_DIR, "output", "cleaned_placement_data.csv")

# Set standard publication styling
plt.rcParams['font.sans-serif'] = 'DejaVu Sans'
plt.rcParams['axes.edgecolor'] = '#cccccc'
plt.rcParams['axes.linewidth'] = 0.8
plt.rcParams['grid.color'] = '#e0e0e0'
plt.rcParams['grid.linestyle'] = '--'
plt.rcParams['grid.alpha'] = 0.7


def load_cleaned_data():
    """Load preprocessed records from CSV."""
    if not os.path.exists(CLEANED_CSV):
        from data_preprocessing import preprocess_placement_data
        return preprocess_placement_data()

    records = []
    with open(CLEANED_CSV, "r", encoding="utf-8") as f:
        reader = csv.DictReader(f)
        for row in reader:
            row["student_cgpa"] = float(row["student_cgpa"])
            row["vacancy_min_cgpa"] = float(row["vacancy_min_cgpa"])
            row["salary_avg_lpa"] = float(row["salary_avg_lpa"])
            row["salary_max_lpa"] = float(row["salary_max_lpa"])
            row["salary_min"] = int(row["salary_min"])
            row["salary_max"] = int(row["salary_max"])
            row["is_selected"] = int(row["is_selected"])
            row["interview_round"] = int(row["interview_round"])
            row["skill_count"] = int(row["skill_count"])
            records.append(row)
    return records


def plot_01_status_distribution(records):
    """Chart 1: Pie / Donut Chart of Application Status (Unit 5: Pie Charts)."""
    counts = Counter(r["application_status"] for r in records)
    labels = ["SELECTED", "SHORTLISTED", "APPLIED", "REJECTED"]
    sizes = [counts.get(k, 0) for k in labels]
    colors = ["#2ecc71", "#3498db", "#f39c12", "#e74c3c"]
    explode = (0.05, 0.02, 0.02, 0.05)

    fig, ax = plt.subplots(figsize=(8, 7), dpi=300)
    wedges, texts, autotexts = ax.pie(
        sizes, explode=explode, labels=labels, autopct='%1.1f%%',
        startangle=140, colors=colors, pctdistance=0.75,
        textprops=dict(color="#333333", fontsize=11, weight="bold")
    )
    for at in autotexts:
        at.set_color('white')
        at.set_weight('bold')

    # Draw center circle for Donut effect
    centre_circle = plt.Circle((0, 0), 0.55, fc='white')
    fig.gca().add_artist(centre_circle)

    ax.set_title("Recruitment Funnel: Application Status Distribution\nScenario 22 - Online Job Portal",
                 fontsize=14, weight="bold", pad=20, color="#1a252f")
    plt.tight_layout()
    out_path = os.path.join(OUTPUT_DIR, "01_application_status_distribution.png")
    plt.savefig(out_path)
    plt.close()
    print(f"[✓ Generated] {out_path}")


def plot_02_department_applications(records):
    """Chart 2: Grouped Bar Chart comparing Total Applicants vs Selected (Unit 5: Bar Charts)."""
    depts = sorted(list(set(r["student_department"] for r in records)))
    total_apps = [sum(1 for r in records if r["student_department"] == d) for d in depts]
    selected_apps = [sum(1 for r in records if r["student_department"] == d and r["is_selected"] == 1) for d in depts]

    x = np.arange(len(depts))
    width = 0.35

    fig, ax = plt.subplots(figsize=(9, 6), dpi=300)
    rects1 = ax.bar(x - width/2, total_apps, width, label='Total Applications', color='#34495e', edgecolor='none')
    rects2 = ax.bar(x + width/2, selected_apps, width, label='Selected / Placed', color='#27ae60', edgecolor='none')

    ax.set_ylabel('Number of Applications', fontsize=12, weight='bold')
    ax.set_title('Department-Wise Applications vs Selection Counts\nOnline Job Portal & Employment Trend Analysis',
                 fontsize=14, weight='bold', pad=15)
    ax.set_xticks(x)
    ax.set_xticklabels(depts, fontsize=11, weight='bold')
    ax.legend(frameon=True, facecolor='#f8f9fa')
    ax.grid(axis='y')

    # Value labels
    for rect in rects1:
        h = rect.get_height()
        ax.annotate(f'{h}', xy=(rect.get_x() + rect.get_width() / 2, h),
                    xytext=(0, 3), textcoords="offset points", ha='center', va='bottom', fontsize=10, weight='bold')
    for rect in rects2:
        h = rect.get_height()
        ax.annotate(f'{h}', xy=(rect.get_x() + rect.get_width() / 2, h),
                    xytext=(0, 3), textcoords="offset points", ha='center', va='bottom', fontsize=10, weight='bold', color='#1e8449')

    plt.tight_layout()
    out_path = os.path.join(OUTPUT_DIR, "02_applications_per_department.png")
    plt.savefig(out_path)
    plt.close()
    print(f"[✓ Generated] {out_path}")


def plot_03_salary_distribution_by_role(records):
    """Chart 3: Horizontal Box Plot / Bar Chart of Salaries by Job Title (Unit 5: Box Plots)."""
    # Group salaries by job title
    role_salaries = defaultdict(list)
    for r in records:
        role_salaries[r["job_title"]].append(r["salary_avg_lpa"])

    sorted_roles = sorted(role_salaries.keys(), key=lambda k: np.median(role_salaries[k]))
    sal_data = [role_salaries[r] for r in sorted_roles]

    fig, ax = plt.subplots(figsize=(10, 7), dpi=300)
    bp = ax.boxplot(sal_data, orientation='horizontal', patch_artist=True,
                    boxprops=dict(facecolor='#85c1e9', color='#2980b9'),
                    whiskerprops=dict(color='#2980b9'),
                    capprops=dict(color='#2980b9'),
                    medianprops=dict(color='#c0392b', linewidth=2))

    ax.set_yticklabels(sorted_roles, fontsize=10)
    ax.set_xlabel('Salary Package Range (LPA - Lakhs Per Annum)', fontsize=12, weight='bold')
    ax.set_title('Salary Distribution Across Job Roles (Median & IQR)\nEmployment Trend Analysis System',
                 fontsize=14, weight='bold', pad=15)
    ax.grid(axis='x')

    plt.tight_layout()
    out_path = os.path.join(OUTPUT_DIR, "03_salary_distribution_by_job_role.png")
    plt.savefig(out_path)
    plt.close()
    print(f"[✓ Generated] {out_path}")


def plot_04_top_recruiting_companies(records):
    """Chart 4: Corporate Recruitment Selections (Unit 5: Bar Charts)."""
    company_apps = Counter(r["company_name"] for r in records)
    company_selected = Counter(r["company_name"] for r in records if r["is_selected"] == 1)

    sorted_companies = sorted(company_apps.keys(), key=lambda c: company_selected.get(c, 0), reverse=True)
    selections = [company_selected.get(c, 0) for c in sorted_companies]
    totals = [company_apps.get(c, 0) for c in sorted_companies]

    fig, ax = plt.subplots(figsize=(10, 6), dpi=300)
    y_pos = np.arange(len(sorted_companies))

    ax.barh(y_pos - 0.15, totals, height=0.3, label='Total Applicants', color='#95a5a6')
    bars = ax.barh(y_pos + 0.15, selections, height=0.3, label='Hired / Offers Extended', color='#2980b9')

    ax.set_yticks(y_pos)
    ax.set_yticklabels(sorted_companies, fontsize=10, weight='bold')
    ax.invert_yaxis()
    ax.set_xlabel('Candidate Count', fontsize=12, weight='bold')
    ax.set_title('Recruitment & Hiring Volume by Corporate Employer\nPlacement Cell Analytics',
                 fontsize=14, weight='bold', pad=15)
    ax.legend(loc='lower right', frameon=True)
    ax.grid(axis='x')

    for bar in bars:
        w = bar.get_width()
        ax.annotate(f'{int(w)}', xy=(w, bar.get_y() + bar.get_height() / 2),
                    xytext=(5, 0), textcoords="offset points", ha='left', va='center', fontsize=10, weight='bold', color='#1b4f72')

    plt.tight_layout()
    out_path = os.path.join(OUTPUT_DIR, "04_top_recruiting_companies.png")
    plt.savefig(out_path)
    plt.close()
    print(f"[✓ Generated] {out_path}")


def plot_05_cgpa_vs_selection(records):
    """Chart 5: CGPA Histogram & Distribution: Selected vs Non-Selected (Unit 5: Histograms)."""
    selected_cgpa = [r["student_cgpa"] for r in records if r["is_selected"] == 1]
    other_cgpa = [r["student_cgpa"] for r in records if r["is_selected"] == 0]

    fig, ax = plt.subplots(figsize=(9, 6), dpi=300)
    bins = np.linspace(6.0, 9.5, 12)

    ax.hist(other_cgpa, bins=bins, alpha=0.6, label='Non-Selected Candidates', color='#e74c3c', edgecolor='black')
    ax.hist(selected_cgpa, bins=bins, alpha=0.7, label='Selected Candidates', color='#27ae60', edgecolor='black')

    ax.axvline(np.mean(selected_cgpa), color='#1e8449', linestyle='dashed', linewidth=2,
               label=f'Selected Mean CGPA ({np.mean(selected_cgpa):.2f})')
    ax.axvline(np.mean(other_cgpa), color='#922b21', linestyle='dashed', linewidth=2,
               label=f'Non-Selected Mean CGPA ({np.mean(other_cgpa):.2f})')

    ax.set_xlabel('Academic CGPA (Out of 10.0)', fontsize=12, weight='bold')
    ax.set_ylabel('Number of Candidates', fontsize=12, weight='bold')
    ax.set_title('Academic CGPA Distribution: Selected vs Non-Selected\nStatistical Impact of Academic Scores on Recruitment',
                 fontsize=14, weight='bold', pad=15)
    ax.legend(frameon=True, facecolor='#fdfefe')
    ax.grid(axis='y')

    plt.tight_layout()
    out_path = os.path.join(OUTPUT_DIR, "05_cgpa_vs_selection_correlation.png")
    plt.savefig(out_path)
    plt.close()
    print(f"[✓ Generated] {out_path}")


def plot_06_skills_demand_frequency(records):
    """Chart 6: Technical Skills Demand Frequency in Market (Unit 5: Bar Charts)."""
    all_skills = []
    for r in records:
        skills = [s.strip() for s in r["vacancy_required_skills"].split(",") if s.strip()]
        all_skills.extend(skills)

    skill_counts = Counter(all_skills).most_common(10)
    skills, freqs = zip(*skill_counts)

    fig, ax = plt.subplots(figsize=(9, 6), dpi=300)
    y_pos = np.arange(len(skills))
    bars = ax.barh(y_pos, freqs, color='#8e44ad', edgecolor='none')

    ax.set_yticks(y_pos)
    ax.set_yticklabels(skills, fontsize=11, weight='bold')
    ax.invert_yaxis()
    ax.set_xlabel('Vacancy Requirement Frequency', fontsize=12, weight='bold')
    ax.set_title('Top 10 In-Demand Technical Skills Required by Employers\nCurriculum & Industry Alignment Insights',
                 fontsize=14, weight='bold', pad=15)
    ax.grid(axis='x')

    for bar in bars:
        w = bar.get_width()
        ax.annotate(f'{int(w)}', xy=(w, bar.get_y() + bar.get_height() / 2),
                    xytext=(5, 0), textcoords="offset points", ha='left', va='center', fontsize=10, weight='bold', color='#512e5f')

    plt.tight_layout()
    out_path = os.path.join(OUTPUT_DIR, "06_skills_demand_frequency.png")
    plt.savefig(out_path)
    plt.close()
    print(f"[✓ Generated] {out_path}")


def plot_07_placement_rate_by_department(records):
    """Chart 7: Department-wise Placement Percentage against Institutional Benchmark (Unit 5: Bar Charts)."""
    # Count unique placed students per dept
    dept_all = defaultdict(set)
    dept_placed = defaultdict(set)
    for r in records:
        dept = r["student_department"]
        sid = r["student_id"]
        dept_all[dept].add(sid)
        if r["is_selected"] == 1:
            dept_placed[dept].add(sid)

    depts = sorted(list(dept_all.keys()))
    rates = [(len(dept_placed[d]) / len(dept_all[d])) * 100.0 for d in depts]

    fig, ax = plt.subplots(figsize=(9, 6), dpi=300)
    bars = ax.bar(depts, rates, color=['#2980b9', '#16a085', '#27ae60', '#f39c12', '#d35400'], edgecolor='none', width=0.55)

    # Benchmark line at 80%
    ax.axhline(80, color='#c0392b', linestyle='--', linewidth=1.8, label='Institutional Placement Target (80%)')

    ax.set_ylabel('Placement Success Rate (%)', fontsize=12, weight='bold')
    ax.set_ylim(0, 115)
    ax.set_title('Department-Wise Final Placement Rates (%)\nVelalar College of Engineering and Technology',
                 fontsize=14, weight='bold', pad=15)
    ax.legend(loc='upper right', frameon=True)
    ax.grid(axis='y')

    for bar in bars:
        h = bar.get_height()
        ax.annotate(f'{h:.1f}%', xy=(bar.get_x() + bar.get_width() / 2, h),
                    xytext=(0, 4), textcoords="offset points", ha='center', va='bottom', fontsize=11, weight='bold')

    plt.tight_layout()
    out_path = os.path.join(OUTPUT_DIR, "07_placement_rate_by_department.png")
    plt.savefig(out_path)
    plt.close()
    print(f"[✓ Generated] {out_path}")


def plot_08_correlation_heatmap(records):
    """Chart 8: Feature Correlation Matrix Heatmap (Unit 5: Heat Maps)."""
    features = ["student_cgpa", "vacancy_min_cgpa", "salary_avg_lpa", "interview_round", "skill_count", "is_selected"]
    labels = ["Student CGPA", "Min CGPA Cutoff", "Salary (LPA)", "Interview Round", "Skill Count", "Selected Flag"]

    # Build matrix
    data = np.array([[r[f] for f in features] for r in records])
    corr = np.corrcoef(data, rowvar=False)

    fig, ax = plt.subplots(figsize=(8, 7), dpi=300)
    im = ax.imshow(corr, cmap="coolwarm", vmin=-1, vmax=1)
    cbar = ax.figure.colorbar(im, ax=ax)
    cbar.ax.set_ylabel("Pearson Correlation Coefficient", rotation=-90, va="bottom", fontsize=11, weight='bold')

    ax.set_xticks(np.arange(len(labels)))
    ax.set_yticks(np.arange(len(labels)))
    ax.set_xticklabels(labels, rotation=35, ha="right", fontsize=10, weight='bold')
    ax.set_yticklabels(labels, fontsize=10, weight='bold')

    # Annotate values inside cells
    for i in range(len(labels)):
        for j in range(len(labels)):
            val = corr[i, j]
            color = "white" if abs(val) > 0.5 else "black"
            ax.text(j, i, f"{val:.2f}", ha="center", va="center", color=color, fontsize=10, weight="bold")

    ax.set_title("Correlation Analysis Matrix (Heat Map)\nFactors Influencing Placement Outcomes",
                 fontsize=14, weight="bold", pad=20)
    plt.tight_layout()
    out_path = os.path.join(OUTPUT_DIR, "08_correlation_heatmap.png")
    plt.savefig(out_path)
    plt.close()
    print(f"[✓ Generated] {out_path}")


def generate_all_visualizations():
    """Generates the full suite of data visualization charts."""
    print("=" * 65)
    print("Generating High-Resolution Data Visualizations (Matplotlib & Seaborn Style)...")
    print("=" * 65)
    os.makedirs(OUTPUT_DIR, exist_ok=True)
    records = load_cleaned_data()
    print(f"[i] Dataset loaded: {len(records)} records.")

    plot_01_status_distribution(records)
    plot_02_department_applications(records)
    plot_03_salary_distribution_by_role(records)
    plot_04_top_recruiting_companies(records)
    plot_05_cgpa_vs_selection(records)
    plot_06_skills_demand_frequency(records)
    plot_07_placement_rate_by_department(records)
    plot_08_correlation_heatmap(records)

    print("=" * 65)
    print(f"[✓ COMPLETE] All 8 visualization charts successfully saved to:\n    {OUTPUT_DIR}")
    print("=" * 65)


if __name__ == "__main__":
    generate_all_visualizations()
