package com.command_service.service.impl;

import com.command_service.dao.SubjectDAO;
import com.command_service.exception.KafkaException;
import com.command_service.model.Subject;
import com.command_service.model.dto.SubjectDTO;
import com.command_service.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    SubjectDAO subjectDAO;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    private final KafkaTemplate<String, Object> multiTypeKafkaTemplate;

    public SubjectServiceImpl(KafkaTemplate<String, Object> multiTypeKafkaTemplate) {
        this.multiTypeKafkaTemplate = multiTypeKafkaTemplate;
    }

    @Override
    @Transactional
    public int save(Subject subject){
        this.simpMessagingTemplate.convertAndSend("/topic/subject", subject);
        this.multiTypeKafkaTemplate.send("save", subject);
        return subjectDAO.add(subject);
    }

    @Override
    @Transactional
    public int delete(int subjectID) {

        this.simpMessagingTemplate.convertAndSend("/topic/subject", subjectID);

        return subjectDAO.delete(subjectID);
    }

    @Override
    @Transactional
    public int update(Subject subject) {
        try {
            this.simpMessagingTemplate.convertAndSend("/topic/subject", subject);
        } catch (KafkaException e) {

        }
        return subjectDAO.update(subject);
    }

    @Override
    public Subject getSubjectById(int id) {
        return subjectDAO.getSubjectById(id);
    }

    @Override
    public List<Subject> getAllSubjects() {
        return subjectDAO.getAllSubjects();
    }

    @Override
    public SubjectDTO getSubjectToEdit(int id) {
        return subjectDAO.getSubjectToEdit(id);
    }

}
