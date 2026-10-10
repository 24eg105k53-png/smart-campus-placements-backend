package com.smartcampus.placement.config;

import com.smartcampus.placement.entity.*;
import com.smartcampus.placement.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final SkillRepository skillRepository;
    private final JobPostingRepository jobPostingRepository;
    private final ApplicationRepository applicationRepository;
    private final PlacementRecordRepository placementRecordRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      CompanyRepository companyRepository,
                      StudentProfileRepository studentProfileRepository,
                      SkillRepository skillRepository,
                      JobPostingRepository jobPostingRepository,
                      ApplicationRepository applicationRepository,
                      PlacementRecordRepository placementRecordRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.skillRepository = skillRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.applicationRepository = applicationRepository;
        this.placementRecordRepository = placementRecordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already initialized with users. Skipping DataSeeder.");
            return;
        }

        log.info("Seeding initial demo data for Smart Campus Placement System...");

        String encodedPassword = passwordEncoder.encode("Password@123");

        // 1. Create TPO User
        User tpoUser = new User("Dr. K. Raman (TPO)", "tpo@smartcampus.edu", encodedPassword, User.Role.TPO);
        userRepository.save(tpoUser);

        // 2. Create Recruiter User & Company
        User recruiterUser = new User("Google Campus Recruiter", "recruiter@google.com", encodedPassword, User.Role.RECRUITER);
        recruiterUser = userRepository.save(recruiterUser);

        Company google = new Company(
            "Google Cloud India",
            "Leading global cloud and software technology company.",
            "https://cloud.google.com",
            "Information Technology",
            recruiterUser
        );
        google = companyRepository.save(google);

        Company microsoft = new Company(
            "Microsoft IDC",
            "Global software and artificial intelligence innovator.",
            "https://microsoft.com",
            "Software Development",
            null
        );
        microsoft = companyRepository.save(microsoft);

        // 3. Create Skills
        Skill java = skillRepository.save(new Skill("Java", "java", "TECHNICAL"));
        Skill springBoot = skillRepository.save(new Skill("Spring Boot", "spring boot", "TECHNICAL"));
        Skill postgreSql = skillRepository.save(new Skill("PostgreSQL", "postgresql", "TECHNICAL"));
        Skill react = skillRepository.save(new Skill("React", "react", "TECHNICAL"));
        Skill docker = skillRepository.save(new Skill("Docker", "docker", "TECHNICAL"));
        Skill kubernetes = skillRepository.save(new Skill("Kubernetes", "kubernetes", "TECHNICAL"));
        Skill python = skillRepository.save(new Skill("Python", "python", "TECHNICAL"));
        Skill fastapi = skillRepository.save(new Skill("FastAPI", "fastapi", "TECHNICAL"));
        Skill dsa = skillRepository.save(new Skill("Data Structures", "data structures", "TECHNICAL"));
        Skill git = skillRepository.save(new Skill("Git", "git", "TECHNICAL"));

        // 4. Create Students
        User studentUser1 = new User("John Doe", "student@smartcampus.edu", encodedPassword, User.Role.STUDENT);
        studentUser1 = userRepository.save(studentUser1);
        StudentProfile profile1 = new StudentProfile(
            studentUser1, "REG2026CSE001", "Computer Science and Engineering", 2026, new BigDecimal("8.50"), 0
        );
        profile1.setPhoneNumber("+91 9876543210");
        profile1.setGithubProfileUrl("https://github.com/johndoe");
        profile1.setLinkedinProfileUrl("https://linkedin.com/in/johndoe");
        profile1 = studentProfileRepository.save(profile1);

        profile1.getStudentSkills().add(new StudentSkill(profile1, java, "ADVANCED"));
        profile1.getStudentSkills().add(new StudentSkill(profile1, springBoot, "INTERMEDIATE"));
        profile1.getStudentSkills().add(new StudentSkill(profile1, postgreSql, "INTERMEDIATE"));
        profile1.getStudentSkills().add(new StudentSkill(profile1, react, "INTERMEDIATE"));
        profile1.getStudentSkills().add(new StudentSkill(profile1, git, "ADVANCED"));
        studentProfileRepository.save(profile1);

        User studentUser2 = new User("Alice Sharma", "alice@smartcampus.edu", encodedPassword, User.Role.STUDENT);
        studentUser2 = userRepository.save(studentUser2);
        StudentProfile profile2 = new StudentProfile(
            studentUser2, "REG2026CSE002", "Computer Science and Engineering", 2026, new BigDecimal("9.20"), 0
        );
        profile2 = studentProfileRepository.save(profile2);
        profile2.getStudentSkills().add(new StudentSkill(profile2, java, "ADVANCED"));
        profile2.getStudentSkills().add(new StudentSkill(profile2, python, "ADVANCED"));
        profile2.getStudentSkills().add(new StudentSkill(profile2, docker, "INTERMEDIATE"));
        studentProfileRepository.save(profile2);

        User studentUser3 = new User("Bob Verma", "bob@smartcampus.edu", encodedPassword, User.Role.STUDENT);
        studentUser3 = userRepository.save(studentUser3);
        StudentProfile profile3 = new StudentProfile(
            studentUser3, "REG2026ECE001", "Electronics and Communication Engineering", 2026, new BigDecimal("7.30"), 0
        );
        studentProfileRepository.save(profile3);

        // 5. Create Job Postings
        JobPosting job1 = new JobPosting();
        job1.setCompany(google);
        job1.setTitle("Software Engineer (Backend)");
        job1.setDescription("Build distributed cloud backend systems at hyper-scale. Hands-on expertise in Java, Spring Boot, and PostgreSQL required.");
        job1.setJobLocation("Bangalore / Hybrid");
        job1.setMinimumCgpa(new BigDecimal("7.50"));
        job1.setMaximumBacklogs(0);
        job1.setEligibleDepartments("Computer Science and Engineering,Information Technology");
        job1.setTargetGraduationYear(2026);
        job1.setCtcRange("18 LPA - 24 LPA");
        job1.setApplicationDeadline(Instant.now().plus(30, ChronoUnit.DAYS));
        job1.setStatus(JobPosting.JobStatus.PUBLISHED);
        job1.setRequiredSkills(new HashSet<>(List.of(java, springBoot, postgreSql, docker)));
        job1 = jobPostingRepository.save(job1);

        JobPosting job2 = new JobPosting();
        job2.setCompany(google);
        job2.setTitle("Cloud Platform Engineer");
        job2.setDescription("Manage containerization, cloud networking, and reliability infrastructure using Docker, Kubernetes, and Git.");
        job2.setJobLocation("Hyderabad");
        job2.setMinimumCgpa(new BigDecimal("7.00"));
        job2.setMaximumBacklogs(0);
        job2.setEligibleDepartments("Computer Science and Engineering,Electronics and Communication Engineering,Information Technology");
        job2.setTargetGraduationYear(2026);
        job2.setCtcRange("14 LPA - 18 LPA");
        job2.setApplicationDeadline(Instant.now().plus(45, ChronoUnit.DAYS));
        job2.setStatus(JobPosting.JobStatus.PUBLISHED);
        job2.setRequiredSkills(new HashSet<>(List.of(docker, kubernetes, git, python)));
        job2 = jobPostingRepository.save(job2);

        JobPosting job3 = new JobPosting();
        job3.setCompany(microsoft);
        job3.setTitle("Full Stack Developer");
        job3.setDescription("Develop high-performing web platforms with React and modern REST microservices.");
        job3.setJobLocation("Noida / Remote");
        job3.setMinimumCgpa(new BigDecimal("6.50"));
        job3.setMaximumBacklogs(1);
        job3.setEligibleDepartments("Computer Science and Engineering,Information Technology");
        job3.setTargetGraduationYear(2026);
        job3.setCtcRange("15 LPA - 20 LPA");
        job3.setApplicationDeadline(Instant.now().plus(20, ChronoUnit.DAYS));
        job3.setStatus(JobPosting.JobStatus.PUBLISHED);
        job3.setRequiredSkills(new HashSet<>(List.of(react, java, git)));
        job3 = jobPostingRepository.save(job3);

        // 6. Create Initial Application
        Application app1 = new Application(profile1, job1);
        app1.setStatus(Application.ApplicationStatus.APPLIED);
        app1.setRecruiterNotes("Strong Java fundamentals and solid academic profile.");
        applicationRepository.save(app1);

        // 7. Create Verified Placement Record
        PlacementRecord placement1 = new PlacementRecord(
            profile2, google, job1, 2026, new BigDecimal("22.00")
        );
        placementRecordRepository.save(placement1);

        log.info("Demo data seeding completed successfully! Users seeded: Student, Recruiter, TPO.");
    }
}
