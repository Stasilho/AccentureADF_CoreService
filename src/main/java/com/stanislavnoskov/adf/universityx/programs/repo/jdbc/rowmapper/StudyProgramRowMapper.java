package com.stanislavnoskov.adf.universityx.programs.repo.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.stanislavnoskov.adf.universityx.programs.model.AcademicDegree;
import com.stanislavnoskov.adf.universityx.programs.model.StudyProgram;
import com.stanislavnoskov.adf.universityx.programs.repo.ProgramDatabaseModel;
import org.springframework.jdbc.core.RowMapper;

public class StudyProgramRowMapper implements RowMapper<StudyProgram> {

    @Override
    public StudyProgram mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt(ProgramDatabaseModel.StudyProgramTable.PROGRAM_ID);
        String code = rs.getString(ProgramDatabaseModel.StudyProgramTable.PROGRAM_CODE);
        String description = rs.getString(ProgramDatabaseModel.StudyProgramTable.DESCRIPTION);
        Integer duration = rs.getInt(ProgramDatabaseModel.StudyProgramTable.DURATION);

        String degreeName = rs.getString(ProgramDatabaseModel.AcademicDegreeTable.DEGREE_NAME);
        String degreeType = rs.getString(ProgramDatabaseModel.AcademicDegreeTable.DEGREE_TYPE);
        AcademicDegree academicDegree = new AcademicDegree(degreeName, degreeType);

        return new StudyProgram(id, code, description, duration, academicDegree);
    }
}
