package com.stanislavnoskov.adf.universityx.programs.rest;

import com.stanislavnoskov.adf.universityx.programs.rest.mapper.ProgramMapper;
import com.stanislavnoskov.adf.universityx.programs.service.ProgramService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ProgramControllerTest {

    private static ProgramController controller;
    private static ProgramMapper programMapper;
    private static ProgramService service;

    @BeforeClass
    public static void initialize() {
        service = Mockito.mock(ProgramService.class);
        programMapper = Mockito.mock(ProgramMapper.class);
        controller = new ProgramController(programMapper, service);
    }

    @Test
    public void testRetrieveStudyPrograms() {
        controller.retrieveStudyPrograms();

        verify(service, times(1)).retrieveStudyPrograms();
    }
}