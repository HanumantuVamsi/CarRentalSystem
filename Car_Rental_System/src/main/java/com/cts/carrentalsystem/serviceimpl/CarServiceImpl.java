package com.cts.carrentalsystem.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cts.carrentalsystem.dtos.CarDto;
import com.cts.carrentalsystem.enums.CarStatus;
import com.cts.carrentalsystem.exception.CarAlreadyBooked;
import com.cts.carrentalsystem.exception.CarNotFound;
import com.cts.carrentalsystem.model.Car;
import com.cts.carrentalsystem.repository.CarRepository;
import com.cts.carrentalsystem.service.CarService;

@Service
public class CarServiceImpl implements CarService {
    
    private static final Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);
    
    @Autowired
    private CarRepository carRepo;
    
    @Autowired
    private ModelMapper modelMapper;

    
    @Override
    public CarDto createCar(CarDto cars) {
        logger.info("Creating car with model: {}", cars.getModel());
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
        
        logger.info("Car created successfully: {}", savedCar);
        return savedCar;
    }


    @Override
    public List<CarDto> getAllCars() {
        logger.info("Retrieving all cars");
        List<Car> cars = carRepo.findAll();
        List<CarDto> carDtos = cars.stream().map(car -> modelMapper.map(car, CarDto.class)).collect(Collectors.toList());
        logger.info("Retrieved {} cars", carDtos.size());
        return carDtos;
    }


    @Override
    public CarDto getCar(long id) {
        logger.info("Retrieving car with ID: {}", id);
        Car car = carRepo.findById(id).orElseThrow(
                ()-> new CarNotFound(String.format("Car with the car id %d is not found",id))
                );
        
        CarDto carDto = modelMapper.map(car, CarDto.class);
        logger.info("Retrieved car: {}", carDto);
        return carDto;
    }




    @Override
    public CarDto updateCar(long id, CarDto cars) {
        logger.info("Updating car with ID: {}", id);
        
        Car car = carRepo.findById(id).orElseThrow(
                ()-> new CarNotFound(String.format("Car with the car id %d is not found",id))
                );
        
        car.setBrand(cars.getBrand());
        car.setModel(cars.getModel());
        car.setYear(cars.getYear());
        car.setStatus(cars.getStatus());
        car.setPricePerDay(cars.getPricePerDay());
        
        carRepo.save(car);
        
        CarDto updatedCarDto = modelMapper.map(car, CarDto.class);
        logger.info("Car updated successfully: {}", updatedCarDto);
        return updatedCarDto;
    }


    @Override
    public void deleteCar(long id) {
        logger.info("Deleting car with ID: {}", id);
        Car car = carRepo.findById(id).orElseThrow(
                ()-> new CarNotFound(String.format("Car with the car id %d is not found",id))
                );
        if(car.getStatus().equals(CarStatus.AVAILABLE)) {
            carRepo.deleteById(id);
            logger.info("Car deleted successfully: {}", id);
        }
        else {
            logger.warn("Attempted to delete booked car with ID: {}", id);
            throw new CarAlreadyBooked("The Car you are deleting is booked by someone else");
        }
    }
    
}
