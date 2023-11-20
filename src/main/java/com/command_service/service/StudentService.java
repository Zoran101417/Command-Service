package com.command_service.service;

import com.command_service.model.Student;

import java.util.List;

public interface StudentService {
    int save(Student student);

    int delete(int studentID);

    Student getStudentById(int id);

    List<Student> getAllStudents();

    int update(Student student);
}
