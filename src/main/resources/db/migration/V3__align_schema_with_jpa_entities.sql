-- V3__align_schema_with_jpa_entities.sql

-- 1. Align applications table with Application entity (which extends BaseEntity)
ALTER TABLE applications ADD COLUMN IF NOT EXISTS created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
UPDATE applications SET created_at = applied_at WHERE created_at IS NULL;
ALTER TABLE applications ALTER COLUMN created_at SET NOT NULL;

-- 2. Align placement_records table with PlacementRecord entity (which extends BaseEntity)
ALTER TABLE placement_records ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
UPDATE placement_records SET updated_at = created_at WHERE updated_at IS NULL;
ALTER TABLE placement_records ALTER COLUMN updated_at SET NOT NULL;

-- 3. Align student_profiles table with StudentProfile entity extra fields
ALTER TABLE student_profiles ADD COLUMN IF NOT EXISTS phone_number VARCHAR(30);
ALTER TABLE student_profiles ADD COLUMN IF NOT EXISTS github_profile_url VARCHAR(255);
ALTER TABLE student_profiles ADD COLUMN IF NOT EXISTS linkedin_profile_url VARCHAR(255);

-- 4. Seed initial profile contact info for demo student if null
UPDATE student_profiles SET 
    phone_number = '+91 9876543210',
    github_profile_url = 'https://github.com/johndoe',
    linkedin_profile_url = 'https://linkedin.com/in/johndoe'
WHERE id = 1 AND phone_number IS NULL;
