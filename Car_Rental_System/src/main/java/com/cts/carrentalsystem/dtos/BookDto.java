package com.cts.carrentalsystem.dtos;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.cts.carrentalsystem.enums.BookingStatus;
import com.cts.carrentalsystem.validation.BookingRange;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@BookingRange
public class BookDto {
	
	@NotNull(message = "Start date is mandatory") @DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
	@NotNull(message = "End date is mandatory") @DateTimeFormat(pattern = "yyyy-MM-dd") 
	private LocalDate endDate;
	
	@NotNull(message = "Booking status is mandatory") 
	private BookingStatus status; 
}
