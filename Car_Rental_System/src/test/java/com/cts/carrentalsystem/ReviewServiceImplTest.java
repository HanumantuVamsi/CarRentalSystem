package com.cts.carrentalsystem;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.carrentalsystem.dtos.ReviewDto;
import com.cts.carrentalsystem.enums.UserRole;
import com.cts.carrentalsystem.exception.CarNotFound;
import com.cts.carrentalsystem.exception.UserNotFound;
import com.cts.carrentalsystem.model.Car;
import com.cts.carrentalsystem.model.Review;
import com.cts.carrentalsystem.model.Users;
import com.cts.carrentalsystem.repository.CarRepository;
import com.cts.carrentalsystem.repository.ReviewRepository;
import com.cts.carrentalsystem.repository.UserRepository;
import com.cts.carrentalsystem.security.JWTService;
import com.cts.carrentalsystem.serviceimpl.ReviewServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {

    @Mock
    private CarRepository carRepo;

    @Mock
    private JWTService jwtService;

    @Mock
    private UserRepository userRepo;

    @Mock
    private ReviewRepository reviewRepo;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Car car;
    private Users user;
    private Review review;
    private ReviewDto reviewDto;

    @BeforeEach
    public void setUp() {
        car = new Car();
        car.setId(1L);
        car.setModel("Model X");
        car.setBrand("Tesla");

        user = new Users();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("Password123!");
        user.setRole(UserRole.CUSTOMER);

        review = new Review();
        review.setCar(car);
        review.setUser(user);
        review.setComment("Great car!");
        review.setRating(5);

        reviewDto = new ReviewDto();
        reviewDto.setComment("Great car!");
        reviewDto.setRating(5);
        reviewDto.setUsername("testuser");
    }

    @Test
    public void testCreateReview() {
        // True scenario
        when(carRepo.findById(anyLong())).thenReturn(Optional.of(car));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(reviewRepo.save(any(Review.class))).thenReturn(review);
        when(jwtService.extractUserName(anyString())).thenReturn(user.getEmail());
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));

        String result = reviewService.createReview(1L, reviewDto, "Bearer token");

        assertNotNull(result);
        assertEquals("Review Saved Successfully", result);
        verify(reviewRepo, times(1)).save(any(Review.class));

        // False scenario - Car not found
        when(carRepo.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(CarNotFound.class, () -> reviewService.createReview(1L, reviewDto, "Bearer token"));

        // False scenario - User not found
        when(carRepo.findById(anyLong())).thenReturn(Optional.of(car));
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(UserNotFound.class, () -> reviewService.createReview(1L, reviewDto, "Bearer token"));
    }

    @Test
    public void testGetAllReviews() {
        // True scenario
        when(carRepo.findById(anyLong())).thenReturn(Optional.of(car));
        when(reviewRepo.findByCarId(anyLong())).thenReturn(Arrays.asList(review));

        List<ReviewDto> reviewDtos = reviewService.getAllReviews("Bearer token", 1L);

        assertNotNull(reviewDtos);
        assertEquals(1, reviewDtos.size());
        verify(reviewRepo, times(1)).findByCarId(anyLong());

        // False scenario - Car not found
        when(carRepo.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(CarNotFound.class, () -> reviewService.getAllReviews("Bearer token", 1L));
    }
}
