package com.accenture.com.snoskov.adf.universityx.programs.repo;

import java.util.List;

import com.accenture.com.snoskov.adf.universityx.programs.model.StudyProgram;

public interface ProgramRepository {

    StudyProgram getById(Integer id);

    List<StudyProgram> getByIds(List<Integer> ids);

    List<StudyProgram> getByCodes(List<String> codes);
}
