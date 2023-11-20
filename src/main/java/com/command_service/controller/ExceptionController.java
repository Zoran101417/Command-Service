package com.command_service.controller;

import com.command_service.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;

@CrossOrigin(origins = "*", maxAge = 3600)
@ControllerAdvice
public class ExceptionController {

    private final Logger LOG = LogManager.getLogger(ExceptionController.class);

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<Object> exception(CustomException exception) {
        LOG.error(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionClass(Exception exception) {
        LOG.error("Exception: " + exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    protected ResponseEntity<Object> handleGeneralException(AccessDeniedException exception) {
        LOG.error("Access is denied, no authorization found: " + exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<Object> authenticateException(Header header, Claims claims, String message){
        LOG.error("Authentication error, Token has expired: " + message);
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }

}
