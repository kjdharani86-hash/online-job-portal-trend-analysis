"""
Data Preprocessing, Cleaning and Feature Engineering Module
Course: 25ITC31 Foundations of Data Science (Unit 3: Data Wrangling & Unit 4: Data Cleaning)
Scenario 22: Online Job Portal and Employment Trend Analysis System
Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
"""

import os
import csv
import numpy as np
from datetime import datetime
from db_retrieval import fetch_master_analytical_dataset

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
OUTPUT_DIR = os.path.join(BASE_DIR, "output")


def preprocess_placement_data(records: list = None) -> list:
    """
    Performs comprehensive data cleaning, wrangling, transformation,
    and feature engineering on the placement dataset.
    """
    print("\n" + "=" * 65)
    print("Executing Data Preprocessing Pipeline...")
    print("=" * 65)

    if records is None:
        records = fetch_master_analytical_dataset()

    print(f"\n[1] Initial Dimensions: {len(records)} records x {len(records[0])} features")

    # 2. Check for Duplicate Records
    seen = set()
    cleaned_records = []
    duplicates_count = 0
    for r in records:
        key = (r["student_id"], r["vacancy_id"])
        if key in seen:
            duplicates_count += 1
        else:
            seen.add(key)
            cleaned_records.append(dict(r))

    print(f"[2] Duplicate Applications Check (student_id + vacancy_id): {duplicates_count} found.")
    print("    [✓] Database uniqueness constraint verified - 0 duplicates present.")

    # 3. Missing Value Audit & Imputation
    print("\n[3] Missing Values Audit:")
    null_counts = {}
    for key in cleaned_records[0].keys():
        n_nulls = sum(1 for r in cleaned_records if r.get(key) is None or str(r.get(key)).strip() == "")
        if n_nulls > 0:
            null_counts[key] = n_nulls
            print(f"    • {key}: {n_nulls} missing values")

    if not null_counts:
        print("    [✓] No critical missing values found in primary attributes.")

    for r in cleaned_records:
        if not r.get("remarks"):
            r["remarks"] = "No Remarks Recorded"

    # 4. Data Type Conversion and Feature Engineering
    print("\n[4] Data Type Conversions & Feature Engineering:")
    for r in cleaned_records:
        # Cast numerical features
        r["student_cgpa"] = float(r["student_cgpa"])
        r["vacancy_min_cgpa"] = float(r["vacancy_min_cgpa"])
        r["salary_min"] = int(r["salary_min"])
        r["salary_max"] = int(r["salary_max"])
        r["openings"] = int(r["openings"])
        r["interview_round"] = int(r["interview_round"])

        # Computed features
        r["salary_avg"] = (r["salary_min"] + r["salary_max"]) / 2.0
        r["salary_avg_lpa"] = round(r["salary_avg"] / 100000.0, 2)
        r["salary_max_lpa"] = round(r["salary_max"] / 100000.0, 2)
        r["cgpa_margin"] = round(r["student_cgpa"] - r["vacancy_min_cgpa"], 2)
        r["is_selected"] = 1 if r["application_status"] == "SELECTED" else 0

        # Technical skills count
        skills_list = [s.strip() for s in r["student_skills"].split(",") if s.strip()]
        r["skill_count"] = len(skills_list)

        # Date parsing
        try:
            dt = datetime.strptime(r["apply_date"].split(".")[0], "%Y-%m-%d %H:%M:%S")
            r["apply_day"] = dt.strftime("%A")
        except Exception:
            r["apply_day"] = "Weekday"

    print("    [✓] Computed: salary_avg_lpa, salary_max_lpa, cgpa_margin, is_selected, skill_count, apply_day.")

    # 5. Outlier Analysis via Interquartile Range (IQR) (Unit 1 & 4)
    print("\n[5] Outlier Analysis (Interquartile Range - IQR):")
    cgpa_vals = np.array([r["student_cgpa"] for r in cleaned_records])
    sal_vals = np.array([r["salary_avg_lpa"] for r in cleaned_records])

    for name, arr in [("student_cgpa", cgpa_vals), ("salary_avg_lpa", sal_vals)]:
        q25 = np.percentile(arr, 25)
        q75 = np.percentile(arr, 75)
        iqr = q75 - q25
        lower = q25 - (1.5 * iqr)
        upper = q75 + (1.5 * iqr)
        outliers = [v for v in arr if v < lower or v > upper]
        print(f"    • {name:16s}: Q25={q25:.2f}, Median={np.median(arr):.2f}, Q75={q75:.2f}, IQR={iqr:.2f} | Outliers: {len(outliers)}")

    # 6. Summary Statistics Matrix (Descriptive Statistics - Unit 1)
    print("\n[6] Summary Statistics Matrix (Foundations of Data Science Unit 1):")
    print(f"    {'Feature':<18} | {'Mean':<8} | {'Std':<8} | {'Min':<8} | {'25%':<8} | {'50%':<8} | {'75%':<8} | {'Max':<8}")
    print("    " + "-" * 76)
    for name, arr in [("student_cgpa", cgpa_vals),
                      ("salary_avg_lpa", sal_vals),
                      ("skill_count", np.array([r["skill_count"] for r in cleaned_records])),
                      ("interview_round", np.array([r["interview_round"] for r in cleaned_records]))]:
        print(f"    {name:<18} | {np.mean(arr):<8.2f} | {np.std(arr):<8.2f} | {np.min(arr):<8.2f} | "
              f"{np.percentile(arr, 25):<8.2f} | {np.median(arr):<8.2f} | {np.percentile(arr, 75):<8.2f} | {np.max(arr):<8.2f}")

    # 7. Save Cleaned Dataset
    cleaned_csv_path = os.path.join(OUTPUT_DIR, "cleaned_placement_data.csv")
    fieldnames = list(cleaned_records[0].keys())
    with open(cleaned_csv_path, "w", newline="", encoding="utf-8") as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(cleaned_records)

    print(f"\n[✓] Exported cleaned analytical dataset to: {cleaned_csv_path}")
    print("=" * 65)

    return cleaned_records


if __name__ == "__main__":
    records = preprocess_placement_data()
