package com.accenture.com.snoskov.adf.universityx.programs.repo;

public class ProgramDatabaseModel {

    private ProgramDatabaseModel() {}

    public static final class StudyProgramTable {
        private StudyProgramTable() {}
        public static final String TABLE_NAME = "programs";
        public static final String PROGRAM_ID = "program_id";
        public static final String PROGRAM_CODE = "program_code";
        public static final String ACADEMIC_DEGREE_ID = "academic_degree_id";
        public static final String DESCRIPTION = "description";
        public static final String DURATION = "duration";
    }

    public static final class AcademicDegreeTable {
        private AcademicDegreeTable() {}
        public static final String TABLE_NAME = "academic_degrees";
        public static final String ACADEMIC_DEGREE_ID = "academic_degree_id";
        public static final String DEGREE_NAME = "degree_name";
        public static final String DEGREE_TYPE = "degree_type";
    }
}
