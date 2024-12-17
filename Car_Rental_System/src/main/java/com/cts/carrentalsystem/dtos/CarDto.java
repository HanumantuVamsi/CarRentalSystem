package com.cts.carrentalsystem.dtos;

import com.cts.carrentalsystem.enums.CarStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {
   
	private String brand;
	private String model;
	private int year;
	private int pricePerDay;
	private CarStatus status;
}
