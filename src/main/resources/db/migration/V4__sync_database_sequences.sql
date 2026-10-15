-- V4__sync_database_sequences.sql
-- Synchronize PostgreSQL serial sequences with current max primary key values

SELECT setval(pg_get_serial_sequence('users', 'id'), COALESCE((SELECT MAX(id) FROM users), 1));
SELECT setval(pg_get_serial_sequence('companies', 'id'), COALESCE((SELECT MAX(id) FROM companies), 1));
SELECT setval(pg_get_serial_sequence('student_profiles', 'id'), COALESCE((SELECT MAX(id) FROM student_profiles), 1));
SELECT setval(pg_get_serial_sequence('skills', 'id'), COALESCE((SELECT MAX(id) FROM skills), 1));
SELECT setval(pg_get_serial_sequence('job_postings', 'id'), COALESCE((SELECT MAX(id) FROM job_postings), 1));
SELECT setval(pg_get_serial_sequence('applications', 'id'), COALESCE((SELECT MAX(id) FROM applications), 1));
SELECT setval(pg_get_serial_sequence('resume_analyses', 'id'), COALESCE((SELECT MAX(id) FROM resume_analyses), 1));
SELECT setval(pg_get_serial_sequence('placement_records', 'id'), COALESCE((SELECT MAX(id) FROM placement_records), 1));
