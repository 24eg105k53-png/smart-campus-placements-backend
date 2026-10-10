package com.smartcampus.placement.security;

import com.smartcampus.placement.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private final Long id;
    private final String fullName;
    private final String email;
    private final String password;
    private final User.Role role;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Long profileId; // StudentProfile ID or Company ID if applicable

    public UserPrincipal(Long id, String fullName, String email, String password, User.Role role,
                         Collection<? extends GrantedAuthority> authorities, Long profileId) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.authorities = authorities;
        this.profileId = profileId;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        Long profileId = null;
        if (user.getStudentProfile() != null) {
            profileId = user.getStudentProfile().getId();
        } else if (user.getCompany() != null) {
            profileId = user.getCompany().getId();
        }

        return new UserPrincipal(
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getPasswordHash(),
            user.getRole(),
            authorities,
            profileId
        );
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public User.Role getRole() {
        return role;
    }

    public Long getProfileId() {
        return profileId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
