package com.smartcampus.placement.service;

import com.smartcampus.placement.dto.request.LoginRequest;
import com.smartcampus.placement.dto.request.StudentRegisterRequest;
import com.smartcampus.placement.dto.response.AuthResponse;
import com.smartcampus.placement.dto.response.UserSummaryResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse registerStudent(StudentRegisterRequest request);
    UserSummaryResponse getCurrentUserSummary();
}
