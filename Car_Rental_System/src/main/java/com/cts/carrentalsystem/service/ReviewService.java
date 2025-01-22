package com.cts.carrentalsystem.service;

import java.util.List;

import com.cts.carrentalsystem.dtos.ReviewDto;

public interface ReviewService {

	String createReview(long carId, ReviewDto reviewDto, String token);

	List<ReviewDto> getAllReviews(String token, long carId);

}
