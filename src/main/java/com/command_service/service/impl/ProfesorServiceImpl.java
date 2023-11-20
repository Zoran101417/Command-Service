package com.command_service.service.impl;


import com.command_service.dao.ProfesorDAO;
import com.command_service.model.Profesor;
import com.command_service.model.dto.ProfessorsDTO;
import com.command_service.service.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProfesorServiceImpl implements ProfesorService {

    @Autowired
    ProfesorDAO profesorDAO;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    private final KafkaTemplate<String, Object> multiTypeKafkaTemplate;

    public ProfesorServiceImpl(KafkaTemplate<String, Object> multiTypeKafkaTemplate) {
        this.multiTypeKafkaTemplate = multiTypeKafkaTemplate;
    }

    @Override
    @Transactional
    public int save(Profesor professor) {
        return profesorDAO.add(professor);
    }

    @Override
    @Transactional
    public int delete(int professorID) {
        return profesorDAO.delete(professorID);
    }

    @Override
    public Profesor getProfesorById(int id) {
        return profesorDAO.getProfesorById(id);
    }

    @Override
    public List<Profesor> getAllProfesors() {
        return profesorDAO.getAllProfesors();
    }

    @Override
    @Transactional
    public int update(Profesor professor) {
        return profesorDAO.update(professor);
    }

    @Override
    public List<Profesor> getAllProfesorsByFacultyID(int facultyID) {
        return profesorDAO.getAllProfesorsByFacultyID(facultyID);
    }

    @Override
    public ProfessorsDTO getFacultyAndSubjects() {
        return profesorDAO.getFacultyAndSubjects();
    }

    @Override
    public ProfessorsDTO getProfessorToEdit(int id) {
        return profesorDAO.getProfessorToEdit(id);
    }
}
