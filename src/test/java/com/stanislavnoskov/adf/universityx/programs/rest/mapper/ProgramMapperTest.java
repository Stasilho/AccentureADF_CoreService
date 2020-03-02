package com.stanislavnoskov.adf.universityx.programs.rest.mapper;

import java.util.Arrays;
import java.util.List;

import com.stanislavnoskov.adf.universityx.programs.model.AcademicDegree;
import com.stanislavnoskov.adf.universityx.programs.model.StudyProgram;
import com.stanislavnoskov.adf.universityx.programs.rest.model.StudyProgramDTO;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProgramMapperTest {

    private static ProgramMapper programMapper;

    @BeforeClass
    public static void initialize() {
        programMapper = new ProgramMapper();
    }

    @Test
    public void studyProgramToDto() {
        StudyProgram program = new StudyProgram(1, "CODE", "description", 3, new AcademicDegree("Bachelor", "academic"));
        StudyProgramDTO programDTO = programMapper.studyProgramToDto(program);

        assertEquals(Integer.valueOf(1), programDTO.getProgramId());
        assertEquals("CODE", programDTO.getProgramCode());
        assertEquals("description", programDTO.getDescription());
        assertEquals(Integer.valueOf(3), programDTO.getDuration());

        assertNotNull(programDTO.getAcademicDegree());
    }

    @Test
    public void studyProgramsToDTOs() {
        StudyProgram program1 = new StudyProgram(1, "CODE1", "description1", 3, new AcademicDegree("Bachelor", "academic"));
        StudyProgram program2 = new StudyProgram(2, "CODE2", "description2", 2, new AcademicDegree("Master", "professional"));

        List<StudyProgramDTO> programsDTO = programMapper.studyProgramsToDTOs(Arrays.asList(program1, program2));

        assertEquals(2, programsDTO.size());
        assertEquals("CODE1", programsDTO.get(0).getProgramCode());
        assertEquals("CODE2", programsDTO.get(1).getProgramCode());
    }
}