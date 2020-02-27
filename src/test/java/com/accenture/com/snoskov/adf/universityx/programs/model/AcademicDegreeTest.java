package com.accenture.com.snoskov.adf.universityx.programs.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class AcademicDegreeTest {

    @Test
    public void testToString() {
        AcademicDegree degree = new AcademicDegree("Bachelor", "academic");
        assertEquals("academic Bachelor", degree.toString());
    }
}