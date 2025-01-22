package com.cts.carrentalsystem.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.carrentalsystem.dtos.ReviewDto;
import com.cts.carrentalsystem.exception.CarNotFound;
import com.cts.carrentalsystem.exception.UserNotFound;
import com.cts.carrentalsystem.model.Car;
import com.cts.carrentalsystem.model.Review;
import com.cts.carrentalsystem.model.Users;
import com.cts.carrentalsystem.repository.CarRepository;
import com.cts.carrentalsystem.repository.ReviewRepository;
import com.cts.carrentalsystem.repository.UserRepository;
import com.cts.carrentalsystem.security.JWTService;
import com.cts.carrentalsystem.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

	 @Autowired
	 private CarRepository carRepo;
	 
	 @Autowired
	 private JWTService service;
	 
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private ReviewRepository reviewRepo;
 
	@Override
	public String createReview(long carId, ReviewDto reviewDto,String token) {
		Review review = new Review();
		
		 Car car = carRepo.findById(carId).orElseThrow(
	                ()-> new CarNotFound(String.format("Car with the car id %d is not found",carId))
	                );
		 review.setCar(car);
		 review.setComment(reviewDto.getComment());
		 review.setRating(reviewDto.getRating());
		 long userId = getUserIdFromToken(token);
		 Users user = userRepo.findById(userId)
	                .orElseThrow(() -> new UserNotFound(String.format("User Id %d is not found", userId)));
		 review.setUser(user);
		 
		 reviewRepo.save(review);
		 
		return "Review Saved Successfully";
	}
	
	
	public long getUserIdFromToken(String token)   { 
    	String email = service.extractUserName(token.substring(7)); // Remove 'Bearer ' prefix 
    	Users user = userRepo.findByEmail(email).orElseThrow(
    			()->new UserNotFound("Used with this emil not found")
    			);
        return user.getId();
    }


	@Override
	public List<ReviewDto> getAllReviews(String token, long carId) {
		// TODO Auto-generated method stub
		
		 Car car = carRepo.findById(carId).orElseThrow(
	                ()-> new CarNotFound(String.format("Car with the car id %d is not found",carId))
	                );
		
		List<Review> reviews =  reviewRepo.findByCarId(carId);
		
		List<ReviewDto> reviewDtos = new ArrayList<>();
		for(Review review : reviews) {
			ReviewDto reviewDto = new ReviewDto();
			reviewDto.setComment(review.getComment());
			reviewDto.setRating(review.getRating());
			reviewDto.setUsername(review.getUser().getUsername());
			
			reviewDtos.add(reviewDto);
		}
		
		return reviewDtos;
	}
    

}
