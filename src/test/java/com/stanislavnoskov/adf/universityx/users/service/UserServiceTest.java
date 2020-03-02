package com.stanislavnoskov.adf.universityx.users.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.stanislavnoskov.adf.universityx.common.rest.model.BasicResponseDTO;
import com.stanislavnoskov.adf.universityx.programs.model.AcademicDegree;
import com.stanislavnoskov.adf.universityx.programs.model.StudyProgram;
import com.stanislavnoskov.adf.universityx.programs.repo.ProgramRepository;
import com.stanislavnoskov.adf.universityx.security.util.JwtTokenUtil;
import com.stanislavnoskov.adf.universityx.users.model.ApplicationUser;
import com.stanislavnoskov.adf.universityx.users.model.Role;
import com.stanislavnoskov.adf.universityx.users.model.UserSummary;
import com.stanislavnoskov.adf.universityx.users.repo.UserRepository;
import com.stanislavnoskov.adf.universityx.users.rest.model.AuthResponseDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private static String adminSecretToken = "a2d2aa3c-9e15-4457-aa22-783f15dc5ebd";

    private static UserRepository userRepository;
    private static ProgramRepository programRepository;
    private static AuthenticationManager authenticationManager;
    private static JwtTokenUtil jwtTokenUtil;

    private static UserService service;

    private ApplicationUser user;
    private Authentication authentication;

    @BeforeClass
    public static void initialize() {
        Environment env = Mockito.mock(Environment.class);
        userRepository = Mockito.mock(UserRepository.class);
        programRepository = Mockito.mock(ProgramRepository.class);
        BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        jwtTokenUtil = Mockito.mock(JwtTokenUtil.class);

        service = new UserService(env, userRepository, programRepository, bCryptPasswordEncoder, authenticationManager, jwtTokenUtil);

        when(env.getProperty("universityx.api.auth.admin.secret")).thenReturn(adminSecretToken);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("abc");
        //doNothing().when(userRepository).addNewUser(any());
    }

    @Before
    public void setUp() {
        user = new ApplicationUser("admin", "password");

        String authority = Role.USER.getName();
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(authority));
        authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), "", authorities);
    }

    @Test
    public void signUpWithWrongAdminToken() {
        user.setToken("wrongToken");
        ResponseEntity<BasicResponseDTO> responseEntity = service.signUp(user);
        BasicResponseDTO response = responseEntity.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals("Invalid token for admin user!", response.getMessage());
    }

    @Test
    public void signUpWithUserAlreadyExists() {
        user.setToken(adminSecretToken);
        when(userRepository.findByUsername(eq(user.getUsername()))).thenReturn(user);
        ResponseEntity<BasicResponseDTO> responseEntity = service.signUp(user);
        BasicResponseDTO response = responseEntity.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        String message = String.format("User '%s' already exists!", user.getUsername());
        assertEquals(message, response.getMessage());
    }

    @Test
    public void signUp() {
        user.setToken(adminSecretToken);
        user.setRole(Role.ADMIN);
        when(userRepository.findByUsername(eq(user.getUsername()))).thenReturn(null);
        ResponseEntity<BasicResponseDTO> responseEntity = service.signUp(user);
        BasicResponseDTO response = responseEntity.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        String message = String.format("User '%s' with %s role successfully created.", user.getUsername(), user.getRole().getName());
        assertEquals(message, response.getMessage());
    }

    @Test
    public void authenticate() {
        String jwtToken = "jwt_token";
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenUtil.generateToken(eq(user.getUsername()), eq(Role.USER.getName()))).thenReturn(jwtToken);

        ResponseEntity<AuthResponseDTO> responseEntity = service.authenticate(user);
        AuthResponseDTO response = responseEntity.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(jwtToken, response.getJwtToken());
    }

    @Test
    public void createUser() {
        user.setRole(Role.USER);
        when(userRepository.findByUsername(eq(user.getUsername()))).thenReturn(null);
        ResponseEntity<BasicResponseDTO> responseEntity = service.createUser(user);
        BasicResponseDTO response = responseEntity.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        String message = String.format("User '%s' with %s role successfully created.", user.getUsername(), user.getRole().getName());
        assertEquals(message, response.getMessage());
    }

    @Test
    public void retrieveUserByName() {
        when(userRepository.findByUsername(eq(user.getUsername()))).thenReturn(user);
        ApplicationUser existingUser = service.retrieveUserByName(user.getUsername());

        assertNotNull(existingUser);
        assertEquals(user.getUsername(), existingUser.getUsername());
    }

    @Test
    public void retrieveUsers() {
        Integer[] userIds = new Integer[] {1, 2, 3};
        ApplicationUser user1 = new ApplicationUser("user1", "password1");
        ApplicationUser user2 = new ApplicationUser("user2", "password2");
        ApplicationUser user3 = new ApplicationUser("user3", "password3");
        when(userRepository.findByIds(eq(Arrays.asList(userIds)))).thenReturn(Arrays.asList(user1, user2, user3));

        List<ApplicationUser> users = service.retrieveUsers(userIds);

        assertEquals(3, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user2", users.get(1).getUsername());
        assertEquals("user3", users.get(2).getUsername());
    }

    @Test
    public void retrieveUserSummary() {
        Integer[] userIds = new Integer[] {1, 2, 3};

        ApplicationUser user1 = new ApplicationUser("user1", "password1");
        user1.setStudyProgram(new StudyProgram(1));

        ApplicationUser user2 = new ApplicationUser("user2", "password2");
        user2.setStudyProgram(new StudyProgram(1));

        ApplicationUser user3 = new ApplicationUser("user3", "password3");
        user3.setStudyProgram(new StudyProgram(2));

        when(userRepository.findByIds(eq(Arrays.asList(userIds)))).thenReturn(Arrays.asList(user1, user2, user3));

        StudyProgram program1 = new StudyProgram(
                1, "CODE1", "description1", 3, new AcademicDegree("Bachelor", "academic"));
        StudyProgram program2 = new StudyProgram(
                2, "CODE2", "description2", 2, new AcademicDegree("Master", "professional"));
        when(programRepository.getByIds(eq(Arrays.asList(1, 2)))).thenReturn(Arrays.asList(program1, program2));


        UserSummary summary = service.retrieveUserSummary(userIds);

        assertNotNull(summary);
        assertEquals(3, summary.getUsers().size());
        assertEquals("user1", summary.getUsers().get(0).getUsername());
        assertEquals("user2", summary.getUsers().get(1).getUsername());
        assertEquals("user3", summary.getUsers().get(2).getUsername());

        assertEquals(2, summary.getStudyPrograms().size());
        Assert.assertEquals("CODE1", summary.getStudyPrograms().get(0).getProgramCode());
        Assert.assertEquals("CODE2", summary.getStudyPrograms().get(1).getProgramCode());
    }
}