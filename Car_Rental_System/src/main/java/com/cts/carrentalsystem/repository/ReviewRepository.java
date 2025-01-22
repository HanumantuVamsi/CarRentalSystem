package com.cts.carrentalsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.carrentalsystem.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	// Find reviews by car ID
	List<Review> findByCarId(long carId);

}
