package com.intuit.car.comparison.controller;

import com.intuit.car.comparison.exception.CarApplicationException;
import com.intuit.car.comparison.model.CarComparisonResult;
import com.intuit.car.comparison.model.Difference;
import com.intuit.car.comparison.service.impl.CarComparisonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CarComparisonController {

    private static final Logger logger = LoggerFactory.getLogger(CarComparisonController.class);

    @Autowired
    private CarComparisonServiceImpl carComparisonServiceImpl;

    @GetMapping("/compare")
    public ResponseEntity<List<CarComparisonResult>> compareCars(@RequestParam("carId1") String carId1,
                                                                 @RequestParam("carId2") String carId2) {
        try {
            List<CarComparisonResult> comparisonResults = carComparisonServiceImpl.compareCars(carId1, carId2);
            return ResponseEntity.ok(comparisonResults);
        } catch (CarApplicationException e) {

            List<Difference> errorDifferences = List.of(new Difference("Error", e.getMessage(), ""));
            CarComparisonResult errorResult = new CarComparisonResult("Comparison Error", null, null, errorDifferences);

            return ResponseEntity.badRequest().body(List.of(errorResult));
        }
    }
}

