package com.cts.carrentalsystem.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import com.cts.carrentalsystem.dtos.CarDto;
import com.cts.carrentalsystem.enums.CarStatus;
import com.cts.carrentalsystem.exception.CarAlreadyBooked;
import com.cts.carrentalsystem.exception.CarNotFound;
import com.cts.carrentalsystem.model.Car;
import com.cts.carrentalsystem.repository.CarRepository;
import com.cts.carrentalsystem.service.CarService;

@Service
public class CarServiceImpl implements CarService {
	
	@Autowired
	private CarRepository carRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	
	@Override
	public CarDto createCar(CarDto cars) {
		// TODO Auto-generated method stub
		Car car = new Car();
		car.setModel(cars.getModel());
		car.setBrand(cars.getBrand());
		car.setYear(cars.getYear());
		car.setPricePerDay(cars.getPricePerDay());
		car.setStatus(cars.getStatus());
		
		
		carRepo.save(car);
		
		CarDto savedCar  = new CarDto();
		savedCar.setModel(car.getModel());
		savedCar.setBrand(car.getBrand());
		savedCar.setPricePerDay(car.getPricePerDay());
		savedCar.setYear(car.getYear());
		savedCar.setStatus(car.getStatus());
		
		
		return savedCar;
	}


	@Override
	public List<CarDto> getAllCars() {
		List<Car> cars = carRepo.findAll();
		
		return cars.stream().map(car -> modelMapper.map(car, CarDto.class)).collect(Collectors.toList());
	}


	@Override
	public CarDto getCar(long id) {
		// TODO Auto-generated method stub
		Car car = carRepo.findById(id).orElseThrow(
				()-> new CarNotFound(String.format("Car with the car id %d is not found",id))
				);
		
		
		return modelMapper.map(car, CarDto.class);
	}




	@Override
	public CarDto updateCar(long id, CarDto cars) {
		
		Car car = carRepo.findById(id).orElseThrow(
				()-> new CarNotFound(String.format("Car with the car id %d is not found",id))
				);
		
		car.setBrand(cars.getBrand());
		car.setModel(cars.getModel());
		car.setYear(cars.getYear());
		car.setStatus(cars.getStatus());
		car.setPricePerDay(cars.getPricePerDay());
		
		carRepo.save(car);
		
		return modelMapper.map(car, CarDto.class);
	}


	@Override
	public void deleteCar(long id) {
		// TODO Auto-generated method stub
		Car car = carRepo.findById(id).orElseThrow(
				()-> new CarNotFound(String.format("Car with the car id %d is not found",id))
				);
		if(car.getStatus().equals(CarStatus.AVAILABLE)) {
			carRepo.deleteById(id);
		}
		else {
			throw new CarAlreadyBooked("The Car you are deleting is booked by someone else");
		}
		
		
		
	}
	
}
