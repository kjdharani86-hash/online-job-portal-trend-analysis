-- ============================================================================
-- ONLINE JOB PORTAL AND EMPLOYMENT TREND ANALYSIS SYSTEM
-- Scenario 22 | Seed Data (Sample Dataset for Testing & Data Science EDA)
-- Student: Dharaneesh.K | Reg No: 732925ITR022 | Class: 25IT3A
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 1. COMPANIES SEED DATA
-- ----------------------------------------------------------------------------
INSERT INTO companies (company_name, industry, location, contact_email, website) VALUES
('Zoho Corporation', 'Product & SaaS', 'Chennai', 'careers@zohocorp.com', 'https://www.zohocorp.com'),
('Tata Consultancy Services', 'IT Services', 'Chennai', 'campus.tcs@tcs.com', 'https://www.tcs.com'),
('Amazon Web Services', 'Cloud & E-Commerce', 'Bangalore', 'aws-recruitment@amazon.com', 'https://aws.amazon.com'),
('Infosys Limited', 'IT Consulting', 'Bangalore', 'freshers@infosys.com', 'https://www.infosys.com'),
('Cognizant Technology Solutions', 'Digital Solutions', 'Coimbatore', 'campus@cognizant.com', 'https://www.cognizant.com'),
('Kaar Technologies', 'Enterprise SAP Consulting', 'Chennai', 'hr@kaartech.com', 'https://www.kaartech.com'),
('Robert Bosch Engineering', 'Automotive Embedded Systems', 'Coimbatore', 'bosch.campus@bosch.com', 'https://www.bosch.com'),
('Accenture Solutions', 'Management & IT Consulting', 'Hyderabad', 'india.recruiting@accenture.com', 'https://www.accenture.com');

-- ----------------------------------------------------------------------------
-- 2. VACANCIES SEED DATA
-- ----------------------------------------------------------------------------
INSERT INTO vacancies (company_id, job_title, min_cgpa, required_skills, eligible_departments, location, salary_min, salary_max, openings, status) VALUES
(1, 'Software Development Engineer (Java)', 7.5, 'Java, Spring Boot, SQL, Data Structures', 'IT,CSE', 'Chennai', 600000, 950000, 15, 'OPEN'),
(1, 'Full Stack Web Developer', 7.0, 'JavaScript, React, Node.js, HTML, CSS', 'IT,CSE,ECE', 'Chennai', 550000, 850000, 10, 'OPEN'),
(2, 'Systems Engineer (Digital)', 7.0, 'Python, Java, Cloud Basics, SQL', 'ALL', 'Chennai', 400000, 700000, 40, 'OPEN'),
(2, 'Ninja Software Developer', 8.0, 'Data Structures, Algorithms, C++, Java, DBMS', 'IT,CSE', 'Chennai', 700000, 900000, 12, 'OPEN'),
(3, 'Cloud Support Associate', 7.5, 'Linux, Networking, Python, AWS Fundamentals', 'IT,CSE,ECE', 'Bangalore', 1200000, 1600000, 8, 'OPEN'),
(3, 'Data Analyst Intern & FTE', 7.8, 'Python, SQL, Pandas, PowerBI, Statistics', 'IT,CSE', 'Bangalore', 1000000, 1400000, 6, 'OPEN'),
(4, 'Specialist Programmer', 7.8, 'Algorithms, Python, Java, Microservices', 'IT,CSE', 'Bangalore', 800000, 950000, 15, 'OPEN'),
(4, 'Operations Executive', 6.0, 'Communication, Basic IT, MS Excel, SQL', 'ALL', 'Coimbatore', 320000, 420000, 25, 'OPEN'),
(5, 'Programmer Analyst Trainee', 6.5, 'Java, SQL, Agile, Web Development', 'IT,CSE,ECE,EEE', 'Coimbatore', 450000, 650000, 30, 'OPEN'),
(6, 'Associate SAP Consultant', 7.2, 'ABAP, ERP Concepts, SQL, Java', 'IT,CSE,ECE', 'Chennai', 650000, 850000, 10, 'OPEN'),
(7, 'Embedded Software Engineer', 7.5, 'Embedded C, Microcontrollers, RTOS, IoT', 'ECE,EEE', 'Coimbatore', 550000, 800000, 12, 'OPEN'),
(8, 'Associate Software Engineer', 6.8, 'Full Stack, Cloud, Python, DevOps', 'ALL', 'Hyderabad', 500000, 750000, 35, 'OPEN');

