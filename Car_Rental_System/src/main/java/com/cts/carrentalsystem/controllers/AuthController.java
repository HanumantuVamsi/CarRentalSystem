package com.cts.carrentalsystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.carrentalsystem.dtos.UserDto;
import com.cts.carrentalsystem.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
	
	@Autowired
	private UserService userService;
	

	@PostMapping("/register")
	public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto user){
	   return new ResponseEntity<>(userService.createUser(user),HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserDto user){
		return new ResponseEntity<String>(userService.verify(user),HttpStatus.OK);
	}
	
}
