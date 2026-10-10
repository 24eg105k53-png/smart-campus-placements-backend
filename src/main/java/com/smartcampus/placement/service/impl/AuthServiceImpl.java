package com.smartcampus.placement.service.impl;

import com.smartcampus.placement.dto.request.LoginRequest;
import com.smartcampus.placement.dto.request.StudentRegisterRequest;
import com.smartcampus.placement.dto.response.AuthResponse;
import com.smartcampus.placement.dto.response.UserSummaryResponse;
import com.smartcampus.placement.entity.StudentProfile;
import com.smartcampus.placement.entity.User;
import com.smartcampus.placement.exception.BadRequestException;
import com.smartcampus.placement.exception.DuplicateResourceException;
import com.smartcampus.placement.repository.StudentProfileRepository;
import com.smartcampus.placement.repository.UserRepository;
import com.smartcampus.placement.security.JwtTokenProvider;
import com.smartcampus.placement.security.UserPrincipal;
import com.smartcampus.placement.service.AuthService;
import com.smartcampus.placement.util.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           StudentProfileRepository studentProfileRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String token = tokenProvider.generateToken(authentication);

        return new AuthResponse(
            token,
            userPrincipal.getId(),
            userPrincipal.getEmail(),
            userPrincipal.getFullName(),
            userPrincipal.getRole().name(),
            userPrincipal.getProfileId()
        );
    }

    @Override
    @Transactional
    public AuthResponse registerStudent(StudentRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("An account with email " + request.getEmail() + " already exists");
        }

        if (studentProfileRepository.existsByStudentRegistrationNumber(request.getStudentRegistrationNumber())) {
            throw new DuplicateResourceException("Registration number " + request.getStudentRegistrationNumber() + " is already registered");
        }

        User user = new User(
            request.getFullName(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            User.Role.STUDENT
        );
        user = userRepository.save(user);

        StudentProfile profile = new StudentProfile(
            user,
            request.getStudentRegistrationNumber(),
            request.getDepartment(),
            request.getGraduationYear(),
            request.getCgpa(),
            request.getActiveBacklogs()
        );
        profile = studentProfileRepository.save(profile);
        user.setStudentProfile(profile);

        String token = tokenProvider.generateTokenFromUserId(user.getId(), user.getEmail(), user.getRole().name());

        return new AuthResponse(
            token,
            user.getId(),
            user.getEmail(),
            user.getFullName(),
            user.getRole().name(),
            profile.getId()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserSummaryResponse getCurrentUserSummary() {
        UserPrincipal currentUser = SecurityUtils.getCurrentUser();
        User user = userRepository.findById(currentUser.getId())
            .orElseThrow(() -> new BadRequestException("User record not found"));

        Long profileId = null;
        if (user.getStudentProfile() != null) {
            profileId = user.getStudentProfile().getId();
        } else if (user.getCompany() != null) {
            profileId = user.getCompany().getId();
        }

        return new UserSummaryResponse(
            user.getId(),
            user.getEmail(),
            user.getFullName(),
            user.getRole().name(),
            user.getAccountStatus().name(),
            profileId
        );
    }
}
