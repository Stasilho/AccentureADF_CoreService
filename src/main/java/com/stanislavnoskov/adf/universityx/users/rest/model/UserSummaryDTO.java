package com.stanislavnoskov.adf.universityx.users.rest.model;

import java.util.List;

import com.stanislavnoskov.adf.universityx.programs.rest.model.StudyProgramDTO;

public class UserSummaryDTO {
    private List<ApplicationUserDTO> users;
    private List<StudyProgramDTO> studyPrograms;

    public UserSummaryDTO(List<ApplicationUserDTO> users, List<StudyProgramDTO> studyPrograms) {
        this.users = users;
        this.studyPrograms = studyPrograms;
    }

    public List<ApplicationUserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<ApplicationUserDTO> users) {
        this.users = users;
    }

    public List<StudyProgramDTO> getStudyPrograms() {
        return studyPrograms;
    }

    public void setStudyPrograms(List<StudyProgramDTO> studyPrograms) {
        this.studyPrograms = studyPrograms;
    }
}
