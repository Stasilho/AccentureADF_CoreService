package com.stanislavnoskov.adf.universityx.programs.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class StudyProgramTest {

    @Test
    public void isEmpty() {
        StudyProgram program = new StudyProgram(1, "CODE", "description", 3, null);
        assertTrue(program.isEmpty());
    }
}