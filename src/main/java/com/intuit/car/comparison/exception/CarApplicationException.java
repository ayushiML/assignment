package com.intuit.car.comparison.exception;

public class CarApplicationException extends RuntimeException {

    public CarApplicationException(String message) {
        super(message);
    }

    public CarApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
