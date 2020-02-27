package com.accenture.com.snoskov.adf.universityx.programs.repo.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.accenture.com.snoskov.adf.universityx.programs.model.AcademicDegree;
import com.accenture.com.snoskov.adf.universityx.programs.model.StudyProgram;
import com.accenture.com.snoskov.adf.universityx.programs.repo.ProgramDatabaseModel.AcademicDegreeTable;
import com.accenture.com.snoskov.adf.universityx.programs.repo.ProgramDatabaseModel.StudyProgramTable;
import org.springframework.jdbc.core.RowMapper;

public class StudyProgramRowMapper implements RowMapper<StudyProgram> {

    @Override
    public StudyProgram mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt(StudyProgramTable.PROGRAM_ID);
        String code = rs.getString(StudyProgramTable.PROGRAM_CODE);
        String description = rs.getString(StudyProgramTable.DESCRIPTION);
        Integer duration = rs.getInt(StudyProgramTable.DURATION);

        String degreeName = rs.getString(AcademicDegreeTable.DEGREE_NAME);
        String degreeType = rs.getString(AcademicDegreeTable.DEGREE_TYPE);
        AcademicDegree academicDegree = new AcademicDegree(degreeName, degreeType);

        return new StudyProgram(id, code, description, duration, academicDegree);
    }
}
