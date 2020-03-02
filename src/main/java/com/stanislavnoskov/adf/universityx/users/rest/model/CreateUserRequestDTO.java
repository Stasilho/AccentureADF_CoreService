package com.stanislavnoskov.adf.universityx.users.rest.model;

public class CreateUserRequestDTO {

    private String studentId;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Integer programId;
    private String yearOfEntrance;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public String getYearOfEntrance() {
        return yearOfEntrance;
    }

    public void setYearOfEntrance(String yearOfEntrance) {
        this.yearOfEntrance = yearOfEntrance;
    }
}
