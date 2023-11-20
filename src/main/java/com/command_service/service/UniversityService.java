package com.command_service.service;

import com.command_service.model.University;
import com.command_service.model.dto.UniversityDTO;
import com.command_service.model.dto.UniversityFacultyDTO;

import java.util.HashMap;
import java.util.List;

public interface UniversityService {

    int save(University university);

    int delete(int universityID);

    University getUniversityById(int id);

    University getUniversityByName(String name);

    List<University> getAllUniversity();

    int update(University university);

    HashMap<Integer, UniversityDTO> getFullUniversity();

    int addFullUniversity(UniversityDTO university);

    String saveUniversityDTO(UniversityDTO universityDTO);

    String saveUniversityCustomDTO(UniversityFacultyDTO universityDTO);

}
