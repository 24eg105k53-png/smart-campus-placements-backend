package com.smartcampus.placement.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class EligibilityResultResponse {

    private boolean eligible;
    private BigDecimal studentCgpa;
    private BigDecimal minimumCgpa;
    private boolean cgpaEligible;

    private Integer studentBacklogs;
    private Integer maximumBacklogs;
    private boolean backlogsEligible;

    private String studentDepartment;
    private String eligibleDepartments;
    private boolean departmentEligible;

    private Integer studentGraduationYear;
    private Integer targetGraduationYear;
    private boolean graduationYearEligible;

    private List<String> failureReasons;

    public EligibilityResultResponse() {}

    public boolean isEligible() { return eligible; }
    public void setEligible(boolean eligible) { this.eligible = eligible; }

    public BigDecimal getStudentCgpa() { return studentCgpa; }
    public void setStudentCgpa(BigDecimal studentCgpa) { this.studentCgpa = studentCgpa; }

    public BigDecimal getMinimumCgpa() { return minimumCgpa; }
    public void setMinimumCgpa(BigDecimal minimumCgpa) { this.minimumCgpa = minimumCgpa; }

    public boolean isCgpaEligible() { return cgpaEligible; }
    public void setCgpaEligible(boolean cgpaEligible) { this.cgpaEligible = cgpaEligible; }

    public Integer getStudentBacklogs() { return studentBacklogs; }
    public void setStudentBacklogs(Integer studentBacklogs) { this.studentBacklogs = studentBacklogs; }

    public Integer getMaximumBacklogs() { return maximumBacklogs; }
    public void setMaximumBacklogs(Integer maximumBacklogs) { this.maximumBacklogs = maximumBacklogs; }

    public boolean isBacklogsEligible() { return backlogsEligible; }
    public void setBacklogsEligible(boolean backlogsEligible) { this.backlogsEligible = backlogsEligible; }

    public String getStudentDepartment() { return studentDepartment; }
    public void setStudentDepartment(String studentDepartment) { this.studentDepartment = studentDepartment; }

    public String getEligibleDepartments() { return eligibleDepartments; }
    public void setEligibleDepartments(String eligibleDepartments) { this.eligibleDepartments = eligibleDepartments; }

    public boolean isDepartmentEligible() { return departmentEligible; }
    public void setDepartmentEligible(boolean departmentEligible) { this.departmentEligible = departmentEligible; }

    public Integer getStudentGraduationYear() { return studentGraduationYear; }
    public void setStudentGraduationYear(Integer studentGraduationYear) { this.studentGraduationYear = studentGraduationYear; }

    public Integer getTargetGraduationYear() { return targetGraduationYear; }
    public void setTargetGraduationYear(Integer targetGraduationYear) { this.targetGraduationYear = targetGraduationYear; }

    public boolean isGraduationYearEligible() { return graduationYearEligible; }
    public void setGraduationYearEligible(boolean graduationYearEligible) { this.graduationYearEligible = graduationYearEligible; }

    public List<String> getFailureReasons() { return failureReasons; }
    public void setFailureReasons(List<String> failureReasons) { this.failureReasons = failureReasons; }
}
