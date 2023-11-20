package com.command_service.service;

import com.command_service.model.Faculty;
import com.command_service.model.dto.FacultyToEditDTO;

import java.util.List;

public interface FacultyService {
    int save(Faculty faculty);

    int delete(int facultyID);

    Faculty getFacultyById(int id);

    List<Faculty> getAllFaculties();

    int update(Faculty faculty);

    List<Faculty> getAllFacultiesByUniversityId(int universityID);

    FacultyToEditDTO getFacultyToEdit(int id);
}
