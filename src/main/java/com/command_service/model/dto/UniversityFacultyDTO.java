package com.command_service.model.dto;

import com.command_service.model.Faculty;
import com.command_service.model.University;

import java.util.List;

public class UniversityFacultyDTO {
    private University university;
    private List<Faculty> facultyList;

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public List<Faculty> getFacultyList() {
        return facultyList;
    }

    public void setFacultyList(List<Faculty> facultyList) {
        this.facultyList = facultyList;
    }
}
