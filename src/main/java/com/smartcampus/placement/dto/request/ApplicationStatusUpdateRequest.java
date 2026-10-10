package com.smartcampus.placement.dto.request;

import com.smartcampus.placement.entity.Application;
import jakarta.validation.constraints.NotNull;

public class ApplicationStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private Application.ApplicationStatus status;

    private String recruiterNotes;

    public ApplicationStatusUpdateRequest() {}

    public ApplicationStatusUpdateRequest(Application.ApplicationStatus status, String recruiterNotes) {
        this.status = status;
        this.recruiterNotes = recruiterNotes;
    }

    public Application.ApplicationStatus getStatus() { return status; }
    public void setStatus(Application.ApplicationStatus status) { this.status = status; }

    public String getRecruiterNotes() { return recruiterNotes; }
    public void setRecruiterNotes(String recruiterNotes) { this.recruiterNotes = recruiterNotes; }
}
