package com.cts.carrentalsystem.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.cts.carrentalsystem.dtos.UserDto;

public interface UserService {


	 UserDto createUser(UserDto user);

	Map<String, String> verify(UserDto user);

	List<UserDto>getAllUsers();

}
