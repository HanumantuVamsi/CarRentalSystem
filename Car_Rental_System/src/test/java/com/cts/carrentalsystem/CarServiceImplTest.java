package com.cts.carrentalsystem;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.cts.carrentalsystem.dtos.CarDto;
import com.cts.carrentalsystem.enums.CarStatus;
import com.cts.carrentalsystem.exception.CarNotFound;
import com.cts.carrentalsystem.model.Car;
import com.cts.carrentalsystem.repository.CarRepository;
import com.cts.carrentalsystem.serviceimpl.CarServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CarServiceImplTest {

	@Mock
	private CarRepository carRepo;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private CarServiceImpl carService;

	private Car car;
	private CarDto carDto;

	@BeforeEach
	public void setUp() {
		car = new Car();
		car.setModel("Model X");
		car.setBrand("Brand Y");
		car.setYear(2020);
		car.setPricePerDay(100);
		car.setStatus(CarStatus.AVAILABLE);

		carDto = new CarDto();
		carDto.setModel("Model X");
		carDto.setBrand("Brand Y");
		carDto.setYear(2020);
		carDto.setPricePerDay(100);
		carDto.setStatus(CarStatus.AVAILABLE);
	}

	@Test
	public void testCreateCar() {
		when(carRepo.save(any(Car.class))).thenReturn(car);
		CarDto createdCar = carService.createCar(carDto);
		assertEquals(carDto.getModel(), createdCar.getModel());
		assertEquals(carDto.getBrand(), createdCar.getBrand());
		verify(carRepo, times(1)).save(any(Car.class));
	}

	@Test
	public void testGetAllCars() {
		List<Car> cars = new ArrayList<>();
		cars.add(car);

		when(carRepo.findAll()).thenReturn(cars);
		when(modelMapper.map(any(Car.class), eq(CarDto.class))).thenReturn(carDto);

		List<CarDto> carDtos = carService.getAllCars();

		assertEquals(1, carDtos.size());
		verify(carRepo, times(1)).findAll();
	}

	@Test
	public void testGetCar() {
		when(carRepo.findById(anyLong())).thenReturn(Optional.of(car));
		when(modelMapper.map(any(Car.class), eq(CarDto.class))).thenReturn(carDto);

		CarDto foundCar = carService.getCar(1L);

		assertEquals(carDto.getModel(), foundCar.getModel());
		assertEquals(carDto.getBrand(), foundCar.getBrand());
		verify(carRepo, times(1)).findById(anyLong());
	}

	@Test
	public void testGetCarNotFound() {
		when(carRepo.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(CarNotFound.class, () -> {
			carService.getCar(1L);
		});

		verify(carRepo, times(1)).findById(anyLong());
	}

	@Test
	public void testUpdateCar() {
		when(carRepo.findById(anyLong())).thenReturn(Optional.of(car));
		when(carRepo.save(any(Car.class))).thenReturn(car);
		when(modelMapper.map(any(Car.class), eq(CarDto.class))).thenReturn(carDto);

		CarDto updatedCar = carService.updateCar(1L, carDto);

		assertEquals(carDto.getModel(), updatedCar.getModel());
		assertEquals(carDto.getBrand(), updatedCar.getBrand());
		verify(carRepo, times(1)).findById(anyLong());
		verify(carRepo, times(1)).save(any(Car.class));
	}

	@Test
	public void testDeleteCar() {
		when(carRepo.findById(anyLong())).thenReturn(Optional.of(car));
		doNothing().when(carRepo).deleteById(anyLong());

		carService.deleteCar(1L);

		verify(carRepo, times(1)).findById(anyLong());
		verify(carRepo, times(1)).deleteById(anyLong());
	}

	@Test
	public void testDeleteCarNotFound() {
		when(carRepo.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(CarNotFound.class, () -> {
			carService.deleteCar(1L);
		});

		verify(carRepo, times(1)).findById(anyLong());
	}
}
