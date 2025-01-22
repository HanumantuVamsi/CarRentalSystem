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
import com.cts.carrentalsystem.exception.BookingNotFound;
import com.cts.carrentalsystem.exception.CarAlreadyBooked;
import com.cts.carrentalsystem.exception.CarNotFound;
import com.cts.carrentalsystem.exception.UserNotFound;
import com.cts.carrentalsystem.model.Booking;
import com.cts.carrentalsystem.model.Car;
import com.cts.carrentalsystem.model.Users;
import com.cts.carrentalsystem.repository.BookingRepository;
import com.cts.carrentalsystem.repository.CarRepository;
import com.cts.carrentalsystem.repository.UserRepository;
import com.cts.carrentalsystem.security.JWTService;
import com.cts.carrentalsystem.service.BookingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CarRepository carRepo;

    @Autowired
    private BookingRepository bookingRepo;
    
    @Autowired
    private JWTService service;

    // Book a car for a customer
    @Override
    public void booking(String token, long carId, BookDto book) {
    	long userId = getUserIdFromToken(token);
    
        logger.info("Booking request received for userId: {}, carId: {}", userId, carId);
        
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFound(String.format("User Id %d is not found", userId)));
        Car car = carRepo.findById(carId)
                .orElseThrow(() -> new CarNotFound(String.format("Car Id %d is not found", carId)));

        if (car.getStatus().equals(CarStatus.BOOKED)) {
            logger.warn("Car Id {} is already booked", carId);
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
        logger.info("Booking created successfully for userId: {}, carId: {}, bookingId: {}", userId, carId, booking.getBookId());
    }
    
    
    
    // Get booking details for the authenticated user
    @Override
    public List<BookingDetailsDto> getBookingByUserId(String token) {
    	long userId = getUserIdFromToken(token);
        logger.info("Retrieving bookings for userId: {}", userId);
        
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFound(String.format("User Id %d is not found", userId)));
        
        List<Booking> bookings = bookingRepo.findByUserId(userId);
        logger.info("Found {} bookings for userId: {}", bookings.size(), userId);
        
        return bookings.stream() .map(this::mapToDTO) .collect(Collectors.toList());
    }

    private BookingDetailsDto mapToDTO(Booking booking) {
        return new BookingDetailsDto(
            booking.getBookId(),
            booking.getCar().getBrand(),
            booking.getCar().getModel(),
            booking.getCar().getYear(),
            booking.getBookingDate(),
            booking.getStartDate(),
            booking.getEndDate(),
            booking.getPrice(),
            booking.getStatus(),
            booking.getCar().getId()
        ); 
    }

    // Cancel a booking
    @Override
    public void cancelBooking(String token, long bookId) {
   
        long userId = getUserIdFromToken(token);
        Booking bookingCheck = bookingRepo.findById(bookId).orElseThrow(
        		()-> new BookingNotFound("Booking is not found with this id"))
        		;
        logger.info("Cancellation request received for userId: {}, carId: {}", userId, bookingCheck.getCar().getId());
        if(userId!=bookingCheck.getUser().getId()) {
        	throw new BookingNotFound("Booking is not found with this id");
        }
        if(bookingCheck.getStatus().equals(BookingStatus.CANCELLED)) {
        	throw new BookingNotFound("Booking is already cancelled");
        }
        long carId = bookingCheck.getCar().getId(); 
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFound(String.format("User Id %d is not found", userId)));
        Car car = carRepo.findById(carId)
                .orElseThrow(() -> new CarNotFound(String.format("Car Id %d is not found", carId)));
        
        List<Booking> book = bookingRepo.findByUserIdAndCarId(userId, carId);
        Booking booking = null;
        for (Booking b : book) {
            if (b.getStatus().equals(BookingStatus.BOOKED)) {
                booking = b;
            }
        }
        
        booking.setStatus(BookingStatus.CANCELLED);
        car.setStatus(CarStatus.AVAILABLE);
        
        bookingRepo.save(booking);
        carRepo.save(car);
        logger.info("Booking cancelled successfully for userId: {}, carId: {}, bookingId: {}", userId, carId, booking.getBookId());
    }
    
    

    // Get all booking details (Admin only)
    @Override
    public List<AllBookingDetailsDto> getAllBookingDetails() {
        logger.info("Retrieving all booking details");
        
        List<Booking> bookings = bookingRepo.findAll();
        logger.info("Found {} total bookings", bookings.size());
        
        return bookings.stream().map(this::BookingToAllBooking).collect(Collectors.toList()); 
    }

    private AllBookingDetailsDto BookingToAllBooking(Booking booking) {
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
            booking.getStatus(),
            booking.getPrice(),
            booking.getUser().getEmail()
        ); 
    }

    

    // Complete a booking (Admin only)
    @Override
    public AllBookingDetailsDto completeBooking(long bookId) {
    

        
        Booking bookingCheck = bookingRepo.findById(bookId).orElseThrow(
        		()-> new BookingNotFound("Booking is not found with this id"))
        		;
        logger.info("Cancellation request received for userId: {}, carId: {}", bookingCheck.getUser().getId(), bookingCheck.getCar().getId());
   
     
        long carId = bookingCheck.getCar().getId(); 
        long userId  = bookingCheck.getUser().getId();
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFound(String.format("User Id %d is not found", userId)));
        Car car = carRepo.findById(carId)
                .orElseThrow(() -> new CarNotFound(String.format("Car Id %d is not found", carId)));

        List<Booking> book = bookingRepo.findByUserIdAndCarId(userId, carId);
        Booking booking = null;
        for (Booking b : book) {
            if (b.getStatus().equals(BookingStatus.BOOKED)) {
                booking = b;
            }
        }

        LocalDate today = LocalDate.now();

        if (!booking.getEndDate().isAfter(today)) {
            booking.setEndDate(today);
            booking.setStatus(BookingStatus.COMPLETED);
            car.setStatus(CarStatus.AVAILABLE);

            bookingRepo.save(booking);
            carRepo.save(car);

            logger.info("Booking completed successfully for userId: {}, carId: {}, bookingId: {}", userId, carId, booking.getBookId());
            return BookingToAllBooking(booking);
        } else {
            long daysLate = ChronoUnit.DAYS.between(booking.getEndDate(), today) + 1;

            int totalBill = (int) (booking.getPrice() + 1000 + (daysLate * car.getPricePerDay()));
            booking.setEndDate(today);
            booking.setStatus(BookingStatus.COMPLETED);
            car.setStatus(CarStatus.AVAILABLE);
            booking.setPrice(totalBill);

            bookingRepo.save(booking);
            carRepo.save(car);

            logger.info("Booking completed with additional charges for userId: {}, carId: {}, bookingId: {}", userId, carId, booking.getBookId());
            return BookingToAllBooking(booking);
        }
        
    }
    
    //extracting the user id by using email in jwt token
    public long getUserIdFromToken(String token)   { 
    	String email = service.extractUserName(token.substring(7)); // Remove 'Bearer ' prefix 
    	Users user = userRepo.findByEmail(email).orElseThrow(
    			()->new UserNotFound("Used with this emil not found")
    			);
        return user.getId();
    }
    
    
}
