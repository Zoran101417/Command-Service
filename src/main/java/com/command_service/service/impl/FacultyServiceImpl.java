package com.command_service.service.impl;

import com.command_service.dao.FacultyDAO;
import com.command_service.exception.KafkaException;
import com.command_service.model.Faculty;
import com.command_service.model.dto.FacultyToEditDTO;
import com.command_service.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FacultyServiceImpl implements FacultyService {

    @Autowired
    FacultyDAO facultyDAO;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    private final KafkaTemplate<String, Object> multiTypeKafkaTemplate;

    public FacultyServiceImpl(KafkaTemplate<String, Object> multiTypeKafkaTemplate) {
        this.multiTypeKafkaTemplate = multiTypeKafkaTemplate;
    }

    @Override
    @Transactional
    public int save(Faculty faculty) {
        facultyDAO.add(faculty);
        try {
            this.simpMessagingTemplate.convertAndSend("/topic/faculty", faculty);
            this.multiTypeKafkaTemplate.send("save", faculty);//change it
        } catch (KafkaException e) {//change it

        }
        return facultyDAO.add(faculty);
    }

    @Override
    @Transactional
    public int delete(int facultyID) {
        facultyDAO.delete(facultyID);
        try{
             this.simpMessagingTemplate.convertAndSend("/topic/faculty", facultyID);
            this.multiTypeKafkaTemplate.send("/topic/faculty", facultyID);//change it
        } catch (KafkaException e) {

        }
        return facultyDAO.delete(facultyID);//change it
    }

    @Override
    @Transactional
    public int update(Faculty faculty) {
        facultyDAO.update(faculty);
        try{//change it
            this.simpMessagingTemplate.convertAndSend("/topic/faculty", faculty);
        } catch (KafkaException e) {

        }
        return facultyDAO.update(faculty); //change it
    }

    @Override
    public Faculty getFacultyById(int id) {
        return facultyDAO.getFacultyById(id);
    }

    @Override
    public List<Faculty> getAllFaculties() {
        return facultyDAO.getAllFaculties();
    }

    @Override
    public List<Faculty> getAllFacultiesByUniversityId(int universityID) {
        return facultyDAO.getAllFacultiesByUniversityId(universityID);
    }

    @Override
    public FacultyToEditDTO getFacultyToEdit(int id) {
        return facultyDAO.getFacultyToEdit(id);
    }
}
