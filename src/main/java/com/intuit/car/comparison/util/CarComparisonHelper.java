package com.intuit.car.comparison.util;

import com.intuit.car.comparison.model.Car;
import com.intuit.car.comparison.model.Feature;
import com.intuit.car.comparison.model.Specification;

import java.util.Set;
import java.util.stream.Collectors;

public class CarComparisonHelper {
    public static int calculateSimilarityScore(Car car1, Car car2) {
        int score = 0;

        // Add weighted score based on attribute matches
        if (car1.getMake().equals(car2.getMake())) score += 5;
        if (car1.getModel().equals(car2.getModel())) score += 5;
        if (car1.getYear() == car2.getYear()) score += 5;
        if (car1.getTrim().equals(car2.getTrim())) score += 5;
        if (car1.getBodyType().equals(car2.getBodyType())) score += 5;

        // Price similarity within a range
        if (Math.abs(car1.getPrice() - car2.getPrice()) < 2000) score += 5;

        // Add scores based on common features
        Set<String> car1Features = car1.getFeatures().stream().map(Feature::getId).collect(Collectors.toSet());
        Set<String> car2Features = car2.getFeatures().stream().map(Feature::getId).collect(Collectors.toSet());
        car1Features.retainAll(car2Features);
        score += car1Features.size(); // Add based on common features

        // Add scores based on common specifications
        Set<String> car1Specs = car1.getSpecifications().stream().map(Specification::getId).collect(Collectors.toSet());
        Set<String> car2Specs = car2.getSpecifications().stream().map(Specification::getId).collect(Collectors.toSet());
        car1Specs.retainAll(car2Specs);
        score += car1Specs.size(); // Add based on common specifications

        // Add performance comparison
        if (car1.getPerformance().getZeroToSixty() == car2.getPerformance().getZeroToSixty()) score += 3;
        if (car1.getPerformance().getTopSpeed() == car2.getPerformance().getTopSpeed()) score += 3;
        if (car1.getPerformance().getFuelEfficiency() == car2.getPerformance().getFuelEfficiency()) score += 3;

        return score;
    }
}
