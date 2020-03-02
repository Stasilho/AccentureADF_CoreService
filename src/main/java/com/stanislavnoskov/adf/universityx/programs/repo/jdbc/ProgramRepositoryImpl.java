package com.stanislavnoskov.adf.universityx.programs.repo.jdbc;

import java.util.Collections;
import java.util.List;

import com.stanislavnoskov.adf.universityx.programs.model.StudyProgram;
import com.stanislavnoskov.adf.universityx.programs.repo.ProgramRepository;
import com.stanislavnoskov.adf.universityx.programs.repo.jdbc.rowmapper.StudyProgramRowMapper;
import com.stanislavnoskov.adf.universityx.programs.repo.ProgramDatabaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
@Primary
public class ProgramRepositoryImpl implements ProgramRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProgramRepositoryImpl.class);

    private static final String WHERE_SQL = " where ";

    private static final String GET_PROGRAM_SQL =
            "select "
            + ProgramDatabaseModel.StudyProgramTable.TABLE_NAME + "." + ProgramDatabaseModel.StudyProgramTable.PROGRAM_ID + ", "
            + ProgramDatabaseModel.StudyProgramTable.TABLE_NAME + "." + ProgramDatabaseModel.StudyProgramTable.PROGRAM_CODE + ", "
            + ProgramDatabaseModel.StudyProgramTable.TABLE_NAME + "." + ProgramDatabaseModel.StudyProgramTable.DESCRIPTION + ", "
            + ProgramDatabaseModel.StudyProgramTable.TABLE_NAME + "." + ProgramDatabaseModel.StudyProgramTable.DURATION + ", "
            + ProgramDatabaseModel.AcademicDegreeTable.TABLE_NAME + "." + ProgramDatabaseModel.AcademicDegreeTable.DEGREE_NAME + ", "
            + ProgramDatabaseModel.AcademicDegreeTable.TABLE_NAME + "." + ProgramDatabaseModel.AcademicDegreeTable.DEGREE_TYPE
            + " from "
            + ProgramDatabaseModel.StudyProgramTable.TABLE_NAME
            + " inner join "
            + ProgramDatabaseModel.AcademicDegreeTable.TABLE_NAME
            + " on "
            + ProgramDatabaseModel.StudyProgramTable.TABLE_NAME + "." + ProgramDatabaseModel.StudyProgramTable.ACADEMIC_DEGREE_ID
            + " = "
            + ProgramDatabaseModel.AcademicDegreeTable.TABLE_NAME + "." + ProgramDatabaseModel.AcademicDegreeTable.ACADEMIC_DEGREE_ID;

    private static final String GET_PROGRAM_BY_ID_CONDITION_SQL =
            WHERE_SQL
            + ProgramDatabaseModel.StudyProgramTable.TABLE_NAME + "." + ProgramDatabaseModel.StudyProgramTable.PROGRAM_ID
            + " = :"
            + ProgramDatabaseModel.StudyProgramTable.PROGRAM_ID;

    private static final String GET_PROGRAM_BY_IDS_CONDITION_SQL =
            WHERE_SQL
            + ProgramDatabaseModel.StudyProgramTable.TABLE_NAME + "." + ProgramDatabaseModel.StudyProgramTable.PROGRAM_ID
            + " in (:"
            + ProgramDatabaseModel.StudyProgramTable.PROGRAM_ID
            + ")";

    private static final String GET_PROGRAM_BY_CODES_CONDITION_SQL =
            WHERE_SQL
            + ProgramDatabaseModel.StudyProgramTable.TABLE_NAME + "." + ProgramDatabaseModel.StudyProgramTable.PROGRAM_CODE
            + " in (:"
            + ProgramDatabaseModel.StudyProgramTable.PROGRAM_CODE
            + ")";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ProgramRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public StudyProgram getById(Integer id) {
        String query = GET_PROGRAM_SQL + GET_PROGRAM_BY_ID_CONDITION_SQL;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(ProgramDatabaseModel.StudyProgramTable.PROGRAM_ID, id);

        try {
            return namedParameterJdbcTemplate.queryForObject(query, params, new StudyProgramRowMapper());
        } catch (EmptyResultDataAccessException exc) {
            LOGGER.info("Study programs with id: {} not found", id);
            return null;
        }
    }

    @Override
    public List<StudyProgram> getByIds(List<Integer> ids){
        StringBuilder query = new StringBuilder(GET_PROGRAM_SQL);
        MapSqlParameterSource params = new MapSqlParameterSource();

        if (!CollectionUtils.isEmpty(ids)) {
            query.append(GET_PROGRAM_BY_IDS_CONDITION_SQL);
            params.addValue(ProgramDatabaseModel.StudyProgramTable.PROGRAM_ID, ids);
        }

        try {
            return namedParameterJdbcTemplate.query(query.toString(), params, new StudyProgramRowMapper());
        } catch (EmptyResultDataAccessException exc) {
            LOGGER.info("Study programs with ids: {} not found", ids);
            return Collections.emptyList();
        }
    }

    @Override
    public List<StudyProgram> getByCodes(List<String> codes) {
        String query = GET_PROGRAM_SQL + GET_PROGRAM_BY_CODES_CONDITION_SQL;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(ProgramDatabaseModel.StudyProgramTable.PROGRAM_CODE, codes);

        try {
            return namedParameterJdbcTemplate.query(query, params, new StudyProgramRowMapper());
        } catch (EmptyResultDataAccessException exc) {
            LOGGER.info("Study programs with codes: {} not found", codes);
            return Collections.emptyList();
        }
    }
}
