package com.accenture.com.snoskov.adf.universityx.users.repo.jdbc;

import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;

import com.accenture.com.snoskov.adf.universityx.users.model.ApplicationUser;
import com.accenture.com.snoskov.adf.universityx.programs.model.StudyProgram;
import com.accenture.com.snoskov.adf.universityx.users.repo.UserRepository;
import com.accenture.com.snoskov.adf.universityx.users.repo.UserDatabaseModel.UserTable;
import com.accenture.com.snoskov.adf.universityx.users.repo.jdbc.rowmapper.ApplicationUserRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
@Primary
public class UserRepositoryImpl implements UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryImpl.class);

    private static final String GET_USER_SQL =
            "select "
            + UserTable.TABLE_NAME + "." + UserTable.USER_ID + ", "
            + UserTable.TABLE_NAME + "." + UserTable.USERNAME + ", "
            + UserTable.TABLE_NAME + "." + UserTable.FIRST_NAME + ", "
            + UserTable.TABLE_NAME + "." + UserTable.LAST_NAME + ", "
            + UserTable.TABLE_NAME + "." + UserTable.EMAIL + ", "
            + UserTable.TABLE_NAME + "." + UserTable.PSWD_FIELD + ", "
            + UserTable.TABLE_NAME + "." + UserTable.YEAR_OF_ENTRANCE + ", "
            + UserTable.TABLE_NAME + "." + UserTable.PROGRAM_ID + ", "
            + UserTable.TABLE_NAME + "." + UserTable.ROLE
            + " from "
            + UserTable.TABLE_NAME;

    private static final String GET_USER_BY_USERNAME_CONDITION_SQL =
            " where "
            + UserTable.TABLE_NAME + "." + UserTable.USERNAME
            + " = :"
            + UserTable.USERNAME;

    private static final String GET_USER_BY_IDS_CONDITION_SQL =
            " where "
            + UserTable.TABLE_NAME + "." + UserTable.USER_ID
            + " in (:"
            + UserTable.USER_ID
            + ")";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private SimpleJdbcInsert insertUserTemplate;

    @Autowired
    public UserRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, DataSource ds) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

        this.insertUserTemplate = new SimpleJdbcInsert(ds)
                .withTableName(UserTable.TABLE_NAME)
                .usingGeneratedKeyColumns(UserTable.USER_ID)
                .usingColumns(
                        UserTable.USERNAME,
                        UserTable.FIRST_NAME,
                        UserTable.LAST_NAME,
                        UserTable.EMAIL,
                        UserTable.PSWD_FIELD,
                        UserTable.YEAR_OF_ENTRANCE,
                        UserTable.PROGRAM_ID,
                        UserTable.ROLE);
        this.insertUserTemplate.withoutTableColumnMetaDataAccess();
    }

    @Override
    public void addNewUser(ApplicationUser user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(UserTable.USERNAME, user.getUsername());
        params.addValue(UserTable.FIRST_NAME, user.getFirstName());
        params.addValue(UserTable.LAST_NAME, user.getLastName());
        params.addValue(UserTable.EMAIL, user.getEmail());
        params.addValue(UserTable.PSWD_FIELD, user.getPassword());
        params.addValue(UserTable.YEAR_OF_ENTRANCE, user.getYearOfEntrance());
        params.addValue(UserTable.ROLE, user.getRole().getName());

        StudyProgram studyProgram = user.getStudyProgram();
        if (studyProgram != null) {
            params.addValue(UserTable.PROGRAM_ID, studyProgram.getId());
        }

        insertUserTemplate.execute(params);
    }

    @Override
    public ApplicationUser findByUsername(String username) {
        String query = GET_USER_SQL + GET_USER_BY_USERNAME_CONDITION_SQL;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(UserTable.USERNAME, username);

        try {
            return namedParameterJdbcTemplate.queryForObject(query, params, new ApplicationUserRowMapper());
        } catch (EmptyResultDataAccessException exc) {
            LOGGER.info("User with username: {} not found", username);
            return null;
        }
    }

    @Override
    public List<ApplicationUser> findByIds(List<Integer> userIds) {
        StringBuilder query = new StringBuilder(GET_USER_SQL);
        MapSqlParameterSource params = new MapSqlParameterSource();

        if (!CollectionUtils.isEmpty(userIds)) {
            query.append(GET_USER_BY_IDS_CONDITION_SQL);
            params.addValue(UserTable.USER_ID, userIds);
        }

        try {
            return namedParameterJdbcTemplate.query(query.toString(), params, new ApplicationUserRowMapper());
        } catch (EmptyResultDataAccessException exc) {
            LOGGER.info("No users not found for given ids");
            return Collections.emptyList();
        }
    }
}
