package com.accenture.com.snoskov.adf.universityx.users.rest;

import java.util.regex.Pattern;

import com.accenture.com.snoskov.adf.universityx.users.exception.BadAuthRequestException;
import com.accenture.com.snoskov.adf.universityx.users.rest.model.CreateUserRequestDTO;
import com.accenture.com.snoskov.adf.universityx.users.rest.model.SignUpRequestDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UserRequestValidator {

    private Pattern userNamePattern;
    private Pattern emailPattern;
    private Pattern namePattern;

    public UserRequestValidator() {
        userNamePattern = Pattern.compile("[A-Za-z0-9_]+");
        emailPattern = Pattern.compile("^(.+)@(.+)$");
        namePattern = Pattern.compile("[A-Za-z]+");
    }

    public void validateSignUpRequest(SignUpRequestDTO signUpRequestDTO) {
        validateUsername(signUpRequestDTO.getUsername());
        validatePassword(signUpRequestDTO.getPassword());
        validateEmail(signUpRequestDTO.getEmail());

        validateName(signUpRequestDTO.getFirstName(), "first name");
        validateName(signUpRequestDTO.getLastName(), "last name");
    }

    public void validateCreateUserRequest(CreateUserRequestDTO createRequestDTO) {
        validateUsername(createRequestDTO.getStudentId());
        validatePassword(createRequestDTO.getPassword());
        validateEmail(createRequestDTO.getEmail());

        validateName(createRequestDTO.getFirstName(), "first name");
        validateName(createRequestDTO.getLastName(), "last name");
    }

    private void validateUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new BadAuthRequestException("Username is empty!");
        } else if (username.length() < 6) {
            throw new BadAuthRequestException("Username must have at least 6 characters.");
        } else if (username.length() > 30) {
            throw new BadAuthRequestException("Username length can't exceed 30 characters.");
        } else if (!userNamePattern.matcher(username).matches()) {
            throw new BadAuthRequestException("Username contains invalid characters");
        }
    }

    private void validatePassword(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new BadAuthRequestException("Password is empty!");
        } else if (password.length() < 6) {
            throw new BadAuthRequestException("Password must have at least 6 characters.");
        } else if (password.length() > 30) {
            throw new BadAuthRequestException("Password length can't exceed 30 characters.");
        }
        // TODO: Require password to have some numbers or special chars
    }

    private void validateEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            throw new BadAuthRequestException("Email is empty!");
        } else if (!emailPattern.matcher(email).matches()) {
            throw new BadAuthRequestException("Email looks suspicious.");
        }
        // TODO: Maybe require some specific domain
    }

    private void validateName(String name, String prefix) {
        if (StringUtils.isEmpty(name)) {
            throw new BadAuthRequestException(prefix + " is empty!");
        } else if (name.length() < 2) {
            throw new BadAuthRequestException(prefix + " must have at least 2 characters.");
        } else if (name.length() > 30) {
            throw new BadAuthRequestException(prefix + " length can't exceed 30 characters.");
        } else if (!namePattern.matcher(name).matches()) {
            throw new BadAuthRequestException(prefix + " contains invalid characters");
        }
    }
}
