package com.stanislavnoskov.adf.universityx.programs.service;

import java.util.Collections;
import java.util.List;

import com.stanislavnoskov.adf.universityx.programs.model.StudyProgram;
import com.stanislavnoskov.adf.universityx.programs.repo.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProgramService {

    private ProgramRepository programRepository;

    @Autowired
    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    public List<StudyProgram> retrieveStudyPrograms() {
        return programRepository.getByIds(Collections.emptyList());
    }
}
