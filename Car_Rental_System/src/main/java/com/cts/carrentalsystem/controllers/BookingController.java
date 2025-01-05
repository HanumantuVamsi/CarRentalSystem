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
     
	@PostMapping("/{user_id}/booking/{car_id}")
	@PreAuthorize("hasAuthority('CUSTOMER')")
	public ResponseEntity<String> booking(@PathVariable("user_id") long userId,@PathVariable("car_id") long carId,@Valid @RequestBody BookDto book){
		
		service.booking(userId,carId,book);
		
		return new ResponseEntity<String>("Booking is Succesfull",HttpStatus.OK);
	}
	
	@GetMapping("/{user_id}")
	@PreAuthorize("hasAuthority('CUSTOMER')")
	public ResponseEntity<List<BookingDetailsDto>>getBookingByUserId(@PathVariable("user_id") long userId){
		
		
		return new ResponseEntity<List<BookingDetailsDto>>(service.getBookingByUserId(userId),HttpStatus.OK);
	}
	
    @PutMapping("/{user_id}/booking/{car_id}/cancel")
	@PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<String>cancleBooking(@PathVariable("user_id")long userId,@PathVariable("car_id")long carId){
    	service.cancelBooking(userId,carId);
    	return new ResponseEntity<String>("Booking is Cancelled SuccessFully",HttpStatus.OK);
    }
    
    @PutMapping("/{user_id}/booking/{car_id}/complete")
    public ResponseEntity<String>completeBooking(@PathVariable("user_id")long userId,@PathVariable("car_id")long carId){
    	service.completeBooking(userId,carId);
    	return new ResponseEntity<String>(service.completeBooking(userId,carId),HttpStatus.OK);
    }
	
    @GetMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<AllBookingDetailsDto>>getAllBookings(){
    	return new ResponseEntity<List<AllBookingDetailsDto>>(service.getAllBookingDetails(),HttpStatus.OK);
    }
}
