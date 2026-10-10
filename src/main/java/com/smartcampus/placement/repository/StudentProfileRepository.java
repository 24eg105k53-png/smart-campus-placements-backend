package com.smartcampus.placement.repository;

import com.smartcampus.placement.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    Optional<StudentProfile> findByUserId(Long userId);
    Optional<StudentProfile> findByStudentRegistrationNumber(String studentRegistrationNumber);
    boolean existsByStudentRegistrationNumber(String studentRegistrationNumber);
    List<StudentProfile> findByDepartment(String department);

    @Query("SELECT DISTINCT sp.department FROM StudentProfile sp")
    List<String> findDistinctDepartments();

    long countByDepartment(String department);
}
