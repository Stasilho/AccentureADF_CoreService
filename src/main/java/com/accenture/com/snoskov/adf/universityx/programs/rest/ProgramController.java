package com.accenture.com.snoskov.adf.universityx.programs.rest;

import java.util.List;

import com.accenture.com.snoskov.adf.universityx.programs.rest.mapper.ProgramMapper;
import com.accenture.com.snoskov.adf.universityx.programs.rest.model.StudyProgramDTO;
import com.accenture.com.snoskov.adf.universityx.programs.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apis/programs")
public class ProgramController {

    private ProgramMapper programMapper;
    private ProgramService programService;

    @Autowired
    public ProgramController(ProgramMapper programMapper, ProgramService programService) {
        this.programMapper = programMapper;
        this.programService = programService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping
    public List<StudyProgramDTO> retrieveStudyPrograms() {
        return programMapper.studyProgramsToDTOs(programService.retrieveStudyPrograms());
    }
}
