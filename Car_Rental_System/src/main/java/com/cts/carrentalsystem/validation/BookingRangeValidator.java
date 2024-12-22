package com.cts.carrentalsystem.validation;



import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

import com.cts.carrentalsystem.dtos.BookDto;

public class BookingRangeValidator implements ConstraintValidator<BookingRange, BookDto> {

    @Override
    public void initialize(BookingRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookDto bookingDto, ConstraintValidatorContext context) {
        if (bookingDto.getStartDate() == null || bookingDto.getEndDate() == null) {
            return true; // @NotNull will handle this case
        }

        LocalDate startDate = bookingDto.getStartDate();
        LocalDate endDate = bookingDto.getEndDate();
        LocalDate bookingDate = LocalDate.now();

        // Start date should be greater than or equal to booking date
        if (!startDate.isEqual(bookingDate) && startDate.isBefore(bookingDate)) {
            context.buildConstraintViolationWithTemplate("Start date should be greater than or equal to booking date")
                   .addPropertyNode("startDate")
                   .addConstraintViolation();
            return false;
        }

        // Booking should be allowed within two weeks from start date
        if (startDate.isAfter(bookingDate.plusWeeks(2))) {
            context.buildConstraintViolationWithTemplate("Booking is allowed only within two weeks from start date")
                   .addPropertyNode("startDate")
                   .addConstraintViolation();
            return false;
        }

        // End date should be greater than or equal to start date
        if (endDate.isBefore(startDate)) {
            context.buildConstraintViolationWithTemplate("End date should be greater than or equal to start date")
                   .addPropertyNode("endDate")
                   .addConstraintViolation();
            return false;
        }

        return true;
    }
}

