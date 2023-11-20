package com.command_service.controller;

import com.command_service.exception.GenericException;
import com.command_service.model.User;
import com.command_service.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final Logger LOG = LogManager.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping
    @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<Object> updateUser(@RequestBody User user) {
        User updatedUser = userService.update(user);
        if(updatedUser != null) {
            LOG.info("User was successfully updated");
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            LOG.error("User was already updated, reload the user again!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User was already updated, reload the user again!");
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE')")
    public ResponseEntity<Object> createUser(@RequestBody User user) throws GenericException {
        User createUser = userService.save(user);
        return new ResponseEntity<>(createUser, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('DELETE', 'DELETE ALL')")
    public ResponseEntity<Object> deleteStudent(@PathVariable int id) {
        int deleteFlag = 3;
                userService.delete(id);
        if(deleteFlag > 0) {
            LOG.info("The student was successfully deleted");
            return ResponseEntity.status(HttpStatus.OK).body("The student was successfully deleted");
        } else {
            LOG.error("The student was not deleted");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The student was not deleted");
        }
    }

    @GetMapping(value = "/permissions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> getUserPermissions(@RequestParam String userEmail){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserPermissionsByEmail(userEmail));
    }

}
