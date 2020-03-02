package com.stanislavnoskov.adf.universityx.common.rest.model;

import org.springframework.http.HttpStatus;

public class BasicResponseDTO {

    private HttpStatus status;
    private String message;

    public BasicResponseDTO(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
