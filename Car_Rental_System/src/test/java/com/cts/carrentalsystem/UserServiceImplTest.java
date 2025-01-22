package com.cts.carrentalsystem;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cts.carrentalsystem.dtos.UserDto;
import com.cts.carrentalsystem.enums.UserRole;
import com.cts.carrentalsystem.model.Users;
import com.cts.carrentalsystem.repository.UserRepository;
import com.cts.carrentalsystem.security.JWTService;
import com.cts.carrentalsystem.serviceimpl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private JWTService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private Users user;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        user = new Users();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("Password123!");
        user.setRole(UserRole.CUSTOMER);

        userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setEmail("testuser@example.com");
        userDto.setPassword("Password123!");
        userDto.setRole(UserRole.CUSTOMER);
    }

    @Test
    public void testCreateUser() {
        // True scenario
        when(userRepo.save(any(Users.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("EncodedPassword123!");

        UserDto savedUser = userService.createUser(userDto);

        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        verify(userRepo, times(1)).save(any(Users.class));

        // False scenario - Email already in use
        when(userRepo.existsByEmail(anyString())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDto));
    }

    @Test
    public void testVerifyUser() {
        // Mock the authentication process
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtService.generateToken(anyString())).thenReturn("fake-jwt-token");
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));

        // True scenario
        Map<String, String> response = userService.verify(userDto);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.get("token"));
        assertEquals("CUSTOMER", response.get("role"));

        // False scenario - Authentication failed
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));
        assertThrows(RuntimeException.class, () -> userService.verify(userDto));
    }

    @Test
    public void testGetAllUsers() {
        // True scenario
        List<Users> users = Arrays.asList(user);
        when(userRepo.findAll()).thenReturn(users);
//        when(modelMapper.map(any(Users.class), eq(UserDto.class))).thenReturn(userDto);

        List<UserDto> userDtos = userService.getAllUsers();

        assertNotNull(userDtos);
        assertEquals(1, userDtos.size());
        verify(userRepo, times(1)).findAll();

        // False scenario
        when(userRepo.findAll()).thenThrow(new RuntimeException("Error retrieving users"));
        assertThrows(RuntimeException.class, () -> userService.getAllUsers());
    }
}
