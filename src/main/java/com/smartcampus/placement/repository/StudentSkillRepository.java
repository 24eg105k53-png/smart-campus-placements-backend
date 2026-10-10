package com.smartcampus.placement.repository;

import com.smartcampus.placement.entity.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentSkillRepository extends JpaRepository<StudentSkill, StudentSkill.StudentSkillId> {
    List<StudentSkill> findByStudentProfileId(Long studentProfileId);
    void deleteByStudentProfileIdAndSkillId(Long studentProfileId, Long skillId);
}
