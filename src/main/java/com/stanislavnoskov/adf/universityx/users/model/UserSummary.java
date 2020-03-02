package com.stanislavnoskov.adf.universityx.users.model;

import java.util.List;

import com.stanislavnoskov.adf.universityx.programs.model.StudyProgram;

public class UserSummary {

    private List<ApplicationUser> users;
    private List<StudyProgram> studyPrograms;

    public UserSummary(List<ApplicationUser> users, List<StudyProgram> studyPrograms) {
        this.users = users;
        this.studyPrograms = studyPrograms;
    }

    public List<ApplicationUser> getUsers() {
        return users;
    }

    public List<StudyProgram> getStudyPrograms() {
        return studyPrograms;
    }
}
