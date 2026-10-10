package com.smartcampus.placement.dto.response;

public class UserSummaryResponse {

    private Long id;
    private String email;
    private String fullName;
    private String role;
    private String accountStatus;
    private Long profileId;

    public UserSummaryResponse() {}

    public UserSummaryResponse(Long id, String email, String fullName, String role, String accountStatus, Long profileId) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.accountStatus = accountStatus;
        this.profileId = profileId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }

    public Long getProfileId() { return profileId; }
    public void setProfileId(Long profileId) { this.profileId = profileId; }
}
