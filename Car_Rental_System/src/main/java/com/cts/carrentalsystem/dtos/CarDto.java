package com.cts.carrentalsystem.dtos;

import com.cts.carrentalsystem.enums.CarStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {
   
	@NotBlank(message = "Brand should not be empty")
	private String brand;
	
	@NotBlank(message = "Model should not be empty")
	private String model; 
	
	@NotNull(message = "Year should not be null") 
	@Min(value = 2000, message = "Year should be valid") 
	private Integer year;
	
    @NotNull(message = "Price per day should not be null") 
	@Min(value = 0, message = "Price per day should be a positive number")
	private Integer pricePerDay;
	
	@NotNull(message = "Status should not be null")
	private CarStatus status;
}
