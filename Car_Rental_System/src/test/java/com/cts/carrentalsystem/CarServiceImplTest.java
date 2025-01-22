package com.cts.carrentalsystem;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.cts.carrentalsystem.dtos.CarDto;
import com.cts.carrentalsystem.enums.CarStatus;
import com.cts.carrentalsystem.exception.CarAlreadyBooked;
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
        car.setId(1L);
        car.setModel("Model X");
        car.setBrand("Tesla");
        car.setYear(2022);
        car.setPricePerDay(100); // Changed to int
        car.setStatus(CarStatus.AVAILABLE);

        carDto = new CarDto();
        carDto.setModel("Model X");
        carDto.setBrand("Tesla");
        carDto.setYear(2022);
        carDto.setPricePerDay(100); // Changed to int
        carDto.setStatus(CarStatus.AVAILABLE);
    }

    private CarDto mapToDto(Car car) {
        CarDto dto = new CarDto();
        dto.setModel(car.getModel());
        dto.setBrand(car.getBrand());
        dto.setYear(car.getYear());
        dto.setPricePerDay(car.getPricePerDay());
        dto.setStatus(car.getStatus());
        return dto;
    }

    private Car mapToEntity(CarDto dto) {
        Car car = new Car();
        car.setModel(dto.getModel());
        car.setBrand(dto.getBrand());
        car.setYear(dto.getYear());
        car.setPricePerDay(dto.getPricePerDay());
        car.setStatus(dto.getStatus());
        return car;
    }

    @Test
    public void testCreateCar() {
        // True scenario
        Car carEntity = mapToEntity(carDto);
        when(carRepo.save(any(Car.class))).thenReturn(car);
//        when(modelMapper.map(any(Car.class), eq(CarDto.class))).thenReturn(carDto);

        CarDto savedCar = carService.createCar(carDto);

        assertNotNull(savedCar);
        assertEquals("Model X", savedCar.getModel());
        verify(carRepo, times(1)).save(any(Car.class));

        // False scenario
        when(carRepo.save(any(Car.class))).thenThrow(new RuntimeException("Error creating car"));
        assertThrows(RuntimeException.class, () -> carService.createCar(carDto));
    }

    @Test
    public void testGetAllCars() {
        // True scenario
        List<Car> cars = Arrays.asList(car);
        when(carRepo.findAll()).thenReturn(cars);
        when(modelMapper.map(any(Car.class), eq(CarDto.class))).thenReturn(carDto);

        List<CarDto> carDtos = carService.getAllCars();

        assertNotNull(carDtos);
        assertEquals(1, carDtos.size());
        verify(carRepo, times(1)).findAll();

        // False scenario
        when(carRepo.findAll()).thenThrow(new RuntimeException("Error retrieving cars"));
        assertThrows(RuntimeException.class, () -> carService.getAllCars());
    }

    @Test
    public void testGetCar() {
        // True scenario
        when(carRepo.findById(1L)).thenReturn(Optional.of(car));
        when(modelMapper.map(any(Car.class), eq(CarDto.class))).thenReturn(carDto);

        CarDto foundCar = carService.getCar(1L);

        assertNotNull(foundCar);
        assertEquals("Model X", foundCar.getModel());
        verify(carRepo, times(1)).findById(1L);

        // False scenario
        when(carRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(CarNotFound.class, () -> carService.getCar(2L));
    }

    @Test
    public void testUpdateCar() {
        // True scenario
        when(carRepo.findById(1L)).thenReturn(Optional.of(car));
        when(carRepo.save(any(Car.class))).thenReturn(car);
        when(modelMapper.map(any(Car.class), eq(CarDto.class))).thenReturn(carDto);

        CarDto updatedCar = carService.updateCar(1L, carDto);

        assertNotNull(updatedCar);
        assertEquals("Model X", updatedCar.getModel());
        verify(carRepo, times(1)).findById(1L);
        verify(carRepo, times(1)).save(any(Car.class));

        // False scenario
        when(carRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(CarNotFound.class, () -> carService.updateCar(2L, carDto));
    }

    @Test
    public void testDeleteCar() {
        // True scenario
        when(carRepo.findById(1L)).thenReturn(Optional.of(car));

        carService.deleteCar(1L);

        verify(carRepo, times(1)).findById(1L);
        verify(carRepo, times(1)).deleteById(1L);

        // False scenario - Car already booked
        car.setStatus(CarStatus.BOOKED);
        when(carRepo.findById(2L)).thenReturn(Optional.of(car));
        assertThrows(CarAlreadyBooked.class, () -> carService.deleteCar(2L));

        // False scenario - Car not found
        when(carRepo.findById(3L)).thenReturn(Optional.empty());
        assertThrows(CarNotFound.class, () -> carService.deleteCar(3L));
    }
}
