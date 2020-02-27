package com.accenture.com.snoskov.adf.universityx.programs.exception;

public class StudyProgramNotFoundException extends RuntimeException {

    public StudyProgramNotFoundException(String programCode) {
        super(String.format("Program '%s' doesn't exists", programCode));
    }

    public StudyProgramNotFoundException(Integer id) {
        super(String.format("Program with id: '%d' doesn't exists", id));
    }
}
