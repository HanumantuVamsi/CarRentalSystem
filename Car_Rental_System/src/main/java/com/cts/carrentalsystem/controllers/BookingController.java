package com.cts.carrentalsystem.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.cts.carrentalsystem.dtos.AllBookingDetailsDto;
import com.cts.carrentalsystem.dtos.BookDto;
import com.cts.carrentalsystem.dtos.BookingDetailsDto;
import com.cts.carrentalsystem.model.Booking;
import com.cts.carrentalsystem.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
@Validated
public class BookingController {
	
	@Autowired
	private BookingService service;
     
	 // Book a car for a customer
	@PostMapping("/booking/{car_id}")
	@PreAuthorize("hasAuthority('CUSTOMER')")
	public ResponseEntity<String> booking(@PathVariable("car_id") long carId,@Valid @RequestBody BookDto book,@RequestHeader("Authorization") String token){
		
		service.booking(token,carId,book);
		
		return new ResponseEntity<String>("Booking is Succesfull",HttpStatus.OK);
	}
	
	// Get booking details for the authenticated user
	@GetMapping("/id")
	@PreAuthorize("hasAuthority('CUSTOMER')")
	public ResponseEntity<List<BookingDetailsDto>>getBookingByUserToken(@RequestHeader("Authorization") String token){
		
		
		return new ResponseEntity<List<BookingDetailsDto>>(service.getBookingByUserToken(token),HttpStatus.OK);
	}
	
	// Get booking details of the user by user id
	@GetMapping("/{userId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<List<BookingDetailsDto>>getBookingByUserId(@PathVariable long userId){
		
		
		return new ResponseEntity<List<BookingDetailsDto>>(service.getBookingByUserId(userId),HttpStatus.OK);
	}
	
	 // Cancel a booking 
    @PutMapping("/booking/{book_id}/cancel")
	@PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<String>cancleBooking(@RequestHeader("Authorization") String token,@PathVariable("book_id")long bookId){
    	service.cancelBooking(token,bookId);
    	return new ResponseEntity<String>("Booking is Cancelled SuccessFully",HttpStatus.OK);
    }
    
    // Mark a booking as complete (Admin only)
    @PutMapping("/booking/{book_id}/complete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AllBookingDetailsDto>completeBooking(@PathVariable("book_id")long bookId){

    	return new ResponseEntity<AllBookingDetailsDto>(service.completeBooking(bookId),HttpStatus.OK);
    }
	
    
    // Get all booking details (Admin only)
    @GetMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<AllBookingDetailsDto>>getAllBookings(){
    	return new ResponseEntity<List<AllBookingDetailsDto>>(service.getAllBookingDetails(),HttpStatus.OK);
    }
}
