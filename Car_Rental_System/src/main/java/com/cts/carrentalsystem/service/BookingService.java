package com.cts.carrentalsystem.service;

import java.util.List;

import com.cts.carrentalsystem.dtos.AllBookingDetailsDto;
import com.cts.carrentalsystem.dtos.BookDto;
import com.cts.carrentalsystem.dtos.BookingDetailsDto;


public interface BookingService {

	// Book a car for a customer
	void booking(String token, long carId, BookDto book);
	
	  // Get booking details for the authenticated user
	List<BookingDetailsDto> getBookingByUserId(String token);

	 // Cancel a booking
	void cancelBooking(String token, long carId);


    // Get all booking details (Admin only)
	List<AllBookingDetailsDto> getAllBookingDetails();


    // Complete a booking (Admin only)
	AllBookingDetailsDto completeBooking( long carId);

}
