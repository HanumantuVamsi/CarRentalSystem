package com.cts.carrentalsystem.dtos;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.cts.carrentalsystem.enums.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate startDate;
	@DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate endDate;
    private BookingStatus status;
}
