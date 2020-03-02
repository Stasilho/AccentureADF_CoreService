package com.stanislavnoskov.adf.universityx.programs.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.stanislavnoskov.adf.universityx.programs.model.AcademicDegree;
import com.stanislavnoskov.adf.universityx.programs.model.StudyProgram;
import com.stanislavnoskov.adf.universityx.programs.repo.ProgramRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ProgramServiceTest {

    private static ProgramService service;
    private static ProgramRepository programRepository;

    @BeforeClass
    public static void initialize() {
        programRepository = Mockito.mock(ProgramRepository.class);
        service = new ProgramService(programRepository);
    }

    @Test
    public void retrieveStudyPrograms() {
        StudyProgram program1 = new StudyProgram(1, "CODE1", "description1", 3, new AcademicDegree("Bachelor", "academic"));
        StudyProgram program2 = new StudyProgram(2, "CODE2", "description2", 2, new AcademicDegree("Master", "professional"));
        when(programRepository.getByIds(eq(Collections.emptyList()))).thenReturn(Arrays.asList(program1, program2));

        List<StudyProgram> programs = service.retrieveStudyPrograms();

        assertNotNull(programs);
        assertEquals(2, programs.size());
    }
}