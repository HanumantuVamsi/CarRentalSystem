package com.cts.carrentalsystem.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.carrentalsystem.dtos.CarDto;
import com.cts.carrentalsystem.service.CarService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cars")
@Validated
public class CarController {
	
	@Autowired
	private CarService service;
    
	
	// Create a new car (Admin only)
	@PostMapping("/")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<CarDto> createCar(@Valid @RequestBody CarDto cars){
		
		return new ResponseEntity<CarDto>(service.createCar(cars),HttpStatus.CREATED);
	}
	
	// Get all cars (Admin and Customer)
	@GetMapping("/")
	@PreAuthorize("hasAnyAuthority('ADMIN','CUSTOMER')")
	public ResponseEntity<List<CarDto>> getAllCars(){
		
		return new ResponseEntity<List<CarDto>>(service.getAllCars(),HttpStatus.OK);
	}
	
	 // Get car by ID (Admin and Customer)
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN','CUSTOMER')")
	public ResponseEntity<CarDto> getCar(@PathVariable("id") long id){
		return new ResponseEntity<CarDto>(service.getCar(id),HttpStatus.OK);
	}
	
	 // Update car details (Admin only)
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<CarDto> updateCar(@PathVariable("id") long id,@Valid @RequestBody CarDto cars){
		
		return new ResponseEntity<CarDto>(service.updateCar(id,cars),HttpStatus.OK);
	}
	
     // Delete a car (Admin only)
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> deleteCar(@PathVariable("id") long id){
		service.deleteCar(id);
		
		return new ResponseEntity<String>("Car is Deleted Successfully",HttpStatus.OK);
	}
}
