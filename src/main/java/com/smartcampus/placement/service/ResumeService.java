package com.smartcampus.placement.service;

import com.smartcampus.placement.entity.ResumeAnalysis;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ResumeService {
    ResumeAnalysis uploadAndParseResume(Long studentUserId, MultipartFile file);
    ResumeAnalysis getLatestResumeAnalysis(Long studentUserId);
    Resource loadResumeAsResource(Long studentUserId);
}
