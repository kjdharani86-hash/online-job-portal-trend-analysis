# SOFTWARE TESTING STRATEGIES, TECHNIQUES & TEST CASES
## Online Job Portal and Employment Trend Analysis System
### Scenario 22 | Department of Information Technology
**Velalar College of Engineering and Technology, Erode - 12**  
**Student:** Dharaneesh.K | **Reg No:** 732925ITR022 | **Class:** 25IT3A  
**Course:** 25ITT33 Software Engineering (Unit 4: Software Testing)

---

## 1. TESTING STRATEGIES & METHODOLOGY

Software testing is an empirical investigation conducted to provide stakeholders with information about the quality of the software product under test. For this project, testing was designed across four distinct testing levels and paradigms:

```
+-----------------------------------------------------------------------+
|                        SYSTEM & VALIDATION TESTING                    |
|   (End-to-End Recruitment Scenario, Multi-User Role Flows & Reporting)|
+-----------------------------------------------------------------------+
                                   ▲
+-----------------------------------------------------------------------+
|                          INTEGRATION TESTING                          |
|         (Java Service <-> JDBC Driver <-> SQLite DBMS Transactions)   |
+-----------------------------------------------------------------------+
                                   ▲
+-----------------------------------------------------------------------+
|                   UNIT & COMPONENT LEVEL TESTING                      |
|       (DAO Operations, Input Validators, Custom Exception Throwing)   |
+-----------------------------------------------------------------------+
```

---

## 2. TESTING TECHNIQUES APPLIED

### 2.1 Black-Box Testing (Functional Testing)
Black-box testing focuses on inputs and outputs without reference to internal source code:
1. **Equivalence Partitioning (EP)**:
   - *CGPA Input*: Valid partition $[0.00, 10.00]$; Invalid partitions $[< 0.00]$ and $[> 10.00]$.
   - *Phone Number*: Valid partition $[10 \text{ numeric digits}]$; Invalid partitions $[< 10 \text{ digits}]$, $[> 10 \text{ digits}]$, and $[ \text{contains non-digits} ]$.
   - *Application Status*: Valid partition $[\text{'APPLIED'}, \text{'SHORTLISTED'}, \text{'SELECTED'}, \text{'REJECTED'}]$; Invalid $[\text{any other string}]$.

2. **Boundary Value Analysis (BVA)**:
   - *CGPA Cutoffs*: Tested at boundaries: $-0.01$ (Invalid), $0.00$ (Min valid), $6.00$ (Threshold), $10.00$ (Max valid), $10.01$ (Invalid).
   - *Interview Round*: Tested at boundary $0$ (Invalid), $1$ (Min valid initial round), $3$ (Offer round).

### 2.2 White-Box Testing (Structural Testing)
White-box testing focuses on control flow graphs, basis path testing, and condition coverage:
1. **Basis Path Testing for Eligibility Logic (`isEligible` method)**:
   - *Condition 1*: `student.getCgpa() >= vacancy.getMinCgpa()`
   - *Condition 2*: `"ALL".equalsIgnoreCase(vacancy.getEligibleDepartments())`
   - *Condition 3*: `allowedList.contains(student.getDepartment())`
   - **Cyclomatic Complexity ($V(G)$)**:
     $$V(G) = P + 1 = 3 + 1 = 4 \text{ independent execution paths}$$
     - Path 1: Candidate CGPA < cutoff $\implies$ Ineligible (`IneligibleCandidateException`).
     - Path 2: Candidate CGPA $\ge$ cutoff AND allowed departments = `"ALL"` $\implies$ Eligible (`true`).
     - Path 3: Candidate CGPA $\ge$ cutoff AND candidate department in allowed list $\implies$ Eligible (`true`).
     - Path 4: Candidate CGPA $\ge$ cutoff AND candidate department NOT in allowed list $\implies$ Ineligible (`IneligibleCandidateException`).

2. **Branch & Exception Coverage**:
   - Explicitly forcing SQLite `UNIQUE` constraint violation to verify that the DAO catches database errors and the Service layer throws checked `DuplicateApplicationException`.

---

## 3. TEST CASES EXECUTION MATRIX

