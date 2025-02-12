package com.intuit.car.comparison.repository;

import com.intuit.car.comparison.model.Car;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends MongoRepository<Car, String> {
    List<Car> findByMakeContaining(String make); // Use an existing property like 'make' instead of 'name'
}
