package com.accenture.com.snoskov.adf.universityx.users.repo;

public class UserDatabaseModel {

    private UserDatabaseModel() {}

    public static final class UserTable {
        private UserTable() {}
        public static final String TABLE_NAME = "users";
        public static final String USER_ID = "user_id";
        public static final String USERNAME = "username";
        public static final String FIRST_NAME = "firstname";
        public static final String LAST_NAME = "lastname";
        public static final String EMAIL = "email";
        public static final String PSWD_FIELD = "password";
        public static final String YEAR_OF_ENTRANCE = "year_of_entrance";
        public static final String PROGRAM_ID = "program_id";
        public static final String ROLE = "role";
        public static final String CREATED_AT = "reg_date";
    }
}
