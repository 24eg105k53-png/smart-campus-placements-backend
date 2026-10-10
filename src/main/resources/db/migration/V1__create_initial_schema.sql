-- V1__create_initial_schema.sql

-- 1. USERS TABLE
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL CHECK (role IN ('STUDENT', 'TPO', 'RECRUITER')),
    account_status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE' CHECK (account_status IN ('ACTIVE', 'SUSPENDED', 'PENDING_APPROVAL')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

-- 2. COMPANIES TABLE
CREATE TABLE companies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(180) NOT NULL UNIQUE,
    description TEXT,
    website VARCHAR(255),
    industry VARCHAR(100),
    recruiter_user_id BIGINT UNIQUE REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- 3. STUDENT PROFILES TABLE
CREATE TABLE student_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    student_registration_number VARCHAR(60) NOT NULL UNIQUE,
    university VARCHAR(180) NOT NULL DEFAULT 'State Technical University',
    degree VARCHAR(100) NOT NULL DEFAULT 'B.Tech',
    department VARCHAR(100) NOT NULL,
    graduation_year INTEGER NOT NULL,
    cgpa NUMERIC(4, 2) NOT NULL CHECK (cgpa >= 0.00 AND cgpa <= 10.00),
    active_backlogs INTEGER NOT NULL DEFAULT 0 CHECK (active_backlogs >= 0),
    resume_file_path VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_student_department ON student_profiles(department);
CREATE INDEX idx_student_cgpa ON student_profiles(cgpa);
CREATE INDEX idx_student_grad_year ON student_profiles(graduation_year);

-- 4. SKILLS TABLE
CREATE TABLE skills (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    normalized_name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(50) DEFAULT 'TECHNICAL'
);

CREATE INDEX idx_skills_normalized ON skills(normalized_name);

-- 5. STUDENT SKILLS (JOIN TABLE)
CREATE TABLE student_skills (
    student_profile_id BIGINT NOT NULL REFERENCES student_profiles(id) ON DELETE CASCADE,
    skill_id BIGINT NOT NULL REFERENCES skills(id) ON DELETE CASCADE,
    proficiency_level VARCHAR(30) DEFAULT 'INTERMEDIATE',
    PRIMARY KEY (student_profile_id, skill_id)
);

-- 6. JOB POSTINGS TABLE
CREATE TABLE job_postings (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
    title VARCHAR(180) NOT NULL,
    description TEXT NOT NULL,
    job_location VARCHAR(120) DEFAULT 'On-Campus / Hybrid',
    minimum_cgpa NUMERIC(4, 2) NOT NULL DEFAULT 6.00 CHECK (minimum_cgpa >= 0.00 AND minimum_cgpa <= 10.00),
    maximum_backlogs INTEGER NOT NULL DEFAULT 0 CHECK (maximum_backlogs >= 0),
    eligible_departments TEXT NOT NULL,
    target_graduation_year INTEGER NOT NULL,
    ctc_range VARCHAR(80),
    application_deadline TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PUBLISHED' CHECK (status IN ('DRAFT', 'PUBLISHED', 'CLOSED', 'ARCHIVED')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_jobs_company ON job_postings(company_id);
CREATE INDEX idx_jobs_status ON job_postings(status);
CREATE INDEX idx_jobs_deadline ON job_postings(application_deadline);

-- 7. JOB REQUIRED SKILLS (JOIN TABLE)
CREATE TABLE job_skills (
    job_posting_id BIGINT NOT NULL REFERENCES job_postings(id) ON DELETE CASCADE,
    skill_id BIGINT NOT NULL REFERENCES skills(id) ON DELETE CASCADE,
    is_mandatory BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (job_posting_id, skill_id)
);

-- 8. APPLICATIONS TABLE
CREATE TABLE applications (
    id BIGSERIAL PRIMARY KEY,
    student_profile_id BIGINT NOT NULL REFERENCES student_profiles(id) ON DELETE CASCADE,
    job_posting_id BIGINT NOT NULL REFERENCES job_postings(id) ON DELETE CASCADE,
    status VARCHAR(30) NOT NULL DEFAULT 'APPLIED' CHECK (status IN ('APPLIED', 'SHORTLISTED', 'INTERVIEW_SCHEDULED', 'REJECTED', 'PLACED')),
    recruiter_notes TEXT,
    applied_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT uq_student_job_application UNIQUE (student_profile_id, job_posting_id)
);

CREATE INDEX idx_applications_student ON applications(student_profile_id);
CREATE INDEX idx_applications_job ON applications(job_posting_id);
CREATE INDEX idx_applications_status ON applications(status);

-- 9. RESUME ANALYSES TABLE
CREATE TABLE resume_analyses (
    id BIGSERIAL PRIMARY KEY,
    student_profile_id BIGINT NOT NULL UNIQUE REFERENCES student_profiles(id) ON DELETE CASCADE,
    file_reference VARCHAR(255) NOT NULL,
    file_type VARCHAR(10) NOT NULL,
    processing_status VARCHAR(30) NOT NULL DEFAULT 'COMPLETED' CHECK (processing_status IN ('PENDING', 'COMPLETED', 'FAILED')),
    extracted_skills_json TEXT,
    extracted_summary TEXT,
    extracted_cgpa NUMERIC(4, 2),
    raw_text_length INTEGER,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- 10. PLACEMENT RECORDS TABLE
CREATE TABLE placement_records (
    id BIGSERIAL PRIMARY KEY,
    student_profile_id BIGINT NOT NULL REFERENCES student_profiles(id) ON DELETE CASCADE,
    company_id BIGINT NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
    job_posting_id BIGINT REFERENCES job_postings(id) ON DELETE SET NULL,
    placement_year INTEGER NOT NULL,
    ctc_amount NUMERIC(10, 2) NOT NULL CHECK (ctc_amount >= 0.00),
    offer_date DATE DEFAULT CURRENT_DATE NOT NULL,
    verified_by_tpo BOOLEAN DEFAULT TRUE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_placement_year ON placement_records(placement_year);
CREATE INDEX idx_placement_company ON placement_records(company_id);