-- ----------------------------------------------------------------------------
-- 3. STUDENTS SEED DATA
-- ----------------------------------------------------------------------------
INSERT INTO students (student_id, name, department, qualification, cgpa, skills, email, phone) VALUES
('732925ITR022', 'Dharaneesh K', 'IT', 'B.Tech IT', 8.92, 'Java, Python, SQL, React, Spring Boot, Git', 'dharaneesh.it22@vcet.ac.in', '9876543210'),
('732925ITR001', 'Abinaya S', 'IT', 'B.Tech IT', 8.45, 'Java, SQL, HTML, CSS, JavaScript', 'abinaya.it01@vcet.ac.in', '9876543211'),
('732925ITR005', 'Balaji R', 'IT', 'B.Tech IT', 7.60, 'Python, Django, SQL, Data Structures', 'balaji.it05@vcet.ac.in', '9876543212'),
('732925ITR015', 'Chandru M', 'IT', 'B.Tech IT', 6.80, 'Java, C++, SQL, Networking Basics', 'chandru.it15@vcet.ac.in', '9876543213'),
('732925ITR035', 'Gokulnath P', 'IT', 'B.Tech IT', 7.95, 'Python, Data Analysis, SQL, PowerBI, Pandas', 'gokul.it35@vcet.ac.in', '9876543214'),
('732925ITR050', 'Harini K', 'IT', 'B.Tech IT', 8.70, 'Java, Spring Boot, Cloud, Microservices, SQL', 'harini.it50@vcet.ac.in', '9876543215'),
('732925ITR072', 'Kavitha V', 'IT', 'B.Tech IT', 7.30, 'Web Development, React, Node.js, MongoDB', 'kavitha.it72@vcet.ac.in', '9876543216'),
('732925ITR090', 'Naveen Kumar A', 'IT', 'B.Tech IT', 8.10, 'AWS, Linux, Python, Docker, Kubernetes', 'naveen.it90@vcet.ac.in', '9876543217'),
('732925CSR010', 'Ananya R', 'CSE', 'B.E CSE', 9.15, 'Data Structures, Algorithms, C++, Java, Python', 'ananya.cs10@vcet.ac.in', '9876543218'),
('732925CSR025', 'Deepak S', 'CSE', 'B.E CSE', 8.35, 'Java, Spring Boot, MySQL, REST APIs', 'deepak.cs25@vcet.ac.in', '9876543219'),
('732925CSR042', 'Jeeva E', 'CSE', 'B.E CSE', 7.40, 'Python, Machine Learning, Scikit-Learn, SQL', 'jeeva.cs42@vcet.ac.in', '9876543220'),
('732925CSR068', 'Monisha T', 'CSE', 'B.E CSE', 8.60, 'Full Stack, React, Node.js, PostgreSQL', 'monisha.cs68@vcet.ac.in', '9876543221'),
('732925CSR084', 'Pradeep K', 'CSE', 'B.E CSE', 6.90, 'Java, Core Java, SQL, Manual Testing', 'pradeep.cs84@vcet.ac.in', '9876543222'),
('732925CSR102', 'Santhosh M', 'CSE', 'B.E CSE', 7.80, 'Cloud Basics, Linux, Python, Flask', 'santhosh.cs102@vcet.ac.in', '9876543223'),
('732925CSR115', 'Varun G', 'CSE', 'B.E CSE', 8.85, 'Algorithms, C++, Golang, Distributed Systems', 'varun.cs115@vcet.ac.in', '9876543224'),
('732925ECR008', 'Ashwin B', 'ECE', 'B.E ECE', 7.75, 'Embedded C, Microcontrollers, IoT, Python', 'ashwin.ec08@vcet.ac.in', '9876543225'),
('732925ECR019', 'Divya P', 'ECE', 'B.E ECE', 8.20, 'MATLAB, VLSI, Embedded Systems, C', 'divya.ec19@vcet.ac.in', '9876543226'),
('732925ECR033', 'Karthik S', 'ECE', 'B.E ECE', 7.10, 'C, C++, Networking, Signal Processing', 'karthik.ec33@vcet.ac.in', '9876543227'),
('732925ECR052', 'Praveen R', 'ECE', 'B.E ECE', 8.05, 'Java, SQL, Embedded C, RTOS', 'praveen.ec52@vcet.ac.in', '9876543228'),
('732925ECR077', 'Sneha M', 'ECE', 'B.E ECE', 6.75, 'Basic Electronics, Python, MS Excel', 'sneha.ec77@vcet.ac.in', '9876543229'),
('732925EER004', 'Ajith Kumar T', 'EEE', 'B.E EEE', 7.65, 'Power Electronics, MATLAB, PLC, C', 'ajith.ee04@vcet.ac.in', '9876543230'),
('732925EER018', 'Gayathri N', 'EEE', 'B.E EEE', 8.10, 'Control Systems, IoT, Python, Circuit Design', 'gayathri.ee18@vcet.ac.in', '9876543231'),
('732925EER029', 'Manikandan V', 'EEE', 'B.E EEE', 6.60, 'Electrical Machines, Basic C, AutoCAD', 'mani.ee29@vcet.ac.in', '9876543232'),
('732925EER045', 'Revathi S', 'EEE', 'B.E EEE', 7.50, 'Embedded Systems, MATLAB, Java, SQL', 'revathi.ee45@vcet.ac.in', '9876543233'),
('732925MER002', 'Arunkumar L', 'MECH', 'B.E MECH', 7.20, 'SolidWorks, AutoCAD, Python Basics', 'arun.me02@vcet.ac.in', '9876543234'),
('732925MER014', 'Dinesh Babu K', 'MECH', 'B.E MECH', 6.40, 'Manufacturing, Operations, MS Office', 'dinesh.me14@vcet.ac.in', '9876543235'),
('732925MER026', 'Kishore J', 'MECH', 'B.E MECH', 7.85, 'ANSYS, Python, Data Analytics, CAD', 'kishore.me26@vcet.ac.in', '9876543236'),
('732925MER038', 'Rahul C', 'MECH', 'B.E MECH', 6.70, 'Supply Chain, Quality Control, Basics IT', 'rahul.me38@vcet.ac.in', '9876543237'),
('732925MER051', 'Vigneshwaran P', 'MECH', 'B.E MECH', 8.00, 'Automation, Robotics, Python, C++', 'vignesh.me51@vcet.ac.in', '9876543238'),
('732925ITR095', 'Suresh Kumar N', 'IT', 'B.Tech IT', 8.25, 'Java, Spring Boot, MySQL, REST APIs, Git', 'suresh.it95@vcet.ac.in', '9876543239');

