package com.intuit.car.comparison.controller;

import com.intuit.car.comparison.exception.CarApplicationException;
import com.intuit.car.comparison.model.Car;
import com.intuit.car.comparison.model.SimilarCarContext;
import com.intuit.car.comparison.service.impl.CarCacheServiceImpl;
import com.intuit.car.comparison.service.impl.SimilarCarServiceImpl;
import com.intuit.car.comparison.util.ErrorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class CarInfoController {

    @Autowired
    private CarCacheServiceImpl carCacheServiceImpl;

    @Autowired
    private SimilarCarServiceImpl similarCarServiceImpl;

    @GetMapping("/search/car")
    public ResponseEntity<?> searchCars(@RequestParam("keyword") String keyword) {
        try {
            List<Car> cars = carCacheServiceImpl.searchCarsByKeyword(keyword);
            return ResponseEntity.ok(cars);
        } catch (CarApplicationException e) {
            return ErrorUtil.getErrorResponse(e);
        }
    }

    @GetMapping("/fetch/similar/car")
    public ResponseEntity<?> fetchSimilarCars(@RequestParam("carId") String carId) {
        try {
            List<SimilarCarContext> similarCars = similarCarServiceImpl.findTop10Similar(carId);
            return ResponseEntity.ok(similarCars);
        } catch (CarApplicationException e) {
            log.error("Error fetching similar cars for carId {}: {}", carId, e.getMessage());
            // Create a custom error response
            return ErrorUtil.getErrorResponse(e);
        }
    }
}

