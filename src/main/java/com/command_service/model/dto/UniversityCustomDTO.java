package com.command_service.model.dto;

import com.command_service.model.University;

import java.util.List;

public class UniversityCustomDTO {
    private University university;
    private List<FacultyDTO> facultyList;

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public List<FacultyDTO> getFacultyList() {
        return facultyList;
    }

    public void setFacultyList(List<FacultyDTO> faculty) {
        this.facultyList = faculty;
    }
}
