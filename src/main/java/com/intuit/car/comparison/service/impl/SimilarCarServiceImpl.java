package com.intuit.car.comparison.service.impl;

import com.intuit.car.comparison.exception.CarApplicationException;
import com.intuit.car.comparison.model.Car;
import com.intuit.car.comparison.model.SimilarCarContext; // Import the new class
import com.intuit.car.comparison.repository.CarRepository;
import com.intuit.car.comparison.service.SimilarCarService;
import com.intuit.car.comparison.util.CarComparisonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class SimilarCarServiceImpl implements SimilarCarService {

    private static final Logger logger = LoggerFactory.getLogger(SimilarCarServiceImpl.class);

    @Autowired
    private CarRepository carRepository;

    @Cacheable("similarCarsCache")
    public List<SimilarCarContext> findTop10Similar(String carId) throws CarApplicationException {
        // Fetch the input car
        Car inputCar = carRepository.findById(carId).orElseThrow(() -> new CarApplicationException("Car not found"));

        // Fetch all cars asynchronously
        CompletableFuture<List<Car>> allCarsFuture = CompletableFuture.supplyAsync(carRepository::findAll);

        List<Car> allCars;
        try {
            allCars = allCarsFuture.get(); // Await result asynchronously
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error fetching all cars: {}", e.getMessage());
            throw new CarApplicationException("Error fetching similar cars");
        }

        List<SimilarCarContext> similarCarContexts = new ArrayList<>();

        for (Car car : allCars) {
            if (!car.getId().equals(inputCar.getId())) {
                int score = CarComparisonHelper.calculateSimilarityScore(inputCar, car);
                similarCarContexts.add(SimilarCarContext.builder()
                                .similarityScore(score)
                                .car(car)
                        .build());
            }
        }

        // Sort by similarity score and return the top 10
        return similarCarContexts.stream()
                .sorted(Comparator.comparingInt(SimilarCarContext::getSimilarityScore).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
}
