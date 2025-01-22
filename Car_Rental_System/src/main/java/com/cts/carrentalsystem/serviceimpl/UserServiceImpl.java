package com.cts.carrentalsystem.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cts.carrentalsystem.dtos.CarDto;
import com.cts.carrentalsystem.dtos.UserDto;
import com.cts.carrentalsystem.enums.UserRole;
import com.cts.carrentalsystem.model.Users;
import com.cts.carrentalsystem.repository.UserRepository;
import com.cts.carrentalsystem.security.JWTService;
import com.cts.carrentalsystem.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private JWTService jwtService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	 // Create a new user
	@Override
	public UserDto createUser(UserDto userDto) {
		logger.info("Creating user: {}", userDto.getUsername());

		validateUserDto(userDto);
		Users user = mapToEntity(userDto);
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));

		Optional<Users> adminUser = userRepo.findByRole(UserRole.ADMIN);
		if (adminUser.isEmpty()) {
			user.setRole(UserRole.ADMIN);
		} else {
			user.setRole(UserRole.CUSTOMER);
		}

		Users savedUser = userRepo.save(user);
		logger.info("User created successfully: {}", savedUser.getUsername());

		return mapToDto(savedUser);
	}

	//converting userdto to users
	private Users mapToEntity(UserDto userDto) {
		Users user = new Users();
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		return user;
	}

	//converting users to userdto
	private UserDto mapToDto(Users user) {
		UserDto userDto = new UserDto();
		userDto.setUsername(user.getUsername());
		userDto.setEmail(user.getEmail());
		userDto.setPassword(user.getPassword());
		userDto.setRole(user.getRole());
		return userDto;
	}

	//validating the input data from user
	private void validateUserDto(UserDto userDto) {
		logger.info("Validating user data: {}", userDto.getUsername());

		if (!StringUtils.hasText(userDto.getUsername())) {
			logger.error("Username is mandatory");
			throw new IllegalArgumentException("Username is mandatory");
		}
		if (!StringUtils.hasText(userDto.getEmail()) || !isValidEmail(userDto.getEmail())) {
			logger.error("Valid email is mandatory");
			throw new IllegalArgumentException("Valid email is mandatory");
		}
		if (userRepo.existsByEmail(userDto.getEmail())) {
			logger.error("Email is already in use: {}", userDto.getEmail());
			throw new IllegalArgumentException("Email is already in use");
		}
		if (!StringUtils.hasText(userDto.getPassword()) || !isValidPassword(userDto.getPassword())) {
			logger.error("Invalid password format");
			throw new IllegalArgumentException(
					"Password must be at least 8 characters long and contain uppercase, lowercase, digit, and special character");
		}
	}

	//validation for valid user
	private boolean isValidEmail(String email) {
		String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
		return email.matches(emailRegex);
	}

	//validation for valid password
	private boolean isValidPassword(String password) {
		return password.length() >= 8 && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*")
				&& password.matches(".*\\d.*") && password.matches(".*[!@#\\$%\\^&\\*].*");
	}

	 // Verify user login details
	@Override
	public Map<String, String> verify(UserDto user) {
		logger.info("Verifying user: {}", user.getEmail());
		Map<String, String> response = new HashMap<>();
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
		if (authentication.isAuthenticated()) {
			String token = jwtService.generateToken(user.getEmail());
			logger.info("User verified successfully: {}", user.getEmail());
			String role = getUserRole(user.getEmail());
			
			response.put("token", token);
			response.put("role", role);
			return response;
		} else {
			return response;
		}
	}

	//getting role form the users table
	public String getUserRole(String email) {
		Users user = userRepo.findByEmail(email).get();
		return user.getRole().name();
	}

	  // Get all users
	@Override
	public List<UserDto> getAllUsers() {
		// TODO Auto-generated method stub
		List<Users> users = userRepo.findAll();
        
		List<UserDto> userDtos = new ArrayList<>();
		
		for(Users user:users) {
			if(user.getRole().equals(UserRole.CUSTOMER))
			userDtos.add(mapToDto(user));
		}
		return userDtos;
	}

}
