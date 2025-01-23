package com.cts.carrentalsystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.carrentalsystem.dtos.UserDto;
import com.cts.carrentalsystem.service.UserService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private UserService userService;

    
    // This method handles user registration.
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto user){
        logger.info("Register request received for user: {}", user.getUsername());
        UserDto createdUser = userService.createUser(user);
        logger.info("User registered successfully: {}", createdUser.getUsername());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // This method handles user login.
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDto user){
        logger.info("Login request received for user: {}", user.getUsername());

        return new ResponseEntity<Map<String,String>>(userService.verify(user),HttpStatus.OK);
    }
    
    //This method retrieves all registered users.
    @GetMapping("/")
	@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllusers(){
    	
    	return new ResponseEntity<List<UserDto>>(userService.getAllUsers(),HttpStatus.OK);
    }
    
    //This method delete the registered user.
    @DeleteMapping("/{userId}")
	@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable long userId){
    	return new ResponseEntity<String>(userService.deleteUser(userId),HttpStatus.OK);
    }
}
