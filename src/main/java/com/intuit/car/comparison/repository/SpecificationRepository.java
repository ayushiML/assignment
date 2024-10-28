package com.intuit.car.comparison.repository;

import com.intuit.car.comparison.model.Specification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpecificationRepository extends MongoRepository<Specification, String> {
}

