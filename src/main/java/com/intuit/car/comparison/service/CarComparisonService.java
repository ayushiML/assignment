package com.intuit.car.comparison.service;

import com.intuit.car.comparison.exception.CarApplicationException;
import com.intuit.car.comparison.model.CarComparisonResult;

import java.util.List;

public interface CarComparisonService {
    List<CarComparisonResult> compareCars(String carId1, String carId2) throws CarApplicationException;
}
