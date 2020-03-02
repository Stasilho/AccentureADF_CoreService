package com.stanislavnoskov.adf.universityx.users.model;

import com.stanislavnoskov.adf.universityx.programs.model.StudyProgram;

public class ApplicationUser {

    private Integer id;

    private String username;
    private String password;
    private String email;
    private String token;

    private String firstName;
    private String lastName;

    private Role role;

    private String yearOfEntrance;
    private StudyProgram studyProgram;

    public ApplicationUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPersonIdentity(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getYearOfEntrance() {
        return yearOfEntrance;
    }

    public void setYearOfEntrance(String yearOfEntrance) {
        this.yearOfEntrance = yearOfEntrance;
    }

    public StudyProgram getStudyProgram() {
        return studyProgram;
    }

    public void setStudyProgram(StudyProgram studyProgram) {
        this.studyProgram = studyProgram;
    }
}
