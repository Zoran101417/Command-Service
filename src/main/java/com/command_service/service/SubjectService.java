package com.command_service.service;

import com.command_service.model.Subject;
import com.command_service.model.dto.SubjectDTO;

import java.util.List;

public interface SubjectService {
    int save(Subject subject);

    int delete(int subjectID);

    Subject getSubjectById(int id);

    List<Subject> getAllSubjects();

    int update(Subject subject);

    SubjectDTO getSubjectToEdit(int id);
}
