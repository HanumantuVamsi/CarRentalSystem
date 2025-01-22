package com.cts.carrentalsystem.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.carrentalsystem.dtos.ReviewDto;
import com.cts.carrentalsystem.service.ReviewService;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
	
	@Autowired
	ReviewService service;
   
	 // Create a new review
	@PostMapping("/{carId}")
	public ResponseEntity<String> createReview(@RequestHeader("Authorization") String token,@PathVariable("carId") long carId,@RequestBody ReviewDto reviewDto){
		
		return new ResponseEntity<String>(service.createReview(carId,reviewDto,token),HttpStatus.OK);
	}
	
	 // Get all reviews for a specific car
	@GetMapping("/{carId}")
	public ResponseEntity<List<ReviewDto>> getAllReviews(@RequestHeader("Authorization") String token,@PathVariable("carId") long carId){
		return new ResponseEntity<List<ReviewDto>>(service.getAllReviews(token,carId),HttpStatus.OK);
	}
}
