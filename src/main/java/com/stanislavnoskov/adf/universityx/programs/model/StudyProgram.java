package com.stanislavnoskov.adf.universityx.programs.model;

import org.apache.commons.lang3.StringUtils;

public class StudyProgram {

    private final Integer id;
    private final String programCode;
    private final String description;
    private final Integer duration;
    private final AcademicDegree academicDegree;

    public StudyProgram(Integer id) {
        this(id, null, null, null, null);
    }

    public StudyProgram(Integer id, String programCode, String description, Integer duration, AcademicDegree academicDegree) {
        this.id = id;
        this.programCode = programCode;
        this.description = description;
        this.duration = duration;
        this.academicDegree = academicDegree;
    }

    public Integer getId() {
        return id;
    }

    public String getProgramCode() {
        return programCode;
    }

    public String getDescription() {
        return description;
    }

    public Integer getDuration() {
        return duration;
    }

    public AcademicDegree getAcademicDegree() {
        return academicDegree;
    }

    public boolean isEmpty() {
        return StringUtils.isBlank(this.programCode)
                || StringUtils.isBlank(this.description)
                || this.duration == null
                || this.academicDegree == null;
    }
}
