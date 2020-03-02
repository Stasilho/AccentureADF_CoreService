package com.stanislavnoskov.adf.universityx.users.rest;

import java.util.List;

import com.stanislavnoskov.adf.universityx.users.exception.BadAuthRequestException;
import com.stanislavnoskov.adf.universityx.users.rest.mapper.UserMapper;
import com.stanislavnoskov.adf.universityx.users.rest.model.ApplicationUserDTO;
import com.stanislavnoskov.adf.universityx.users.rest.model.AuthRequestDTO;
import com.stanislavnoskov.adf.universityx.users.rest.model.AuthResponseDTO;
import com.stanislavnoskov.adf.universityx.users.rest.model.CreateUserRequestDTO;
import com.stanislavnoskov.adf.universityx.users.rest.model.SignUpRequestDTO;
import com.stanislavnoskov.adf.universityx.common.rest.model.BasicResponseDTO;
import com.stanislavnoskov.adf.universityx.users.rest.model.UserSummaryDTO;
import com.stanislavnoskov.adf.universityx.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apis/users")
public class UserController {

    private UserRequestValidator userRequestValidator;
    private UserService userService;
    private UserMapper userMapper;

    @Autowired
    public UserController(UserRequestValidator userRequestValidator, UserService userService, UserMapper userMapper) {
        this.userRequestValidator = userRequestValidator;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<BasicResponseDTO> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        try {
            userRequestValidator.validateSignUpRequest(signUpRequestDTO);
        } catch (BadAuthRequestException exc) {
            BasicResponseDTO response = new BasicResponseDTO(HttpStatus.BAD_REQUEST, exc.getMessage());
            return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
        }
        return userService.signUp(userMapper.userFrom(signUpRequestDTO));
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody AuthRequestDTO authRequestDTO) {
        return userService.authenticate(userMapper.userFrom(authRequestDTO));
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<BasicResponseDTO> createUser(@RequestBody CreateUserRequestDTO createUserRequestDTO) {
        try {
            userRequestValidator.validateCreateUserRequest(createUserRequestDTO);
        } catch (BadAuthRequestException exc) {
            BasicResponseDTO response = new BasicResponseDTO(HttpStatus.BAD_REQUEST, exc.getMessage());
            return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
        }
        return userService.createUser(userMapper.userFrom(createUserRequestDTO));
    }

    @GetMapping(value = "/{username}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ApplicationUserDTO retrieveUserByName(@PathVariable String username) {
        return userMapper.applicationUserToDto(userService.retrieveUserByName(username));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping
    public List<ApplicationUserDTO> retrieveUsers(@RequestParam(name = "id") Integer[] usersIds) {
        return userMapper.applicationUsersToDTOs(userService.retrieveUsers(usersIds));
    }

    @GetMapping(value = "/summary")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public UserSummaryDTO retrieveUserSummary(@RequestParam(name = "id") Integer[] usersIds) {
        return userMapper.userSummaryToDto(userService.retrieveUserSummary(usersIds));
    }
}