| Test ID | Module / Feature | Test Type | Test Description | Input Data | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|---|
| **TC-01** | Student Module | Unit (BVA) | Validate valid student registration | `ID: 732925ITR099, Name: Rithanya, Dept: IT, CGPA: 8.25` | Student registered in database with ID 732925ITR099 | Student successfully registered in DB | **PASS** |
| **TC-02** | Student Module | Black Box (EP) | Reject student registration with invalid CGPA (> 10.0) | `CGPA: 11.50` | Input validation catches error; rejects with message | `[ERROR] Invalid CGPA! Must be between 0.00 and 10.00.` | **PASS** |
| **TC-03** | Student Module | Black Box (EP) | Reject student registration with malformed email | `Email: student@@xyz` | Input validation regex catches invalid email | `[ERROR] Invalid email address format.` | **PASS** |
| **TC-04** | Vacancy Module | Unit / DAO | Query active open vacancies | Query `status = 'OPEN'` | Returns list of 12 open job vacancies with company names | 12 open vacancies returned correctly | **PASS** |
| **TC-05** | Eligibility Engine | White Box (Path 1) | Ineligible application due to low CGPA | `Student CGPA: 6.80, Vacancy Min CGPA: 7.00 (TCS Digital)` | Throws `IneligibleCandidateException` | `Ineligibility Error: Student [732925ITR015] cannot apply for Vacancy [3]. Reason: CGPA 6.80 is below cutoff 7.00` | **PASS** |
| **TC-06** | Eligibility Engine | White Box (Path 4) | Ineligible application due to mismatched Department | `Student Dept: IT, Vacancy Depts: ECE,EEE (Bosch Embedded)` | Throws `IneligibleCandidateException` | `Ineligibility Error: Student [732925ITR999] cannot apply for Vacancy [11]. Reason: Department 'IT' is not in allowed list (ECE,EEE)` | **PASS** |
| **TC-07** | Application Module | Integration | Successful valid job application submission | `Student: 732925ITR999, Vacancy ID: 1 (Zoho SDE)` | Application persisted with status `APPLIED` and generated ID | `[SUCCESS] Application registered successfully! Generated Application ID: #63` | **PASS** |
| **TC-08** | Duplicate Prevention | Integration / DBMS | Prevent duplicate application by same student for same vacancy | Re-submit: `Student: 732925ITR999, Vacancy ID: 1` | Throws `DuplicateApplicationException`, DB duplicate prevented | `[OK EXPECTED EXCEPTION CAUGHT] Duplicate Application Rejected: Student [732925ITR999] has already applied for Vacancy ID [1].` | **PASS** |
| **TC-09** | Recruiter Module | Integration | Update candidate recruitment status to SHORTLISTED | `Application ID: 63, Status: SHORTLISTED, Round: 2` | Database record updated to `SHORTLISTED` and round 2 | Status updated to `SHORTLISTED (Round 2)` | **PASS** |
| **TC-10** | Recruiter Module | Integration | Update candidate recruitment status to SELECTED | `Application ID: 63, Status: SELECTED, Round: 3` | Database record updated to `SELECTED` and round 3 | Status updated to `SELECTED (Offer Extended)` | **PASS** |
| **TC-11** | Multithreading | Unit / Thread | Verify non-blocking async notification dispatch | Trigger `applyForJob` | Background worker thread dispatches email notification without halting CLI | Notification printed asynchronously by worker thread | **PASS** |
| **TC-12** | Reporting Engine | Unit (Streams) | Generate application status distribution summary | Invoke `printSelectionCountReport` | Accurately groups 60+ applications by status and computes percentages | Formatted table output with exact counts and percentages | **PASS** |
| **TC-13** | Reporting Engine | Unit (Streams) | Generate department-wise placement trend report | Invoke `printDepartmentTrendReport` | Accurately computes unique placed students and % per dept | Table displayed with IT, CSE, ECE, EEE, MECH placement ratios | **PASS** |
| **TC-14** | Python Pipeline | Integration | Extract database records into CSV | Run `python/db_retrieval.py` | Generates `output/raw_placement_data.csv` with 62 records | 62 records extracted with all 26 attributes | **PASS** |
| **TC-15** | Python Pipeline | Unit / NumPy | Preprocess data and perform IQR outlier analysis | Run `python/data_preprocessing.py` | Computes IQR fences and generates `cleaned_placement_data.csv` | Correctly identified 7 super-dream salary outliers above upper fence | **PASS** |
| **TC-16** | Python Pipeline | System (Viz) | Generate 8 high-resolution analytical charts | Run `python/visualizations.py` | 8 `.png` charts created in `output/visualizations/` | All 8 high-resolution charts generated successfully | **PASS** |

---

## 4. TEST SUMMARY & VERIFICATION CONCLUSION
- **Total Test Cases Executed**: 16
- **Test Cases Passed**: 16 (100% Pass Rate)
- **Test Cases Failed**: 0
- **Defect Density**: 0 critical defects remaining.
- **Verification Statement**: The system successfully enforces all business constraints, handles exceptions gracefully, guarantees data persistence, and fulfills all project scenario requirements.
