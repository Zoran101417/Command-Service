package com.command_service.service;


import com.command_service.model.Profesor;
import com.command_service.model.dto.ProfessorsDTO;

import java.sql.SQLException;
import java.util.List;

public interface ProfesorService {
    int save(Profesor profesor);

    int delete(int profesorID);

    Profesor getProfesorById(int id);

    List<Profesor> getAllProfesors();

    int update(Profesor profesor);

    List<Profesor> getAllProfesorsByFacultyID(int facultyID) throws SQLException;

    ProfessorsDTO getFacultyAndSubjects();

    ProfessorsDTO getProfessorToEdit(int id);
}
