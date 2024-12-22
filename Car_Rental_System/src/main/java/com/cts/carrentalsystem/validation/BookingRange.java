package com.cts.carrentalsystem.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BookingRangeValidator.class)
@Documented
public @interface BookingRange {
    String message() default "Invalid booking date range";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
