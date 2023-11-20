package com.command_service.controller;

import com.command_service.kafka.KafkaSender;
import com.command_service.model.University;
import com.command_service.model.dto.UniversityDTO;
import com.command_service.model.dto.UniversityFacultyDTO;
import com.command_service.service.UniversityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/university", produces = MediaType.APPLICATION_JSON_VALUE)
public class UniversityController {

    private final Logger LOG = LogManager.getLogger(UniversityController.class);

    private final UniversityService universityService;

    private final KafkaSender kafkaSender;

    private final String multiTypeTopicName = "multitype";

    @Autowired
    KafkaTemplate<String, Object> multiTypeKafkaTemplate;

    public UniversityController(UniversityService universityService, KafkaSender kafkaSender) {
        this.universityService = universityService;
        this.kafkaSender = kafkaSender;
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<Object> updateUniversity(@RequestBody University university) {
        int updateRow = universityService.update(university);
        if(updateRow <= 0) {
            LOG.error("The university was already updated, please reload the information again!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The university was already updated, please reload the information again!");
        } else {
            LOG.info("The university was successfully updated");
            return ResponseEntity.status(HttpStatus.OK).body("The university was successfully updated");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('DELETE', 'DELETE ALL')")
    public ResponseEntity<Object> deleteUniversity(@PathVariable int id) {
        int deleteFlag = universityService.delete(id);
        if(deleteFlag > 0) {
            LOG.info("The University was successfully deleted");
            return ResponseEntity.status(HttpStatus.OK).body("The University was successfully deleted");
        } else {
            LOG.error("The University was not deleted");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The University was not deleted");
        }
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('CREATE')")
    public ResponseEntity<Object> saveUniversity(@RequestBody University university) {
        int addFlag = universityService.save(university);
        if(addFlag > 0) {
            LOG.info("The University was successfully added");
            //this.simpMessagingTemplate.convertAndSend("/topic/greetings", university);
            return ResponseEntity.status(HttpStatus.OK).body("The University was successfully added");
        } else {
            LOG.error("The University was not added");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The University was not added");
        }
    }

    @RequestMapping(value = "/saveAll", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveUniversityDTO(@RequestBody UniversityDTO universityDTO){
        return ResponseEntity.status(HttpStatus.OK).body(universityService.saveUniversityDTO(universityDTO));
    }

    @RequestMapping(value = "/saveUniversity", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveUniversityCustomDTO(@RequestBody UniversityFacultyDTO universityFacultyDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(universityService.saveUniversityCustomDTO(universityFacultyDTO));
    }

}
