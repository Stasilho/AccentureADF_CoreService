package com.accenture.com.snoskov.adf.universityx.programs.rest.mapper;

import java.util.List;

import com.accenture.com.snoskov.adf.universityx.programs.model.StudyProgram;
import com.accenture.com.snoskov.adf.universityx.programs.rest.model.StudyProgramDTO;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@Component
public class ProgramMapper {

    public StudyProgramDTO studyProgramToDto(StudyProgram program) {
        StudyProgramDTO programDTO = new StudyProgramDTO();
        programDTO.setProgramId(program.getId());
        programDTO.setProgramCode(program.getProgramCode());
        programDTO.setDescription(program.getDescription());
        programDTO.setDuration(program.getDuration());
        programDTO.setAcademicDegree(program.getAcademicDegree().toString());

        return programDTO;
    }

    public List<StudyProgramDTO> studyProgramsToDTOs(List<StudyProgram> studyPrograms) {
        return studyPrograms.stream()
                            .map(this::studyProgramToDto)
                            .collect(toList());
    }
}
