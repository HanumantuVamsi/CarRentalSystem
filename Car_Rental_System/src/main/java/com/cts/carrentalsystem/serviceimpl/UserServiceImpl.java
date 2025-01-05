package com.cts.carrentalsystem.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    private Users mapToEntity(UserDto userDto) {
        Users user = new Users();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return user;
    }

    private UserDto mapToDto(Users user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        return userDto;
    }

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

    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*")
                && password.matches(".*\\d.*") && password.matches(".*[!@#\\$%\\^&\\*].*");
    }

    @Override
    public String verify(UserDto user) {
        logger.info("Verifying user: {}", user.getEmail());
        
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        
        String token = jwtService.generateToken(user.getEmail());
        logger.info("User verified successfully: {}", user.getEmail());
        
        return token;
    }
}
