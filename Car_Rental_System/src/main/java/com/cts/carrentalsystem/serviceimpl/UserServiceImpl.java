package com.cts.carrentalsystem.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.carrentalsystem.dtos.UserDto;
import com.cts.carrentalsystem.enums.UserRole;
import com.cts.carrentalsystem.model.Users;
import com.cts.carrentalsystem.repository.UserRepository;
import com.cts.carrentalsystem.security.JWTService;
//import com.cts.carrentalsystem.security.JWTService;
import com.cts.carrentalsystem.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	
	
    @Autowired
    private JWTService jwtService;
	

	@Autowired
	private PasswordEncoder passwordEncoder;
	

	@Autowired
	private AuthenticationManager authenticationManager;


	@Override
	public UserDto createUser(UserDto user1) {
		
		
	    Users user = new Users();
	    user.setUsername(user1.getUsername());
	    user.setEmail(user1.getEmail());
	    user.setPassword(passwordEncoder.encode(user1.getPassword()));
	    
	    Optional<Users> adminUser = userRepo.findByRole(UserRole.ADMIN); 
	    if (adminUser.isEmpty()) 
	    { 
	    	user.setRole(UserRole.ADMIN);
	    } 
	    else { 
	    	user.setRole(UserRole.CUSTOMER); 
	    }
	    
	    
	    
	    userRepo.save(user);
	    
	    UserDto savedUser = new UserDto();
	    savedUser.setUsername(user.getUsername());
	    savedUser.setEmail(user.getEmail());
	    savedUser.setPassword(user.getPassword());
	    
		return savedUser;
	}


	@Override
	public String verify(UserDto user) {
		 Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
	        if (authentication.isAuthenticated()) {
	            return jwtService.generateToken(user.getEmail());
	        } else {
	            return "fail";
	        }
	}
	

}
