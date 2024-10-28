package com.intuit.car.comparison.repository;

import com.intuit.car.comparison.model.Performance;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PerformanceRepository extends MongoRepository<Performance, String> {
}
