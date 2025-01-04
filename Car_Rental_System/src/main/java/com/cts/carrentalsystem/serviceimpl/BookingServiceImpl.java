package com.cts.carrentalsystem.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cts.carrentalsystem.dtos.AllBookingDetailsDto;
import com.cts.carrentalsystem.dtos.BookDto;
import com.cts.carrentalsystem.dtos.BookingDetailsDto;
import com.cts.carrentalsystem.enums.BookingStatus;
import com.cts.carrentalsystem.enums.CarStatus;
import com.cts.carrentalsystem.exception.CarAlreadyBooked;
import com.cts.carrentalsystem.exception.CarNotFound;
import com.cts.carrentalsystem.exception.UserNotFound;
import com.cts.carrentalsystem.model.Booking;
import com.cts.carrentalsystem.model.Car;
import com.cts.carrentalsystem.model.Users;
import com.cts.carrentalsystem.repository.BookingRepository;
import com.cts.carrentalsystem.repository.CarRepository;
import com.cts.carrentalsystem.repository.UserRepository;
import com.cts.carrentalsystem.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CarRepository carRepo;

	@Autowired
	private BookingRepository bookingRepo;

	@Override
	public void booking(long userId, long carId, BookDto book) {
		// TODO Auto-generated method stub
		Users user = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFound(String.format("User Id %d is not found", userId)));
		Car car = carRepo.findById(carId)
				.orElseThrow(() -> new CarNotFound(String.format("Car Id %d is not found", carId)));

		if (car.getStatus().equals(CarStatus.BOOKED)) {
			throw new CarAlreadyBooked("Car already booked");
		}

		Booking booking = new Booking();
		long daysBetween = ChronoUnit.DAYS.between(book.getStartDate(), book.getEndDate()) + 1;
		int price = (int) (daysBetween * car.getPricePerDay());
		booking.setUser(user);
		booking.setCar(car);
		booking.setStartDate(book.getStartDate());
		booking.setEndDate(book.getEndDate());
		booking.setPrice(price);
		booking.setStatus(BookingStatus.BOOKED);

		car.setStatus(CarStatus.BOOKED);
		
		bookingRepo.save(booking);

	}

	@Override
	public List<BookingDetailsDto> getBookingByUserId(long userId) {
		// TODO Auto-generated method stub
		Users user = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFound(String.format("User Id %d is not found", userId)));
		List<Booking> bookings = bookingRepo.findByUserId(userId);
		return bookings.stream() .map(this::mapToDTO) .collect(Collectors.toList());

	}

	private BookingDetailsDto mapToDTO(Booking booking) 
	{ 
		return new BookingDetailsDto( 
			booking.getBookId(),
			booking.getCar().getBrand(),
			booking.getCar().getModel(),
			booking.getCar().getYear(), 
			booking.getBookingDate(),
			booking.getStartDate(),
			booking.getEndDate(), 
			 ); 
	}

	@Override
	public void cancelBooking(long userId, long carId) {
		// TODO Auto-generated method stub
		Users user = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFound(String.format("User Id %d is not found", userId)));
		Car car = carRepo.findById(carId)
				.orElseThrow(() -> new CarNotFound(String.format("Car Id %d is not found", carId)));
		
		List<Booking> book =bookingRepo.findByUserIdAndCarId(userId,carId);
		Booking booking =null;
		for(Booking b:  book) {
			if(b.getStatus().equals(BookingStatus.BOOKED)) {
				booking = b;
			}
		}
		
		booking.setStatus(BookingStatus.CANCELLED);
		car.setStatus(CarStatus.AVAILABLE);
		
		bookingRepo.save(booking);
		carRepo.save(car);
		
		
	}

	@Override
	public List<AllBookingDetailsDto> getAllBookingDetails() {
		// TODO Auto-generated method stub
		List<Booking> bookings = bookingRepo.findAll();
		return bookings.stream() .map(this::BookingToAllBooking) .collect(Collectors.toList()); 
	}
	
	private AllBookingDetailsDto BookingToAllBooking(Booking booking) 
	{ 
		return new AllBookingDetailsDto( 
			booking.getBookId(),
			booking.getUser().getId(),
			booking.getCar().getId(),
			booking.getCar().getBrand(),
			booking.getCar().getModel(),
			booking.getCar().getYear(), 
			booking.getBookingDate(),
			booking.getStartDate(),
			booking.getEndDate(), 
			booking.getStatus() ); 
	}
	
	
	

}
