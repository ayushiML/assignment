package com.intuit.car.comparison.exception;

public class CarValidationException extends CarApplicationException {
    public CarValidationException(String message) {
        super(message);
    }

    public CarValidationException(String message, Throwable cause) {
        super(message);
    }
}
