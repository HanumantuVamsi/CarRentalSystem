package com.cts.carrentalsystem.dtos;

import java.time.LocalDate;

import com.cts.carrentalsystem.enums.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetailsDto {
	
	private long bookId;
	private String brand;
	private String model;
	private int year;
	private LocalDate bookedDate;
	private LocalDate startDate;
	private LocalDate endDate;
	private BookingStatus status;
	

}
