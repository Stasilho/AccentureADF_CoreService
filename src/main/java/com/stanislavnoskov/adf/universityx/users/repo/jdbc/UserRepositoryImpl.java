package com.stanislavnoskov.adf.universityx.users.repo.jdbc;

import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;

import com.stanislavnoskov.adf.universityx.users.model.ApplicationUser;
import com.stanislavnoskov.adf.universityx.programs.model.StudyProgram;
import com.stanislavnoskov.adf.universityx.users.repo.UserRepository;
import com.stanislavnoskov.adf.universityx.users.repo.jdbc.rowmapper.ApplicationUserRowMapper;
import com.stanislavnoskov.adf.universityx.users.repo.UserDatabaseModel;
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
            + UserDatabaseModel.UserTable.TABLE_NAME + "." + UserDatabaseModel.UserTable.USER_ID + ", "
            + UserDatabaseModel.UserTable.TABLE_NAME + "." + UserDatabaseModel.UserTable.USERNAME + ", "
            + UserDatabaseModel.UserTable.TABLE_NAME + "." + UserDatabaseModel.UserTable.FIRST_NAME + ", "
            + UserDatabaseModel.UserTable.TABLE_NAME + "." + UserDatabaseModel.UserTable.LAST_NAME + ", "
            + UserDatabaseModel.UserTable.TABLE_NAME + "." + UserDatabaseModel.UserTable.EMAIL + ", "
            + UserDatabaseModel.UserTable.TABLE_NAME + "." + UserDatabaseModel.UserTable.PSWD_FIELD + ", "
            + UserDatabaseModel.UserTable.TABLE_NAME + "." + UserDatabaseModel.UserTable.YEAR_OF_ENTRANCE + ", "
            + UserDatabaseModel.UserTable.TABLE_NAME + "." + UserDatabaseModel.UserTable.PROGRAM_ID + ", "
            + UserDatabaseModel.UserTable.TABLE_NAME + "." + UserDatabaseModel.UserTable.ROLE
            + " from "
            + UserDatabaseModel.UserTable.TABLE_NAME;

    private static final String GET_USER_BY_USERNAME_CONDITION_SQL =
            " where "
            + UserDatabaseModel.UserTable.TABLE_NAME + "." + UserDatabaseModel.UserTable.USERNAME
            + " = :"
            + UserDatabaseModel.UserTable.USERNAME;

    private static final String GET_USER_BY_IDS_CONDITION_SQL =
            " where "
            + UserDatabaseModel.UserTable.TABLE_NAME + "." + UserDatabaseModel.UserTable.USER_ID
            + " in (:"
            + UserDatabaseModel.UserTable.USER_ID
            + ")";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private SimpleJdbcInsert insertUserTemplate;

    @Autowired
    public UserRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, DataSource ds) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

        this.insertUserTemplate = new SimpleJdbcInsert(ds)
                .withTableName(UserDatabaseModel.UserTable.TABLE_NAME)
                .usingGeneratedKeyColumns(UserDatabaseModel.UserTable.USER_ID)
                .usingColumns(
                        UserDatabaseModel.UserTable.USERNAME,
                        UserDatabaseModel.UserTable.FIRST_NAME,
                        UserDatabaseModel.UserTable.LAST_NAME,
                        UserDatabaseModel.UserTable.EMAIL,
                        UserDatabaseModel.UserTable.PSWD_FIELD,
                        UserDatabaseModel.UserTable.YEAR_OF_ENTRANCE,
                        UserDatabaseModel.UserTable.PROGRAM_ID,
                        UserDatabaseModel.UserTable.ROLE);
        this.insertUserTemplate.withoutTableColumnMetaDataAccess();
    }

    @Override
    public void addNewUser(ApplicationUser user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(UserDatabaseModel.UserTable.USERNAME, user.getUsername());
        params.addValue(UserDatabaseModel.UserTable.FIRST_NAME, user.getFirstName());
        params.addValue(UserDatabaseModel.UserTable.LAST_NAME, user.getLastName());
        params.addValue(UserDatabaseModel.UserTable.EMAIL, user.getEmail());
        params.addValue(UserDatabaseModel.UserTable.PSWD_FIELD, user.getPassword());
        params.addValue(UserDatabaseModel.UserTable.YEAR_OF_ENTRANCE, user.getYearOfEntrance());
        params.addValue(UserDatabaseModel.UserTable.ROLE, user.getRole().getName());

        StudyProgram studyProgram = user.getStudyProgram();
        if (studyProgram != null) {
            params.addValue(UserDatabaseModel.UserTable.PROGRAM_ID, studyProgram.getId());
        }

        insertUserTemplate.execute(params);
    }

    @Override
    public ApplicationUser findByUsername(String username) {
        String query = GET_USER_SQL + GET_USER_BY_USERNAME_CONDITION_SQL;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(UserDatabaseModel.UserTable.USERNAME, username);

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
            params.addValue(UserDatabaseModel.UserTable.USER_ID, userIds);
        }

        try {
            return namedParameterJdbcTemplate.query(query.toString(), params, new ApplicationUserRowMapper());
        } catch (EmptyResultDataAccessException exc) {
            LOGGER.info("No users not found for given ids");
            return Collections.emptyList();
        }
    }
}
