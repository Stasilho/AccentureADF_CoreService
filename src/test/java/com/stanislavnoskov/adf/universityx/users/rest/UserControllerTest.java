package com.stanislavnoskov.adf.universityx.users.rest;

import com.stanislavnoskov.adf.universityx.users.model.ApplicationUser;
import com.stanislavnoskov.adf.universityx.users.rest.mapper.UserMapper;
import com.stanislavnoskov.adf.universityx.users.rest.model.AuthRequestDTO;
import com.stanislavnoskov.adf.universityx.users.rest.model.CreateUserRequestDTO;
import com.stanislavnoskov.adf.universityx.users.rest.model.SignUpRequestDTO;
import com.stanislavnoskov.adf.universityx.users.service.UserService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private static UserController controller;
    private static UserMapper userMapper;
    private static UserService service;

    @BeforeClass
    public static void initialize() {
        service = Mockito.mock(UserService.class);
        UserRequestValidator requestValidator = Mockito.mock(UserRequestValidator.class);
        userMapper = Mockito.mock(UserMapper.class);
        controller = new UserController(requestValidator, service, userMapper);

        doNothing().when(requestValidator).validateSignUpRequest(any());
    }

    @Test
    public void testSignUp() {
        SignUpRequestDTO requestDTO = new SignUpRequestDTO();
        requestDTO.setUsername("admin");
        requestDTO.setPassword("password");
        ApplicationUser user = new ApplicationUser("admin", "password");
        when(userMapper.userFrom(requestDTO)).thenReturn(user);
        when(service.signUp(user)).thenReturn(any());

        controller.signUp(requestDTO);

        verify(service, times(1)).signUp(any(ApplicationUser.class));
    }

    @Test
    public void testAuthenticate() {
        AuthRequestDTO requestDTO = new AuthRequestDTO();
        requestDTO.setUsername("admin");
        requestDTO.setPassword("password");
        ApplicationUser user = new ApplicationUser("admin", "password");
        when(userMapper.userFrom(requestDTO)).thenReturn(user);
        when(service.authenticate(user)).thenReturn(any());

        controller.authenticate(requestDTO);

        verify(service, times(1)).authenticate(any(ApplicationUser.class));
    }

    @Test
    public void testCreateUser() {
        CreateUserRequestDTO requestDTO = new CreateUserRequestDTO();
        requestDTO.setStudentId("ABC");
        requestDTO.setPassword("password");
        ApplicationUser user = new ApplicationUser("ABC", "password");
        when(userMapper.userFrom(requestDTO)).thenReturn(user);
        when(service.createUser(user)).thenReturn(any());

        controller.createUser(requestDTO);

        verify(service, times(1)).createUser(any(ApplicationUser.class));
    }

    @Test
    public void testRetrieveUserByName() {
        when(service.retrieveUserByName("username")).thenReturn(any());

        controller.retrieveUserByName("username");

        verify(service, times(1)).retrieveUserByName("username");
    }

    @Test
    public void testRetrieveUsers() {
        Integer[] usersIds = new Integer[] {1, 2, 3};
        when(service.retrieveUsers(usersIds)).thenReturn(any());

        controller.retrieveUsers(usersIds);

        verify(service, times(1)).retrieveUsers(usersIds);
    }

    @Test
    public void testRetrieveUserSummary() {
        Integer[] usersIds = new Integer[] {1, 2, 3};
        when(service.retrieveUserSummary(usersIds)).thenReturn(any());

        controller.retrieveUserSummary(usersIds);

        verify(service, times(1)).retrieveUserSummary(usersIds);
    }
}