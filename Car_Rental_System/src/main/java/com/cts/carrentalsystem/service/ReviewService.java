package com.cts.carrentalsystem.service;

import java.util.List;

import com.cts.carrentalsystem.dtos.ReviewDto;

public interface ReviewService {

	// Create a new review for a car
	String createReview(long carId, ReviewDto reviewDto, String token);

	// Get all reviews for a specific car
	List<ReviewDto> getAllReviews(String token, long carId);

}
