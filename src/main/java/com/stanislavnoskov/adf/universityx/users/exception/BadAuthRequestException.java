package com.stanislavnoskov.adf.universityx.users.exception;

public class BadAuthRequestException extends RuntimeException {

    public BadAuthRequestException(String message) {
        super(message);
    }
}
