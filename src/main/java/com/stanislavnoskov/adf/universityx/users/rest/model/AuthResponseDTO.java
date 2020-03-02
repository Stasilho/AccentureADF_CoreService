package com.stanislavnoskov.adf.universityx.users.rest.model;

import org.springframework.http.HttpStatus;

public class AuthResponseDTO {

    private HttpStatus status;
    private String message;
    private String role;
    private String jwtToken;

    public AuthResponseDTO(HttpStatus status, String message, String role, String jwtToken) {
        this.status = status;
        this.message = message;
        this.role = role;
        this.jwtToken = jwtToken;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
