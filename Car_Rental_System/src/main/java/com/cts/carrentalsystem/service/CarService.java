package com.cts.carrentalsystem.service;

import java.util.List;

import com.cts.carrentalsystem.dtos.CarDto;

public interface CarService {


    // Create a new car
	CarDto createCar(CarDto cars);

	// Get all cars
	List<CarDto> getAllCars();

	// Get car by ID
	CarDto getCar(long id);

	 // Update car details
	CarDto updateCar(long id, CarDto cars);


    // Delete a car
	void deleteCar(long id);

}
