package com.stanislavnoskov.adf.universityx.users.repo.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.stanislavnoskov.adf.universityx.users.model.ApplicationUser;
import com.stanislavnoskov.adf.universityx.users.model.Role;
import com.stanislavnoskov.adf.universityx.programs.model.StudyProgram;
import com.stanislavnoskov.adf.universityx.users.repo.UserDatabaseModel.UserTable;
import org.springframework.jdbc.core.RowMapper;

public class ApplicationUserRowMapper implements RowMapper<ApplicationUser> {

    @Override
    public ApplicationUser mapRow(ResultSet rs, int i) throws SQLException {
        ApplicationUser user = new ApplicationUser(rs.getString(UserTable.USERNAME), rs.getString(UserTable.PSWD_FIELD));
        user.setId(rs.getInt(UserTable.USER_ID));
        user.setPersonIdentity(rs.getString(UserTable.FIRST_NAME), rs.getString(UserTable.LAST_NAME));
        user.setEmail(rs.getString(UserTable.EMAIL));
        user.setRole(Role.fromName(rs.getString(UserTable.ROLE)));
        user.setYearOfEntrance(rs.getString(UserTable.YEAR_OF_ENTRANCE));

        Integer programId = rs.getInt(UserTable.PROGRAM_ID);
        if (!rs.wasNull()) {
            user.setStudyProgram(new StudyProgram(programId));
        }

        return user;
    }
}
