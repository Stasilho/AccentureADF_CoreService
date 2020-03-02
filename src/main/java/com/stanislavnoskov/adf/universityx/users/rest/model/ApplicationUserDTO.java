package com.stanislavnoskov.adf.universityx.users.rest.model;

import com.stanislavnoskov.adf.universityx.programs.rest.model.StudyProgramDTO;

public class ApplicationUserDTO {

    private Integer userId;
    private String username;
    private String firstName;
    private String lastName;
    private Integer programId;
    private StudyProgramDTO studyProgram;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public StudyProgramDTO getStudyProgram() {
        return studyProgram;
    }

    public void setStudyProgram(StudyProgramDTO studyProgram) {
        this.studyProgram = studyProgram;
    }
}
