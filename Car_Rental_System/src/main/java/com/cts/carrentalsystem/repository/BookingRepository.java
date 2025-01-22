package com.cts.carrentalsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.carrentalsystem.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	 // Find bookings by user ID
	List<Booking> findByUserId(long userId);


    // Find bookings by user ID and car ID
	List<Booking> findByUserIdAndCarId(long userId, long carId);

	

}
