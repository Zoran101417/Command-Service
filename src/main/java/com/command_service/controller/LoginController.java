package com.command_service.controller;

import com.command_service.exception.CustomException;
import com.command_service.model.User;
import com.command_service.model.dto.LoginDTO;
import com.command_service.security.TokenManager;
import com.command_service.service.LoginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {

    private static final Logger LOG = LogManager.getLogger(LoginController.class);

    private final LoginService loginService;

    private final TokenManager tokenManager;

    private final AuthenticationManager authenticationManager;

    public LoginController(LoginService loginService, TokenManager tokenManager, AuthenticationManager authenticationManager) {
        this.loginService = loginService;
        this.tokenManager = tokenManager;
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity userLogin(@RequestBody LoginDTO loginCredentials) throws Exception {

        try {

           Authentication authentication =  authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginCredentials.getUsername(), loginCredentials.getPassword()
            ));

        } catch (DisabledException e) {
            LOG.error("User is not active" + e.getMessage());
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            LOG.error("INVALID_CREDENTIALS " + e.getMessage());
            throw new Exception("INVALID_CREDENTIALS", e);
        } catch (Exception e) {
            LOG.error("Authenticate error: " + e.getMessage());
        }

        User loggedUser = loginService.userLogin(loginCredentials);

        if (loggedUser != null) {

        final String jwtToken = tokenManager.generateJwtToken(loggedUser);
            LOG.info("Token: " + jwtToken);
            LOG.info("Token validation :  " + tokenManager.validateJwtTokenAndUser(jwtToken, loggedUser));

        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
        } else {
            LOG.error("INVALID_CREDENTIALS: Incorrect username or password");
            throw new CustomException("INVALID_CREDENTIALS: Incorrect username or password");
        }
    }

    @GetMapping(value = "/validate")
    public ResponseEntity<Object> isTokenValid(@RequestParam("token") String token) throws CustomException {
        return ResponseEntity.status(HttpStatus.OK).body(tokenManager.validateJwtToken(token));
    }

}
