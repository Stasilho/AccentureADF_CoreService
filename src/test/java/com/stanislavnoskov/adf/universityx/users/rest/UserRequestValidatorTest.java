package com.stanislavnoskov.adf.universityx.users.rest;

import com.stanislavnoskov.adf.universityx.users.exception.BadAuthRequestException;
import com.stanislavnoskov.adf.universityx.users.rest.model.CreateUserRequestDTO;
import com.stanislavnoskov.adf.universityx.users.rest.model.SignUpRequestDTO;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserRequestValidatorTest {

    private static UserRequestValidator validator;

    private SignUpRequestDTO signUpRequest;
    private CreateUserRequestDTO createUserRequest;

    @BeforeClass
    public static void initialize() {
        validator = new UserRequestValidator();
    }

    @Before
    public void setUp() {
        signUpRequest = new SignUpRequestDTO();
        signUpRequest.setUsername("admin_123");
        signUpRequest.setPassword("abcde123");
        signUpRequest.setFirstName("Fedor");
        signUpRequest.setLastName("Sumkin");
        signUpRequest.setEmail("user@admin.com");

        createUserRequest = new CreateUserRequestDTO();
        createUserRequest.setStudentId("admin_123");
        createUserRequest.setPassword("abcde123");
        createUserRequest.setFirstName("Fedor");
        createUserRequest.setLastName("Sumkin");
        createUserRequest.setEmail("user@admin.com");
    }

    @Test
    public void testValidateSignUpRequestWhenOK() {
        validator.validateSignUpRequest(signUpRequest);
    }

    @Test(expected = BadAuthRequestException.class)
    public void testValidateSignUpRequestWhenUsernameEmpty() {
        signUpRequest.setUsername(null);
        validator.validateSignUpRequest(signUpRequest);
    }

    @Test(expected = BadAuthRequestException.class)
    public void testValidateSignUpRequestWhenUserNameToShort() {
        signUpRequest.setUsername("abc");
        validator.validateSignUpRequest(signUpRequest);
    }

    @Test(expected = BadAuthRequestException.class)
    public void testValidateSignUpRequestWhenUserHasWrongChars() {
        signUpRequest.setUsername("abc.-/12");
        validator.validateSignUpRequest(signUpRequest);
    }

    @Test(expected = BadAuthRequestException.class)
    public void testValidateSignUpRequestWhenPasswordEmpty() {
        signUpRequest.setPassword(null);
        validator.validateSignUpRequest(signUpRequest);
    }

    @Test(expected = BadAuthRequestException.class)
    public void testValidateSignUpRequestWhenPasswordToShort() {
        signUpRequest.setPassword("123");
        validator.validateSignUpRequest(signUpRequest);
    }

    @Test(expected = BadAuthRequestException.class)
    public void testValidateSignUpRequestWhenBadFirstName() {
        signUpRequest.setFirstName("1user");
        validator.validateSignUpRequest(signUpRequest);
    }

    @Test(expected = BadAuthRequestException.class)
    public void testValidateSignUpRequestWhenBadLastName() {
        signUpRequest.setLastName("_lastname");
        validator.validateSignUpRequest(signUpRequest);
    }

    @Test(expected = BadAuthRequestException.class)
    public void testValidateSignUpRequestWhenBadEmail() {
        signUpRequest.setEmail("email.com");
        validator.validateSignUpRequest(signUpRequest);
    }

    @Test
    public void validateCreateUserRequest() {
        validator.validateCreateUserRequest(createUserRequest);
    }
}