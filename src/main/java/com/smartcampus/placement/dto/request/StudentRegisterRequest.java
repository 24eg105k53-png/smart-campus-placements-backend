package com.smartcampus.placement.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class StudentRegisterRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 150, message = "Full name must be between 2 and 150 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Registration number is required")
    private String studentRegistrationNumber;

    @NotBlank(message = "Department is required")
    private String department;

    @NotNull(message = "Graduation year is required")
    @Min(value = 2020, message = "Graduation year must be valid")
    @Max(value = 2035, message = "Graduation year must be valid")
    private Integer graduationYear;

    @NotNull(message = "CGPA is required")
    @DecimalMin(value = "0.00", message = "CGPA cannot be negative")
    @DecimalMax(value = "10.00", message = "CGPA cannot exceed 10.00")
    private BigDecimal cgpa;

    @NotNull(message = "Active backlogs is required")
    @Min(value = 0, message = "Active backlogs cannot be negative")
    private Integer activeBacklogs;

    public StudentRegisterRequest() {}

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getStudentRegistrationNumber() { return studentRegistrationNumber; }
    public void setStudentRegistrationNumber(String studentRegistrationNumber) { this.studentRegistrationNumber = studentRegistrationNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Integer getGraduationYear() { return graduationYear; }
    public void setGraduationYear(Integer graduationYear) { this.graduationYear = graduationYear; }

    public BigDecimal getCgpa() { return cgpa; }
    public void setCgpa(BigDecimal cgpa) { this.cgpa = cgpa; }

    public Integer getActiveBacklogs() { return activeBacklogs; }
    public void setActiveBacklogs(Integer activeBacklogs) { this.activeBacklogs = activeBacklogs; }
}
