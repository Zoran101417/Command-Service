package com.command_service.service.impl;

import com.command_service.dao.UserDAO;
import com.command_service.exception.CustomException;
import com.command_service.exception.GenericException;
import com.command_service.exception.KafkaException;
import com.command_service.model.User;
import com.command_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    private final KafkaTemplate<String, Object> multiTypeKafkaTemplate;

    public UserServiceImpl(KafkaTemplate<String, Object> multiTypeKafkaTemplate) {
        this.multiTypeKafkaTemplate = multiTypeKafkaTemplate;
    }

    @Override
    @Transactional(rollbackFor = GenericException.class)
    public User save(User user) throws GenericException {

        User createdUser = null;
        try {
            createdUser = userDAO.add(user);

            this.multiTypeKafkaTemplate.send("save", user);
        } catch(Exception e) {
            System.out.println("Exception: " + e.getMessage());
            throw new GenericException("An error occurred while saving the user", e);
        }
        return createdUser;
    }

    @Override
    @Transactional
    public User update(User user) {
       User updeatedUser =  userDAO.update(user);
        try {
            this.simpMessagingTemplate.convertAndSend("/topic/user", user);
            this.multiTypeKafkaTemplate.send("save", user);
        }catch (KafkaException e) {

        }
        return updeatedUser;
    }

    @Override
    @Transactional
    public void delete(int userID) {
        userDAO.delete(userID);
        try {
            this.simpMessagingTemplate.convertAndSend("/topic/user", userID);
        }catch (KafkaException e) {

        }
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public User getUserById(int id) throws CustomException {
        return userDAO.getUserById(id);
    }

    @Override
    public User getUserByNameAndPassword(String username, String password) {
        return userDAO.getUserByNameAndPassword(username, password);
    }

    @Override
    public List<String> getUserPermissionsByEmail(String userMail) {
        return userDAO.getUserPermissionsByEmail(userMail);
    }

}
