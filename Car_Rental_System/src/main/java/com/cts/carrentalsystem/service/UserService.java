package com.cts.carrentalsystem.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.cts.carrentalsystem.dtos.UserDto;

public interface UserService {

	 // Create a new user
	UserDto createUser(UserDto user);

	 // Verify user login details
	Map<String, String> verify(UserDto user);
 
	 // Get all users
	List<UserDto>getAllUsers();

	String deleteUser(long userId);

}
