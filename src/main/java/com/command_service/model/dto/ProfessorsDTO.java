package com.command_service.model.dto;

import com.command_service.model.Faculty;
import com.command_service.model.Profesor;
import com.command_service.model.Subject;

import java.util.List;

public class ProfessorsDTO {
    private Profesor profesor;
    private List<Faculty> facultyList;
    private List<Subject> subjectList;

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public List<Faculty> getFacultyList() {
        return facultyList;
    }

    public void setFacultyList(List<Faculty> facultyList) {
        this.facultyList = facultyList;
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }
}
