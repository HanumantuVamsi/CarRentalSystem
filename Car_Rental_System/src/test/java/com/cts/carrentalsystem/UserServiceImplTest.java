package com.cts.carrentalsystem;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private Users user;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setEmail("test@example.com");
        userDto.setPassword("Test@1234");

        user = new Users();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
    }

    @Test
    public void testCreateUser() {
        when(userRepo.findByRole(UserRole.ADMIN)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepo.save(any(Users.class))).thenReturn(user);

        UserDto createdUser = userService.createUser(userDto);

        assertEquals(userDto.getUsername(), createdUser.getUsername());
        assertEquals(userDto.getEmail(), createdUser.getEmail());
        verify(userRepo, times(1)).save(any(Users.class));
    }

    @Test
    public void testCreateUserAdminExists() {
        when(userRepo.findByRole(UserRole.ADMIN)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepo.save(any(Users.class))).thenAnswer(invocation -> {
            Users savedUser = invocation.getArgument(0);
            assertEquals(UserRole.CUSTOMER, savedUser.getRole());
            return savedUser;
        });

        UserDto createdUser = userService.createUser(userDto);

        assertEquals(UserRole.CUSTOMER, createdUser.getRole());
        verify(userRepo, times(1)).save(any(Users.class));
    }


    @Test
    public void testVerifySuccess() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(any(String.class))).thenReturn("jwtToken");

  //      String token = userService.verify(userDto);

//        assertEquals("jwtToken", token);
    }

    @Test
    public void testVerifyFail() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

//        String token = userService.verify(userDto);

//        assertEquals("fail", token);
    }

    @Test
    public void testValidateUserDto() {
        userDto.setEmail("invalidEmail");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(userDto);
        });

        assertEquals("Valid email is mandatory", exception.getMessage());
    }
}
