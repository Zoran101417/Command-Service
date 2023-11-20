package com.command_service.controller;

import com.command_service.exception.CustomException;
import com.command_service.model.Profesor;
import com.command_service.service.ProfesorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/professor")
public class ProfesorController {

    private final Logger LOG = LogManager.getLogger(ProfesorController.class);

    private final ProfesorService profesorService;

    public ProfesorController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<Object> updateProfessor(@RequestBody Profesor profesor) throws CustomException {
        int updateRow = profesorService.update(profesor);
        if(updateRow <= 0) {
            LOG.error("The professor was already updated, reload the record again");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The professor was already updated, reload the record again");
        } else {
            LOG.error("The professor was successfully updated");
            return ResponseEntity.status(HttpStatus.OK).body("The professor was successfully updated");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('DELETE','DELETE ALL')")
    public ResponseEntity<Object> deleteProfessor(@PathVariable int id) {
        int deleteFlag = profesorService.delete(id);
        if(deleteFlag > 0) {
            LOG.info("The professor was successfully deleted");
            return ResponseEntity.status(HttpStatus.OK).body("The professor was successfully deleted");
        } else {
            LOG.error("The professor was not deleted");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The professor was not deleted");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CREATE')")
    public ResponseEntity<Object> saveProfessor(@RequestBody Profesor profesor) {
        int addFlag = profesorService.save(profesor);
        if(addFlag > 0) {
            LOG.info("The professor was successfully added");
            return ResponseEntity.status(HttpStatus.OK).body("The professor was successfully added");
        } else {
            LOG.error("The professor was not added");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The professor was not added");
        }
    }

}
