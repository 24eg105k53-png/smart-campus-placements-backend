package com.smartcampus.placement.service;

import com.smartcampus.placement.dto.request.StudentProfileUpdateRequest;
import com.smartcampus.placement.dto.response.StudentProfileResponse;
import com.smartcampus.placement.integration.ai.dto.AiReadinessResponse;

import java.util.List;

public interface StudentProfileService {
    StudentProfileResponse getStudentProfile(Long userId);
    StudentProfileResponse updateStudentProfile(Long userId, StudentProfileUpdateRequest request);
    List<StudentProfileResponse.SkillItem> addSkillsToProfile(Long userId, List<String> skillNames);
    void removeSkillFromProfile(Long userId, Long skillId);
    AiReadinessResponse getStudentReadiness(Long userId);
}
