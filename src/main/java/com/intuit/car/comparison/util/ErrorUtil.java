package com.intuit.car.comparison.util;

import com.intuit.car.comparison.model.ErrorResponse;
import org.springframework.http.ResponseEntity;

public class ErrorUtil {
    public static ResponseEntity<?> getErrorResponse(Exception e){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Invalid input or an error occurred while processing: " + e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
