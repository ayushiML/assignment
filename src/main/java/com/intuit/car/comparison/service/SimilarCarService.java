package com.intuit.car.comparison.service;

import com.intuit.car.comparison.exception.CarApplicationException;
import com.intuit.car.comparison.model.SimilarCarContext;

import java.util.List;

public interface SimilarCarService {
    List<SimilarCarContext> findTop10Similar(String carId) throws CarApplicationException;
}
