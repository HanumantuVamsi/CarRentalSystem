package com.cts.carrentalsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.carrentalsystem.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByUserId(long userId);

	List<Booking> findByUserIdAndCarId(long userId, long carId);

	

}
