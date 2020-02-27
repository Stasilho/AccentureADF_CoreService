package com.accenture.com.snoskov.adf.universityx.users.rest.mapper;

import java.util.List;

import com.accenture.com.snoskov.adf.universityx.programs.rest.mapper.ProgramMapper;
import com.accenture.com.snoskov.adf.universityx.programs.rest.model.StudyProgramDTO;
import com.accenture.com.snoskov.adf.universityx.users.model.ApplicationUser;
import com.accenture.com.snoskov.adf.universityx.users.model.Role;
import com.accenture.com.snoskov.adf.universityx.programs.model.StudyProgram;
import com.accenture.com.snoskov.adf.universityx.users.model.UserSummary;
import com.accenture.com.snoskov.adf.universityx.users.rest.model.ApplicationUserDTO;
import com.accenture.com.snoskov.adf.universityx.users.rest.model.AuthRequestDTO;
import com.accenture.com.snoskov.adf.universityx.users.rest.model.CreateUserRequestDTO;
import com.accenture.com.snoskov.adf.universityx.users.rest.model.SignUpRequestDTO;
import com.accenture.com.snoskov.adf.universityx.users.rest.model.UserSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@Component
public class UserMapper {

    private ProgramMapper programMapper;

    @Autowired
    public UserMapper(ProgramMapper programMapper) {
        this.programMapper = programMapper;
    }

    public ApplicationUser userFrom(SignUpRequestDTO signUpRequestDTO) {
        ApplicationUser user = new ApplicationUser(signUpRequestDTO.getUsername(), signUpRequestDTO.getPassword());
        user.setPersonIdentity(signUpRequestDTO.getFirstName(), signUpRequestDTO.getLastName());
        user.setEmail(signUpRequestDTO.getEmail());
        user.setToken(signUpRequestDTO.getToken());
        return user;
    }

    public ApplicationUser userFrom(AuthRequestDTO authRequestDTO) {
        return new ApplicationUser(authRequestDTO.getUsername(), authRequestDTO.getPassword());
    }

    public ApplicationUser userFrom(CreateUserRequestDTO createUserRequestDTO) {
        ApplicationUser user = new ApplicationUser(createUserRequestDTO.getStudentId(), createUserRequestDTO.getPassword());
        user.setEmail(createUserRequestDTO.getEmail());
        user.setPersonIdentity(createUserRequestDTO.getFirstName(), createUserRequestDTO.getLastName());
        user.setRole(Role.USER);
        user.setYearOfEntrance(createUserRequestDTO.getYearOfEntrance());

        StudyProgram program = new StudyProgram(createUserRequestDTO.getProgramId());
        user.setStudyProgram(program);

        return user;
    }

    public ApplicationUserDTO applicationUserToDto(ApplicationUser user) {
        ApplicationUserDTO userDTO = new ApplicationUserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUserId(user.getId());
        userDTO.setUsername(user.getUsername());

        StudyProgram program = user.getStudyProgram();
        if (program != null) {
            if (program.isEmpty()) {
                userDTO.setProgramId(program.getId());
            } else {
                userDTO.setStudyProgram(programMapper.studyProgramToDto(program));
            }
        }

        return userDTO;
    }

    public List<ApplicationUserDTO> applicationUsersToDTOs(List<ApplicationUser> users) {
        return users.stream()
                    .map(this::applicationUserToDto)
                    .collect(toList());
    }

    public UserSummaryDTO userSummaryToDto(UserSummary userSummary) {
        List<ApplicationUserDTO> userDTOs = applicationUsersToDTOs(userSummary.getUsers());
        List<StudyProgramDTO> programDTOs = programMapper.studyProgramsToDTOs(userSummary.getStudyPrograms());

        return new UserSummaryDTO(userDTOs, programDTOs);
    }
}
