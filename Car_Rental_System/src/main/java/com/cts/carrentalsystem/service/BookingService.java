package com.cts.carrentalsystem.service;

import java.util.List;

import com.cts.carrentalsystem.dtos.AllBookingDetailsDto;
import com.cts.carrentalsystem.dtos.BookDto;
import com.cts.carrentalsystem.dtos.BookingDetailsDto;


public interface BookingService {

	void booking(long userId, long carId, BookDto book);

	List<BookingDetailsDto> getBookingByUserId(long userId);

	void cancelBooking(long userId, long carId);

	List<AllBookingDetailsDto> getAllBookingDetails();

}
