package com.smartcampus.placement.repository;

import com.smartcampus.placement.entity.PlacementRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PlacementRecordRepository extends JpaRepository<PlacementRecord, Long> {
    List<PlacementRecord> findByPlacementYear(Integer placementYear);
    List<PlacementRecord> findByCompanyId(Long companyId);
    boolean existsByStudentProfileId(Long studentProfileId);

    @Query("SELECT AVG(pr.ctcAmount) FROM PlacementRecord pr")
    BigDecimal findAverageCtc();

    @Query("SELECT MAX(pr.ctcAmount) FROM PlacementRecord pr")
    BigDecimal findHighestCtc();

    @Query("SELECT pr.studentProfile.department, AVG(pr.ctcAmount), COUNT(pr) " +
           "FROM PlacementRecord pr GROUP BY pr.studentProfile.department")
    List<Object[]> findBranchWisePlacementStats();
}
