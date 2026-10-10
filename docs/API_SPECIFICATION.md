# Smart Campus Placement Platform - REST API Specification

**Base URL**: `http://localhost:8080/api/v1`  
**Content-Type**: `application/json`  
**Authentication**: Bearer JWT (`Authorization: Bearer <token>`)

---

## 1. Authentication Endpoints (`/auth`)

### 1.1 POST `/auth/register/student`
Self-service registration for undergraduate students.
- **Request Body**:
  ```json
  {
    "fullName": "Jane Doe",
    "email": "jane@smartcampus.edu",
    "password": "Password@123",
    "studentRegistrationNumber": "REG2026CSE042",
    "department": "Computer Science and Engineering",
    "graduationYear": 2026,
    "cgpa": 8.75,
    "activeBacklogs": 0
  }
  ```
- **Response** `201 Created`:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "userId": 4,
    "email": "jane@smartcampus.edu",
    "fullName": "Jane Doe",
    "role": "STUDENT",
    "profileId": 4
  }
  ```

### 1.2 POST `/auth/login`
Unified login for all authorized roles (`STUDENT`, `RECRUITER`, `TPO`).
- **Request Body**:
  ```json
  {
    "email": "student@smartcampus.edu",
    "password": "Password@123"
  }
  ```
- **Response** `200 OK`:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "userId": 3,
    "email": "student@smartcampus.edu",
    "fullName": "John Doe",
    "role": "STUDENT",
    "profileId": 1
  }
  ```

### 1.3 GET `/auth/me`
Retrieve currently authenticated user identity and role claims.
- **Headers**: `Authorization: Bearer <token>`
- **Response** `200 OK`:
  ```json
  {
    "id": 3,
    "email": "student@smartcampus.edu",
    "fullName": "John Doe",
    "role": "STUDENT",
    "accountStatus": "ACTIVE",
    "profileId": 1
  }
  ```

---

## 2. Student Profile & Resume Endpoints (`/student`)

### 2.1 GET `/student/profile`
Retrieve the logged-in student's full academic record, skills, and resume summary.
- **Authorized Role**: `STUDENT`

### 2.2 PUT `/student/profile`
Update student phone, portfolio links, CGPA, backlogs, and associated skills.
- **Authorized Role**: `STUDENT`

### 2.3 POST `/student/resume/upload`
Upload PDF or DOCX resume document.
- **Content-Type**: `multipart/form-data`
- **Body**: `file` (binary PDF/DOCX)
- **Response** `200 OK`:
  ```json
  {
    "id": 1,
    "fileReference": "uuid-resume.pdf",
    "fileType": "PDF",
    "processingStatus": "COMPLETED",
    "extractedSkillsJson": "[\"Java\",\"Spring Boot\",\"PostgreSQL\",\"Docker\"]",
    "extractedSummary": "Undergraduate developer experienced in cloud microservices.",
    "extractedCgpa": 8.5
  }
  ```

### 2.4 GET `/student/readiness`
Retrieve the explainable multi-factor placement readiness index.
- **Response** `200 OK`:
  ```json
  {
    "readinessScore": 84,
    "readinessTier": "HIGH_READINESS",
    "breakdown": {
      "academicScore": 30,
      "skillsScore": 32,
      "profileCompletenessScore": 22
    },
    "recommendations": [
      "Your academic standing is strong with zero active backlogs."
    ]
  }
  ```

---

## 3. Placement Drives & Eligibility (`/jobs`)

### 3.1 GET `/jobs`
List all active job postings with search and pagination.
- **Query Params**: `search`, `page`, `size`, `sort`

### 3.2 GET `/jobs/{id}`
Retrieve single job opening details. Includes student eligibility flag if student is authenticated.

### 3.3 GET `/jobs/{id}/eligibility`
Deterministic pre-application criteria evaluation.
- **Response** `200 OK`:
  ```json
  {
    "eligible": true,
    "studentCgpa": 8.50,
    "minimumCgpa": 7.50,
    "cgpaEligible": true,
    "studentBacklogs": 0,
    "maximumBacklogs": 0,
    "backlogsEligible": true,
    "studentDepartment": "Computer Science and Engineering",
    "eligibleDepartments": "Computer Science and Engineering,Information Technology",
    "departmentEligible": true,
    "failureReasons": []
  }
  ```

### 3.4 GET `/jobs/{id}/skill-gap`
AI-driven TF-IDF skill gap analysis comparing candidate skills against requirements.
- **Response** `200 OK`:
  ```json
  {
    "matchPercentage": 75.0,
    "matchingSkills": ["Java", "Spring Boot", "PostgreSQL"],
    "missingSkills": ["Docker"],
    "cosineSimilarity": 0.82,
    "explanation": "You match 3 out of 4 required skills (75%). Adding Docker will boost your match score."
  }
  ```

---

## 4. Application Workflow (`/applications`)

### 4.1 POST `/applications/apply/{jobId}`
Submit application. Triggers deterministic eligibility engine before creation. Rejects ineligible students with `422 Unprocessable Entity`.

### 4.2 GET `/applications/my`
List logged-in student's applications.

### 4.3 GET `/applications/job/{jobId}`
List all candidates for a job posting. Authorized for `RECRUITER` (company owner) and `TPO`.

### 4.4 PATCH `/applications/{id}/status`
Update candidate status (`SHORTLISTED`, `INTERVIEW_SCHEDULED`, `REJECTED`, `PLACED`).
