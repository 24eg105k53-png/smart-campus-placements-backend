-- V2__seed_test_data.sql

-- BCrypt hash for 'Password@123': $2a$10$eACCYoNOHEqgkDwBv5lncuE.wz/XvI.nBfg51gY51M5Zl/N.n.a9K
INSERT INTO users (id, full_name, email, password_hash, role, account_status, created_at, updated_at)
VALUES 
(1, 'TPO Administrator', 'tpo@smartcampus.edu', '$2a$10$eACCYoNOHEqgkDwBv5lncuE.wz/XvI.nBfg51gY51M5Zl/N.n.a9K', 'TPO', 'ACTIVE', NOW(), NOW()),
(2, 'Google Recruiter', 'recruiter@google.com', '$2a$10$eACCYoNOHEqgkDwBv5lncuE.wz/XvI.nBfg51gY51M5Zl/N.n.a9K', 'RECRUITER', 'ACTIVE', NOW(), NOW()),
(3, 'John Doe', 'student@smartcampus.edu', '$2a$10$eACCYoNOHEqgkDwBv5lncuE.wz/XvI.nBfg51gY51M5Zl/N.n.a9K', 'STUDENT', 'ACTIVE', NOW(), NOW()),
(4, 'Alice Sharma', 'alice@smartcampus.edu', '$2a$10$eACCYoNOHEqgkDwBv5lncuE.wz/XvI.nBfg51gY51M5Zl/N.n.a9K', 'STUDENT', 'ACTIVE', NOW(), NOW()),
(5, 'Bob Verma', 'bob@smartcampus.edu', '$2a$10$eACCYoNOHEqgkDwBv5lncuE.wz/XvI.nBfg51gY51M5Zl/N.n.a9K', 'STUDENT', 'ACTIVE', NOW(), NOW());

INSERT INTO companies (id, name, description, website, industry, recruiter_user_id, created_at, updated_at)
VALUES
(1, 'Google Cloud India', 'Leading global technology company specializing in cloud computing and software solutions.', 'https://cloud.google.com', 'Information Technology', 2, NOW(), NOW()),
(2, 'Microsoft IDC', 'Global technology leader empowering organizations through intelligent cloud platforms.', 'https://microsoft.com', 'Software Engineering', NULL, NOW(), NOW());

INSERT INTO student_profiles (id, user_id, student_registration_number, university, degree, department, graduation_year, cgpa, active_backlogs, resume_file_path, created_at, updated_at)
VALUES
(1, 3, 'REG2026CSE001', 'State Technical University', 'B.Tech', 'Computer Science and Engineering', 2026, 8.50, 0, 'sample_john_doe_resume.pdf', NOW(), NOW()),
(2, 4, 'REG2026CSE002', 'State Technical University', 'B.Tech', 'Computer Science and Engineering', 2026, 9.20, 0, NULL, NOW(), NOW()),
(3, 5, 'REG2026ECE001', 'State Technical University', 'B.Tech', 'Electronics and Communication Engineering', 2026, 7.30, 0, NULL, NOW(), NOW());

INSERT INTO skills (id, name, normalized_name, category)
VALUES
(1, 'Java', 'java', 'TECHNICAL'),
(2, 'Spring Boot', 'spring boot', 'TECHNICAL'),
(3, 'PostgreSQL', 'postgresql', 'TECHNICAL'),
(4, 'React', 'react', 'TECHNICAL'),
(5, 'Docker', 'docker', 'TECHNICAL'),
(6, 'Kubernetes', 'kubernetes', 'TECHNICAL'),
(7, 'Python', 'python', 'TECHNICAL'),
(8, 'FastAPI', 'fastapi', 'TECHNICAL'),
(9, 'Data Structures', 'data structures', 'TECHNICAL'),
(10, 'Git', 'git', 'TECHNICAL');

INSERT INTO student_skills (student_profile_id, skill_id, proficiency_level)
VALUES
(1, 1, 'ADVANCED'),
(1, 2, 'INTERMEDIATE'),
(1, 3, 'INTERMEDIATE'),
(1, 4, 'INTERMEDIATE'),
(1, 10, 'ADVANCED');

INSERT INTO job_postings (id, company_id, title, description, job_location, minimum_cgpa, maximum_backlogs, eligible_departments, target_graduation_year, ctc_range, application_deadline, status, created_at, updated_at)
VALUES
(1, 1, 'Software Engineer (Backend)', 'Build high-scale distributed microservices using Java, Spring Boot, and PostgreSQL. Design clean REST APIs and event-driven architectures.', 'Bangalore / Hybrid', 7.50, 0, 'Computer Science and Engineering,Information Technology', 2026, '18 LPA - 24 LPA', NOW() + INTERVAL '30 days', 'PUBLISHED', NOW(), NOW()),
(2, 1, 'Cloud Platform Intern', 'Support site reliability, containerization, and automated deployments with Docker, Kubernetes, and Linux fundamentals.', 'Hyderabad', 7.00, 0, 'Computer Science and Engineering,Electronics and Communication Engineering,Information Technology', 2026, '12 LPA - 15 LPA', NOW() + INTERVAL '45 days', 'PUBLISHED', NOW(), NOW());

INSERT INTO job_skills (job_posting_id, skill_id, is_mandatory)
VALUES
(1, 1, TRUE),
(1, 2, TRUE),
(1, 3, TRUE),
(1, 5, FALSE),
(2, 5, TRUE),
(2, 6, TRUE),
(2, 10, TRUE);

INSERT INTO applications (id, student_profile_id, job_posting_id, status, recruiter_notes, applied_at, updated_at)
VALUES
(1, 1, 1, 'APPLIED', 'Strong academic track record and verified Java/Spring Boot competencies.', NOW(), NOW());

INSERT INTO placement_records (id, student_profile_id, company_id, job_posting_id, placement_year, ctc_amount, offer_date, verified_by_tpo, created_at)
VALUES
(1, 2, 1, 1, 2026, 22.00, CURRENT_DATE, TRUE, NOW());
