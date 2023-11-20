package com.command_service.service.impl;

import com.command_service.model.User;
import com.command_service.model.dto.LoginDTO;
import com.command_service.service.LoginService;
import com.command_service.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserService userService;

    public LoginServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User userLogin(LoginDTO loginCredentials) {
        return userService.getUserByNameAndPassword(loginCredentials.getUsername(), loginCredentials.getPassword());
    }
}
