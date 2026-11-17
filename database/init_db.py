"""
Database Initialization Script for Online Job Portal System
Author: Dharaneesh.K (Reg No: 732925ITR022, Class: 25IT3A)
Institution: Velalar College of Engineering and Technology
Scenario 22: Online Job Portal and Employment Trend Analysis System
"""

import os
import sqlite3

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
DB_PATH = os.path.join(BASE_DIR, "job_portal.db")
SCHEMA_PATH = os.path.join(BASE_DIR, "schema.sql")
SEED_PATH = os.path.join(BASE_DIR, "seed_data.sql")


def init_database():
    print("=" * 60)
    print("Initializing Database for Online Job Portal System...")
    print(f"Target Database File: {DB_PATH}")
    print("=" * 60)

    # Remove existing db if clean start desired
    if os.path.exists(DB_PATH):
        try:
            os.remove(DB_PATH)
            print("[-] Existing database removed for clean initialization.")
        except Exception as e:
            print(f"[!] Warning: Could not remove existing db: {e}")

    conn = sqlite3.connect(DB_PATH)
    cursor = conn.cursor()

    # Enable foreign keys
    cursor.execute("PRAGMA foreign_keys = ON;")

    # Read and execute schema
    print("[+] Executing DDL schema...")
    with open(SCHEMA_PATH, "r", encoding="utf-8") as f:
        schema_sql = f.read()
    cursor.executescript(schema_sql)
    print("[✓] Schema tables and indexes created successfully.")

    # Read and execute seed data
    print("[+] Inserting seed data...")
    with open(SEED_PATH, "r", encoding="utf-8") as f:
        seed_sql = f.read()
    cursor.executescript(seed_sql)
    print("[✓] Seed records inserted successfully.")

    # Verify counts
    tables = ["students", "companies", "vacancies", "applications"]
    print("\n--- Database Verification Summary ---")
    for table in tables:
        cursor.execute(f"SELECT COUNT(*) FROM {table}")
        count = cursor.fetchone()[0]
        print(f"  • Table '{table}': {count} records")

    conn.commit()
    conn.close()
    print("=" * 60)
    print("[✓] Database initialization completed successfully!")
    print("=" * 60)


if __name__ == "__main__":
    init_database()
