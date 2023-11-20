package com.command_service.model.dto;

import com.command_service.model.Faculty;
import com.command_service.model.Profesor;
import com.command_service.model.Student;
import com.command_service.model.University;

import java.util.List;

public class UniversityDTO {
    private University university;
    private List<Faculty> facultyList;
    private List<Profesor> professorList;
    private List<Student> studentList;

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

    public List<Profesor> getProfessorList() {
        return professorList;
    }

    public void setProfessorList(List<Profesor> professorList) {
        this.professorList = professorList;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    @Override
    public String toString() {
        return "UniversityDTO{" +
                "university=" + university +
                ", facultyList=" + facultyList +
                ", professorList=" + professorList +
                ", studentList=" + studentList +
                '}';
    }
}