-- ----------------------------------------------------------------------------
-- 4. APPLICATIONS SEED DATA
-- ----------------------------------------------------------------------------
INSERT INTO applications (student_id, vacancy_id, apply_date, status, interview_round, remarks) VALUES
-- Student Dharaneesh K (IT, CGPA 8.92)
('732925ITR022', 1, '2026-09-01 10:15:00', 'SELECTED', 3, 'Outstanding performance in technical and system design rounds'),
('732925ITR022', 4, '2026-09-02 11:30:00', 'SELECTED', 3, 'Exceptional problem solving in Ninja coding round'),
('732925ITR022', 5, '2026-09-03 14:00:00', 'SHORTLISTED', 2, 'Cleared online assessment; awaiting technical interview'),

-- Student Abinaya S (IT, CGPA 8.45)
('732925ITR001', 1, '2026-09-01 10:45:00', 'SHORTLISTED', 2, 'Strong Core Java; cleared preliminary coding round'),
('732925ITR001', 2, '2026-09-02 12:00:00', 'SELECTED', 3, 'Selected for UI/UX full-stack engineer track'),
('732925ITR001', 3, '2026-09-03 15:20:00', 'APPLIED', 1, 'Application submitted and profile screening pending'),

-- Student Balaji R (IT, CGPA 7.60)
('732925ITR005', 1, '2026-09-01 11:10:00', 'SHORTLISTED', 2, 'Passed MCQ assessment, scheduled for coding round'),
('732925ITR005', 3, '2026-09-04 09:30:00', 'SELECTED', 3, 'Offered Digital Systems Engineer role'),
('732925ITR005', 9, '2026-09-05 16:40:00', 'APPLIED', 1, 'Application verified by placement cell'),

-- Student Chandru M (IT, CGPA 6.80)
('732925ITR015', 3, '2026-09-02 14:15:00', 'REJECTED', 1, 'Did not clear the online aptitude cutoff score'),
('732925ITR015', 8, '2026-09-04 10:00:00', 'SELECTED', 2, 'Selected as Operations Executive'),
('732925ITR015', 9, '2026-09-05 11:45:00', 'SHORTLISTED', 2, 'Cleared round 1 group discussion'),

