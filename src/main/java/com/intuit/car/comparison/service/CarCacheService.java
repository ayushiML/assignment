package com.intuit.car.comparison.service;

import com.intuit.car.comparison.model.Car;

import java.util.List;

public interface CarCacheService {
    List<Car> searchCarsByKeyword(String key);
}
