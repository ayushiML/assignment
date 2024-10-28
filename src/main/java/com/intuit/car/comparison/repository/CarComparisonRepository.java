package com.intuit.car.comparison.repository;

import com.intuit.car.comparison.model.CarComparison;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CarComparisonRepository extends MongoRepository<CarComparison, String> {
}
