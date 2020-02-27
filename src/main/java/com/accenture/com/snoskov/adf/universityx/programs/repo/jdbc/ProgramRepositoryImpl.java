package com.accenture.com.snoskov.adf.universityx.programs.repo.jdbc;

import java.util.Collections;
import java.util.List;

import com.accenture.com.snoskov.adf.universityx.programs.model.StudyProgram;
import com.accenture.com.snoskov.adf.universityx.programs.repo.ProgramDatabaseModel.AcademicDegreeTable;
import com.accenture.com.snoskov.adf.universityx.programs.repo.ProgramDatabaseModel.StudyProgramTable;
import com.accenture.com.snoskov.adf.universityx.programs.repo.ProgramRepository;
import com.accenture.com.snoskov.adf.universityx.programs.repo.jdbc.rowmapper.StudyProgramRowMapper;
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
            + StudyProgramTable.TABLE_NAME + "." + StudyProgramTable.PROGRAM_ID + ", "
            + StudyProgramTable.TABLE_NAME + "." + StudyProgramTable.PROGRAM_CODE + ", "
            + StudyProgramTable.TABLE_NAME + "." + StudyProgramTable.DESCRIPTION + ", "
            + StudyProgramTable.TABLE_NAME + "." + StudyProgramTable.DURATION + ", "
            + AcademicDegreeTable.TABLE_NAME + "." + AcademicDegreeTable.DEGREE_NAME + ", "
            + AcademicDegreeTable.TABLE_NAME + "." + AcademicDegreeTable.DEGREE_TYPE
            + " from "
            + StudyProgramTable.TABLE_NAME
            + " inner join "
            + AcademicDegreeTable.TABLE_NAME
            + " on "
            + StudyProgramTable.TABLE_NAME + "." + StudyProgramTable.ACADEMIC_DEGREE_ID
            + " = "
            + AcademicDegreeTable.TABLE_NAME + "." + AcademicDegreeTable.ACADEMIC_DEGREE_ID;

    private static final String GET_PROGRAM_BY_ID_CONDITION_SQL =
            WHERE_SQL
            + StudyProgramTable.TABLE_NAME + "." + StudyProgramTable.PROGRAM_ID
            + " = :"
            + StudyProgramTable.PROGRAM_ID;

    private static final String GET_PROGRAM_BY_IDS_CONDITION_SQL =
            WHERE_SQL
            + StudyProgramTable.TABLE_NAME + "." + StudyProgramTable.PROGRAM_ID
            + " in (:"
            + StudyProgramTable.PROGRAM_ID
            + ")";

    private static final String GET_PROGRAM_BY_CODES_CONDITION_SQL =
            WHERE_SQL
            + StudyProgramTable.TABLE_NAME + "." + StudyProgramTable.PROGRAM_CODE
            + " in (:"
            + StudyProgramTable.PROGRAM_CODE
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
        params.addValue(StudyProgramTable.PROGRAM_ID, id);

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
            params.addValue(StudyProgramTable.PROGRAM_ID, ids);
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
        params.addValue(StudyProgramTable.PROGRAM_CODE, codes);

        try {
            return namedParameterJdbcTemplate.query(query, params, new StudyProgramRowMapper());
        } catch (EmptyResultDataAccessException exc) {
            LOGGER.info("Study programs with codes: {} not found", codes);
            return Collections.emptyList();
        }
    }
}
