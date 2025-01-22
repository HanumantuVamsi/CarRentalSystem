package com.cts.carrentalsystem.security;

import com.cts.carrentalsystem.dtos.UserPrincipal;
import com.cts.carrentalsystem.model.Users;
import com.cts.carrentalsystem.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
//this implements the UserdetailsService interface to load the username and return UserDetails object for authentication manager
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByEmail(username).orElseThrow(
        		()-> new UsernameNotFoundException("username is not found")
        		);
        
        
        return new UserPrincipal(user);
    }
}