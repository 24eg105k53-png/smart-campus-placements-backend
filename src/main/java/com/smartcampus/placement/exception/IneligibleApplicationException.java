package com.smartcampus.placement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class IneligibleApplicationException extends RuntimeException {

    private final List<String> reasons;

    public IneligibleApplicationException(String message, List<String> reasons) {
        super(message);
        this.reasons = reasons;
    }

    public List<String> getReasons() {
        return reasons;
    }
}
