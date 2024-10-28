package com.intuit.car.comparison.repository;

import com.intuit.car.comparison.model.Feature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeatureRepository extends MongoRepository<Feature, String> {
}

