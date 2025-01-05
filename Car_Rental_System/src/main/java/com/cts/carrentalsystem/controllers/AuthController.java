package com.cts.carrentalsystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.carrentalsystem.dtos.UserDto;
import com.cts.carrentalsystem.service.UserService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto user){
        logger.info("Register request received for user: {}", user.getUsername());
        UserDto createdUser = userService.createUser(user);
        logger.info("User registered successfully: {}", createdUser.getUsername());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto user){
        logger.info("Login request received for user: {}", user.getUsername());
        String result = userService.verify(user);
        if (result != null) {
            logger.info("User logged in successfully: {}", user.getUsername());
        } else {
            logger.warn("Failed login attempt for user: {}", user.getUsername());
        }
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }
}
