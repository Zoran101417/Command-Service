package com.command_service.service;

import com.command_service.model.User;
import com.command_service.model.dto.LoginDTO;

public interface LoginService {

    User userLogin(LoginDTO loginCredentials);
}
