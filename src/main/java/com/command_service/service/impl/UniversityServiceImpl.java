package com.command_service.service.impl;

import com.command_service.dao.FacultyDAO;
import com.command_service.dao.UniversityDAO;
import com.command_service.exception.KafkaException;
import com.command_service.model.Faculty;
import com.command_service.model.University;
import com.command_service.model.dto.UniversityDTO;
import com.command_service.model.dto.UniversityFacultyDTO;
import com.command_service.service.UniversityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

@Service
public class UniversityServiceImpl implements UniversityService {

    private static final Logger LOG = LogManager.getLogger(UniversityServiceImpl.class);

    private final UniversityDAO universityDAO;

    private final FacultyDAO facultyDAO;

    private final KafkaTemplate<String, Object> multiTypeKafkaTemplate;

    public UniversityServiceImpl(UniversityDAO universityDAO, FacultyDAO facultyDAO, KafkaTemplate<String, Object> multiTypeKafkaTemplate) {
        this.universityDAO = universityDAO;
        this.facultyDAO = facultyDAO;
        this.multiTypeKafkaTemplate = multiTypeKafkaTemplate;
    }

    @Override
    @Transactional
    public int save(University university) {
        universityDAO.add(university);
        try {
            this.multiTypeKafkaTemplate.send("save", university);
        } catch (KafkaException e) {
            /// add custom exception
            throw new KafkaException("Failed to produce a message to save university ", e);
        }
        return universityDAO.add(university); // change it
    }

    @Override
    @Transactional
    public int delete(int universityID) {
        universityDAO.delete(universityID);
        try {
            this.multiTypeKafkaTemplate.send("deleteUniversity", universityID);
        } catch (KafkaException e) {

        }

        return universityDAO.delete(universityID);// change it
    }

    @Override
    @Transactional
    public int update(University university) {
        universityDAO.update(university);
        try {
            this.multiTypeKafkaTemplate.send("save", university);
        } catch (KafkaException e) {

        }

        return universityDAO.update(university);// change it
    }


    @Override
    public University getUniversityById(int id) {
        return universityDAO.findById(id);
    }

    @Override
    public University getUniversityByName(String name) {
        return null;
    }

    @Override
    public List<University> getAllUniversity() {
        return universityDAO.getAllUniversity();
    }

    @Override
    public HashMap<Integer, UniversityDTO> getFullUniversity() {
        return null;
    }

    @Override
    public int addFullUniversity(UniversityDTO universityDTO) {
        return 0;
    }

    @Override
    public String saveUniversityDTO(UniversityDTO universityDTO){
        return null;
    }

    @Override
    @Transactional
    public String saveUniversityCustomDTO(UniversityFacultyDTO universityFacultyDTO){

        String message = "Uspesno se zacuvani podatocite";
        try {
            int universityID = save(universityFacultyDTO.getUniversity());

            if(universityID > 0) {
                for(Faculty f : universityFacultyDTO.getFacultyList()) {
                    facultyDAO.add(f);
                }
            }
        } catch (Exception e) {
            LOG.error("saveUniversityCustomDTO error: " + e.getMessage());
            throw e;
        }

        return message;//universityDAO.saveUniversityCustomDTO(universityCustomDTO);
    }
}
