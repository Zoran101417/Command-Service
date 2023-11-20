package com.command_service.model.dto;

import com.command_service.model.Faculty;
import com.command_service.model.Subject;

import java.util.List;

public class SubjectDTO {
    private Subject subject;
    private List<Faculty> facultyList;

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<Faculty> getFacultyList() {
        return facultyList;
    }

    public void setFacultyList(List<Faculty> facultyList) {
        this.facultyList = facultyList;
    }
}
