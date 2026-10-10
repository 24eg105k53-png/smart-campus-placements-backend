package com.smartcampus.placement.repository;

import com.smartcampus.placement.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudentProfileId(Long studentProfileId);
    List<Application> findByJobPostingId(Long jobPostingId);
    Optional<Application> findByStudentProfileIdAndJobPostingId(Long studentProfileId, Long jobPostingId);
    boolean existsByStudentProfileIdAndJobPostingId(Long studentProfileId, Long jobPostingId);
    long countByStatus(Application.ApplicationStatus status);
}