-- Student Gokulnath P (IT, CGPA 7.95)
('732925ITR035', 6, '2026-09-02 16:00:00', 'SELECTED', 3, 'Strong SQL and analytical presentation; selected'),
('732925ITR035', 3, '2026-09-03 11:00:00', 'SHORTLISTED', 2, 'Awaiting final managerial round'),

-- Student Harini K (IT, CGPA 8.70)
('732925ITR050', 1, '2026-09-01 12:30:00', 'SELECTED', 3, 'Excellent microservices and database concepts'),
('732925ITR050', 7, '2026-09-03 14:45:00', 'SELECTED', 3, 'Offered Specialist Programmer role'),

-- Student Kavitha V (IT, CGPA 7.30)
('732925ITR072', 2, '2026-09-02 10:15:00', 'SHORTLISTED', 2, 'Good portfolio in React components'),
('732925ITR072', 9, '2026-09-04 15:30:00', 'APPLIED', 1, 'Resume under review by Cognizant recruiters'),

-- Student Naveen Kumar A (IT, CGPA 8.10)
('732925ITR090', 5, '2026-09-02 11:00:00', 'SELECTED', 3, 'Demonstrated excellent Linux and AWS architectural skills'),
('732925ITR090', 12, '2026-09-04 12:00:00', 'SHORTLISTED', 2, 'Cleared coding evaluation'),

-- Student Ananya R (CSE, CGPA 9.15)
('732925CSR010', 1, '2026-09-01 09:30:00', 'SELECTED', 3, 'Topper of the batch; exceptional algorithm optimization'),
('732925CSR010', 4, '2026-09-02 10:00:00', 'SELECTED', 3, 'Double offer secured; outstanding performance'),
('732925CSR010', 5, '2026-09-03 11:30:00', 'SHORTLISTED', 2, 'Final round interview scheduled'),

-- Student Deepak S (CSE, CGPA 8.35)
('732925CSR025', 1, '2026-09-01 11:45:00', 'SELECTED', 3, 'Strong backend design skills; selected at Zoho'),
('732925CSR025', 7, '2026-09-03 16:00:00', 'SHORTLISTED', 2, 'Cleared advanced coding round at Infosys'),

-- Student Jeeva E (CSE, CGPA 7.40)
('732925CSR042', 3, '2026-09-02 14:00:00', 'SHORTLISTED', 2, 'Good Python concepts; round 2 pending'),
('732925CSR042', 9, '2026-09-04 11:15:00', 'SELECTED', 3, 'Selected as Programmer Analyst Trainee'),

-- Student Monisha T (CSE, CGPA 8.60)
('732925CSR068', 2, '2026-09-02 13:20:00', 'SELECTED', 3, 'Offered full-stack engineering role'),
('732925CSR068', 7, '2026-09-03 15:00:00', 'SHORTLISTED', 2, 'Cleared technical interview'),

-- Student Pradeep K (CSE, CGPA 6.90)
('732925CSR084', 3, '2026-09-02 16:30:00', 'REJECTED', 1, 'CGPA criterion of 7.0 not met for TCS Digital'),
('732925CSR084', 9, '2026-09-04 14:00:00', 'SHORTLISTED', 2, 'Eligible for Cognizant PAT (min CGPA 6.5)'),

-- Student Santhosh M (CSE, CGPA 7.80)
('732925CSR102', 3, '2026-09-03 10:30:00', 'SELECTED', 3, 'Selected for TCS Digital Stream'),
('732925CSR102', 12, '2026-09-05 13:00:00', 'APPLIED', 1, 'Registered on Accenture portal'),

-- Student Varun G (CSE, CGPA 8.85)
('732925CSR115', 4, '2026-09-02 10:45:00', 'SELECTED', 3, 'TCS Ninja selected; superior coding speed'),
('732925CSR115', 5, '2026-09-03 12:15:00', 'SELECTED', 3, 'AWS Cloud Support role offered'),

-- Student Ashwin B (ECE, CGPA 7.75)
('732925ECR008', 11, '2026-09-02 11:00:00', 'SELECTED', 3, 'Selected at Bosch for Automotive Embedded firmware'),
('732925ECR008', 3, '2026-09-03 14:30:00', 'APPLIED', 1, 'Profile shortlisted for preliminary exam'),

