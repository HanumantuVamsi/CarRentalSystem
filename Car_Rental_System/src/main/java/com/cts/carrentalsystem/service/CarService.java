package com.cts.carrentalsystem.service;

import java.util.List;

import com.cts.carrentalsystem.dtos.CarDto;

public interface CarService {

	CarDto createCar(CarDto cars);

	List<CarDto> getAllCars();

	CarDto getCar(long id);

	CarDto updateCar(long id, CarDto cars);

	void deleteCar(long id);

}
