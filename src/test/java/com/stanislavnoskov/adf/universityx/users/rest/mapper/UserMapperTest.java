package com.stanislavnoskov.adf.universityx.users.rest.mapper;

import java.util.Arrays;
import java.util.List;

import com.stanislavnoskov.adf.universityx.programs.model.StudyProgram;
import com.stanislavnoskov.adf.universityx.programs.rest.mapper.ProgramMapper;
import com.stanislavnoskov.adf.universityx.users.model.ApplicationUser;
import com.stanislavnoskov.adf.universityx.users.rest.model.ApplicationUserDTO;
import com.stanislavnoskov.adf.universityx.users.rest.model.AuthRequestDTO;
import com.stanislavnoskov.adf.universityx.users.rest.model.CreateUserRequestDTO;
import com.stanislavnoskov.adf.universityx.users.rest.model.SignUpRequestDTO;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserMapperTest {

    private static UserMapper userMapper;

    @BeforeClass
    public static void initialize() {
        ProgramMapper programMapper = new ProgramMapper();
        userMapper = new UserMapper(programMapper);
    }

    @Test
    public void testUserFromSignUpRequest() {
        SignUpRequestDTO requestDTO = new SignUpRequestDTO();
        requestDTO.setUsername("admin");
        requestDTO.setPassword("password");
        requestDTO.setFirstName("Fedor");
        requestDTO.setLastName("Sumkin");
        requestDTO.setToken("token");
        requestDTO.setEmail("admin@user.com");

        ApplicationUser user = userMapper.userFrom(requestDTO);

        assertEquals("admin", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("Fedor", user.getFirstName());
        assertEquals("Sumkin", user.getLastName());
        assertEquals("token", user.getToken());
        assertEquals("admin@user.com", user.getEmail());
    }

    @Test
    public void testUserFromAuthRequest() {
        AuthRequestDTO requestDTO = new AuthRequestDTO();
        requestDTO.setUsername("admin");
        requestDTO.setPassword("password");

        ApplicationUser user = userMapper.userFrom(requestDTO);

        assertEquals("admin", user.getUsername());
        assertEquals("password", user.getPassword());
    }
    @Test
    public void testUserFromCreateUserRequest() {
        CreateUserRequestDTO requestDTO = new CreateUserRequestDTO();
        requestDTO.setStudentId("admin");
        requestDTO.setPassword("password");
        requestDTO.setFirstName("Fedor");
        requestDTO.setLastName("Sumkin");
        requestDTO.setEmail("admin@user.com");
        requestDTO.setYearOfEntrance("2020");
        requestDTO.setProgramId(5);

        ApplicationUser user = userMapper.userFrom(requestDTO);

        assertEquals("admin", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("Fedor", user.getFirstName());
        assertEquals("Sumkin", user.getLastName());
        assertEquals("admin@user.com", user.getEmail());
        assertEquals("2020", user.getYearOfEntrance());

        assertNotNull(user.getStudyProgram());
        Assert.assertEquals(Integer.valueOf(5), user.getStudyProgram().getId());
    }

    @Test
    public void testApplicationUserToDto() {
        ApplicationUser user = new ApplicationUser("admin", "password");
        user.setId(1);
        user.setPersonIdentity("Fedor", "Sumkin");
        user.setStudyProgram(new StudyProgram(1));
        user.setYearOfEntrance("2020");

        ApplicationUserDTO userDTO = userMapper.applicationUserToDto(user);

        assertEquals(Integer.valueOf(1), userDTO.getUserId());
        assertEquals("admin", userDTO.getUsername());
        assertEquals("Fedor", userDTO.getFirstName());
        assertEquals("Sumkin", userDTO.getLastName());

        assertNull(userDTO.getStudyProgram());
        assertNotNull(userDTO.getProgramId());
        assertEquals(Integer.valueOf(1), userDTO.getProgramId());
    }

    @Test
    public void testApplicationUsersToDtos() {
        ApplicationUser user1 = new ApplicationUser("admin", "password");
        user1.setId(1);
        user1.setPersonIdentity("Fedor", "Sumkin");
        user1.setStudyProgram(new StudyProgram(1));
        user1.setYearOfEntrance("2019");

        ApplicationUser user2 = new ApplicationUser("student", "password2");
        user2.setId(2);
        user2.setPersonIdentity("Vasja", "Pupkin");
        user2.setStudyProgram(new StudyProgram(2));
        user2.setYearOfEntrance("2020");

        List<ApplicationUserDTO> usersDTO = userMapper.applicationUsersToDTOs(Arrays.asList(user1, user2));

        assertEquals(2, usersDTO.size());

        assertEquals("admin", usersDTO.get(0).getUsername());
        assertEquals("student", usersDTO.get(1).getUsername());
    }
}