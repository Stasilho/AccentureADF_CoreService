package com.accenture.com.snoskov.adf.universityx.users.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.accenture.com.snoskov.adf.universityx.programs.model.StudyProgram;
import com.accenture.com.snoskov.adf.universityx.programs.repo.ProgramRepository;
import com.accenture.com.snoskov.adf.universityx.users.model.ApplicationUser;
import com.accenture.com.snoskov.adf.universityx.users.model.Role;
import com.accenture.com.snoskov.adf.universityx.users.model.UserSummary;
import com.accenture.com.snoskov.adf.universityx.users.repo.UserRepository;
import com.accenture.com.snoskov.adf.universityx.users.rest.model.AuthResponseDTO;
import com.accenture.com.snoskov.adf.universityx.common.rest.model.BasicResponseDTO;
import com.accenture.com.snoskov.adf.universityx.security.util.JwtTokenUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@Component
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final Environment env;
    private final UserRepository userRepository;
    private final ProgramRepository programRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserService(
            Environment env,
            UserRepository userRepository,
            ProgramRepository programRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenUtil jwtTokenUtil) {
        this.env = env;
        this.userRepository = userRepository;
        this.programRepository = programRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /*
     Sign up only allowed for ADMIN users
     */
    public ResponseEntity<BasicResponseDTO> signUp(ApplicationUser user) {
        String adminToken = env.getProperty("universityx.api.auth.admin.secret");
        Objects.requireNonNull(adminToken);
        BasicResponseDTO response;

        if (adminToken.equals(user.getToken())) {
            user.setRole(Role.ADMIN);
            response = createApplicationUser(user);
        } else {
            response = new BasicResponseDTO(HttpStatus.BAD_REQUEST, "Invalid token for admin user!");
        }
        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }

    public ResponseEntity<AuthResponseDTO> authenticate(ApplicationUser user) {
        Authentication authentication;
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            authentication = authenticationManager.authenticate(token);
        } catch (AuthenticationException exc) {
            String message = String.format("Authentication failed for user: %s", user.getUsername());
            LOGGER.warn(message);
            AuthResponseDTO response = new AuthResponseDTO(HttpStatus.BAD_REQUEST, message, null, null);
            return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
        }

        final String authority = authentication.getAuthorities().stream()
                                                                .findFirst()
                                                                .map(GrantedAuthority::getAuthority)
                                                                .orElse("");
        final String jwtToken = jwtTokenUtil.generateToken(authentication.getName(), authority);

        AuthResponseDTO response = new AuthResponseDTO(HttpStatus.OK, "success", authority, jwtToken);
        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }

    public ResponseEntity<BasicResponseDTO> createUser(ApplicationUser user) {
        user.setRole(Role.USER);
        BasicResponseDTO response = createApplicationUser(user);
        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }

    private BasicResponseDTO createApplicationUser(ApplicationUser user) {
        if (userRepository.findByUsername(user.getUsername()) == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.addNewUser(user);
            String message = String.format("User '%s' with %s role successfully created.", user.getUsername(), user.getRole().getName());
            return new BasicResponseDTO(HttpStatus.OK, message);
        } else {
            String message = String.format("User '%s' already exists!", user.getUsername());
            return new BasicResponseDTO(HttpStatus.BAD_REQUEST, message);
        }
    }

    public ApplicationUser retrieveUserByName(String username) {
        ApplicationUser user = userRepository.findByUsername(username);

        if (user.getStudyProgram() != null) {
            StudyProgram program = programRepository.getById(user.getStudyProgram().getId());
            user.setStudyProgram(program);
        }
        return user;
    }

    public List<ApplicationUser> retrieveUsers(Integer[] usersIds) {
        return userRepository.findByIds(Arrays.asList(usersIds));
    }

    public UserSummary retrieveUserSummary(Integer[] usersIds) {
        List<ApplicationUser> users = userRepository.findByIds(Arrays.asList(usersIds));
        List<Integer> programIds = users.stream()
                                        .filter(user -> user.getStudyProgram() != null)
                                        .map(user -> user.getStudyProgram().getId())
                                        .distinct()
                                        .sorted()
                                        .collect(toList());

        List<StudyProgram> programs = programRepository.getByIds(programIds);

        return new UserSummary(users, programs);
    }
}
