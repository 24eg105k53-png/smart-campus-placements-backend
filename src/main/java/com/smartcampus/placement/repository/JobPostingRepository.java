package com.smartcampus.placement.repository;

import com.smartcampus.placement.entity.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    Page<JobPosting> findByStatus(JobPosting.JobStatus status, Pageable pageable);
    List<JobPosting> findByCompanyId(Long companyId);

    @Query("SELECT j FROM JobPosting j WHERE j.status = 'PUBLISHED' " +
           "AND (:search IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(j.company.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<JobPosting> searchActiveJobs(@Param("search") String search, Pageable pageable);

    long countByStatus(JobPosting.JobStatus status);
}