-- Student Divya P (ECE, CGPA 8.20)
('732925ECR019', 11, '2026-09-02 11:30:00', 'SELECTED', 3, 'High aptitude in RTOS and C; offer extended'),
('732925ECR019', 5, '2026-09-04 10:00:00', 'SHORTLISTED', 2, 'Cleared round 1 technical screen'),

-- Student Karthik S (ECE, CGPA 7.10)
('732925ECR033', 9, '2026-09-03 15:45:00', 'SHORTLISTED', 2, 'Passed CTS online assessment'),
('732925ECR033', 11, '2026-09-04 11:00:00', 'REJECTED', 2, 'Did not clear hardware timing analysis round'),

-- Student Praveen R (ECE, CGPA 8.05)
('732925ECR052', 6, '2026-09-03 12:00:00', 'SELECTED', 3, 'Associate SAP Consultant offer extended'),
('732925ECR052', 11, '2026-09-04 14:30:00', 'SHORTLISTED', 2, 'Awaiting final HR interview'),

-- Student Sneha M (ECE, CGPA 6.75)
('732925ECR077', 8, '2026-09-03 16:00:00', 'SELECTED', 2, 'Selected at Infosys BPM / Operations'),
('732925ECR077', 9, '2026-09-05 10:15:00', 'SHORTLISTED', 2, 'Cognizant GD cleared'),

-- Student Ajith Kumar T (EEE, CGPA 7.65)
('732925EER004', 11, '2026-09-02 14:00:00', 'SELECTED', 3, 'Selected at Bosch Embedded Unit'),
('732925EER004', 9, '2026-09-04 11:30:00', 'APPLIED', 1, 'Profile verified'),

-- Student Gayathri N (EEE, CGPA 8.10)
('732925EER018', 11, '2026-09-02 14:30:00', 'SELECTED', 3, 'Bosch offer received; top score in circuits round'),
('732925EER018', 12, '2026-09-04 15:00:00', 'SHORTLISTED', 2, 'Accenture technical round cleared'),

-- Student Manikandan V (EEE, CGPA 6.60)
('732925EER029', 8, '2026-09-03 10:00:00', 'SELECTED', 2, 'Selected for Operations role'),
('732925EER029', 3, '2026-09-04 14:00:00', 'REJECTED', 1, 'Cutoff criteria not met'),

-- Student Revathi S (EEE, CGPA 7.50)
('732925EER045', 9, '2026-09-03 11:15:00', 'SELECTED', 3, 'Selected at Cognizant CTS'),
('732925EER045', 12, '2026-09-05 14:00:00', 'SHORTLISTED', 2, 'Awaiting HR round results'),

-- Student Arunkumar L (MECH, CGPA 7.20)
('732925MER002', 3, '2026-09-03 13:00:00', 'SHORTLISTED', 2, 'Passed written assessment'),
('732925MER002', 12, '2026-09-05 10:45:00', 'SELECTED', 3, 'Selected at Accenture Solutions'),

-- Student Dinesh Babu K (MECH, CGPA 6.40)
('732925MER014', 8, '2026-09-03 15:30:00', 'SELECTED', 2, 'Selected as Operations Executive at Infosys'),

-- Student Kishore J (MECH, CGPA 7.85)
('732925MER026', 3, '2026-09-03 16:15:00', 'SELECTED', 3, 'Selected as Systems Engineer at TCS'),
('732925MER026', 12, '2026-09-05 11:30:00', 'SHORTLISTED', 2, 'Accenture technical interview cleared'),

-- Student Rahul C (MECH, CGPA 6.70)
('732925MER038', 8, '2026-09-04 09:30:00', 'SHORTLISTED', 2, 'Cleared aptitude assessment'),
('732925MER038', 12, '2026-09-05 15:00:00', 'REJECTED', 2, 'Did not clear managerial discussion'),

-- Student Vigneshwaran P (MECH, CGPA 8.00)
('732925MER051', 3, '2026-09-03 11:45:00', 'SELECTED', 3, 'TCS Systems Engineer offer accepted'),
('732925MER051', 12, '2026-09-05 16:30:00', 'SELECTED', 3, 'Accenture Associate Software Engineer offer received'),

-- Student Suresh Kumar N (IT, CGPA 8.25)
('732925ITR095', 1, '2026-09-02 10:30:00', 'SELECTED', 3, 'Cleared technical interview and system design round at Zoho'),
('732925ITR095', 3, '2026-09-04 14:15:00', 'SHORTLISTED', 2, 'Passed TCS Digital assessment; awaiting final interview');
