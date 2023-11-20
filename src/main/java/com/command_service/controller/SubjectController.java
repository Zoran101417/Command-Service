package com.command_service.controller;

import com.command_service.model.Subject;
import com.command_service.service.SubjectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/subject", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubjectController {

    private final Logger LOG = LogManager.getLogger(SubjectController.class);

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<Object> updateSubject(@RequestBody Subject subject) {
        int updateRow = subjectService.update(subject);
        if(updateRow > 0) {
            LOG.info("The Subject was successfully updated");
            return ResponseEntity.status(HttpStatus.OK).body("The Subject was successfully updated");
        } else {
            LOG.error("The subject was already updated, reload the subject data again!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The subject was already updated, reload the subject data again!");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('DELETE', 'DELETE ALL')")
    public ResponseEntity<Object> deleteSubject(@PathVariable int id) {
        int deleteFlag = subjectService.delete(id);
        if(deleteFlag > 0) {
            LOG.info("The subject was successfully deleted");
            return ResponseEntity.status(HttpStatus.OK).body("The subject was successfully deleted");
        } else {
            LOG.error("The subject was not deleted");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The subject was not deleted");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CREATE')")
    public ResponseEntity<Object> saveSubject(@RequestBody Subject subject) {
        int addFlag = subjectService.save(subject);
        if(addFlag > 0) {
            LOG.info("The subject was successfully added");
            return ResponseEntity.status(HttpStatus.OK).body("The subject was successfully added");
        } else {
            LOG.error("The subject was not added");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The subject was not added");
        }
    }

}
