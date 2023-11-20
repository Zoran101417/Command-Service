package com.command_service.service.impl;


import com.command_service.dao.StudentDAO;
import com.command_service.exception.KafkaException;
import com.command_service.model.Student;
import com.command_service.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentDAO studentDAO;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    private final KafkaTemplate<String, Object> multiTypeKafkaTemplate;

    public StudentServiceImpl(KafkaTemplate<String, Object> multiTypeKafkaTemplate) {
        this.multiTypeKafkaTemplate = multiTypeKafkaTemplate;
    }

    @Override
    @Transactional
    public int save(Student student) {
        try {
            this.simpMessagingTemplate.convertAndSend("/topic/student", student);
            this.multiTypeKafkaTemplate.send("save", student);
        }catch (KafkaException e) {

        }
        return studentDAO.add(student);
    }

    @Override
    @Transactional
    public int delete(int studentID) {
        try {
            this.simpMessagingTemplate.convertAndSend("/topic/student", studentID);
        }catch (KafkaException e) {

        }
        return studentDAO.delete(studentID);
    }

    @Override
    @Transactional
    public int update(Student student) {
        try {
            this.simpMessagingTemplate.convertAndSend("/topic/student", student);

        }catch (KafkaException e) {

        }
        return studentDAO.update(student);
    }

    @Override
    public Student getStudentById(int id) {
        return studentDAO.getStudentById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }


}
