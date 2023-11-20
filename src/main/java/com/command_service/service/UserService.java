package com.command_service.service;

import com.command_service.exception.CustomException;
import com.command_service.exception.GenericException;
import com.command_service.model.User;

import java.util.List;

public interface UserService {

    User save(User user) throws GenericException;

    User update(User user);

    void delete(int facultyID);

    List<User> getAllUsers();

    User getUserById(int id) throws CustomException;

    User getUserByNameAndPassword(String username, String password);

    List<String> getUserPermissionsByEmail(String userMail);
}
