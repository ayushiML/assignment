package com.intuit.car.comparison.service.impl;


import com.intuit.car.comparison.exception.CarApplicationException;
import com.intuit.car.comparison.model.*;
import com.intuit.car.comparison.repository.CarRepository;
import com.intuit.car.comparison.service.CarComparisonService;
import com.intuit.car.comparison.util.ComparisonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarComparisonServiceImpl implements CarComparisonService {

    private static final Logger logger = LoggerFactory.getLogger(CarComparisonServiceImpl.class);

    @Autowired
    private CarRepository carRepository;

    @Override
    public List<CarComparisonResult> compareCars(String carId1, String carId2) throws CarApplicationException {
        Car car1 = carRepository.findById(carId1)
                .orElseThrow(() -> new CarApplicationException(String.format(ComparisonConstants.CAR_NOT_FOUND, carId1)));
        Car car2 = carRepository.findById(carId2)
                .orElseThrow(() -> new CarApplicationException(String.format(ComparisonConstants.CAR_NOT_FOUND, carId2)));

        List<CarComparisonResult> comparisonResults = new ArrayList<>();

        // Compare basic fields only if values are different
        addComparisonResult(comparisonResults, compareField(ComparisonConstants.MAKE, car1.getMake(), car2.getMake()));
        addComparisonResult(comparisonResults, compareField(ComparisonConstants.MODEL, car1.getModel(), car2.getModel()));
        addComparisonResult(comparisonResults, compareField(ComparisonConstants.YEAR, String.valueOf(car1.getYear()), String.valueOf(car2.getYear())));
        addComparisonResult(comparisonResults, compareField(ComparisonConstants.TRIM, car1.getTrim(), car2.getTrim()));
        addComparisonResult(comparisonResults, compareField(ComparisonConstants.BODY_TYPE, car1.getBodyType(), car2.getBodyType()));
        addComparisonResult(comparisonResults, compareField(ComparisonConstants.PRICE, String.valueOf(car1.getPrice()), String.valueOf(car2.getPrice())));

        // Compare Performance
        addComparisonResult(comparisonResults, comparePerformance(car1.getPerformance(), car2.getPerformance()));

        // Compare Specifications
        addComparisonResult(comparisonResults, compareSpecifications(car1.getSpecifications(), car2.getSpecifications()));

        // Compare Features
        addComparisonResult(comparisonResults, compareFeatures(car1.getFeatures(), car2.getFeatures()));

        return comparisonResults;
    }

    private void addComparisonResult(List<CarComparisonResult> results, CarComparisonResult comparisonResult) {
        if (!comparisonResult.getDifferences().isEmpty()) {
            results.add(comparisonResult);
        }
    }

    private CarComparisonResult compareField(String field, String value1, String value2) {
        List<Difference> differences = new ArrayList<>();
        if (!value1.equals(value2)) {
            differences.add(new Difference(ComparisonConstants.DIFFERENT_VALUES, value1, value2));
        }
        return new CarComparisonResult(field, value1, value2, differences);
    }

    private CarComparisonResult comparePerformance(Performance performance1, Performance performance2) {
        List<Difference> differences = new ArrayList<>();

        try {
            if (performance1.getZeroToSixty() != performance2.getZeroToSixty()) {
                differences.add(new Difference(ComparisonConstants.ZERO_TO_SIXTY, performance1.getZeroToSixty() + ComparisonConstants.ZERO_TO_SIXTY_UNIT, performance2.getZeroToSixty() + ComparisonConstants.ZERO_TO_SIXTY_UNIT));
            }
            if (performance1.getTopSpeed() != performance2.getTopSpeed()) {
                differences.add(new Difference(ComparisonConstants.TOP_SPEED, performance1.getTopSpeed() + ComparisonConstants.TOP_SPEED_UNIT, performance2.getTopSpeed() + ComparisonConstants.TOP_SPEED_UNIT));
            }
            if (performance1.getFuelEfficiency() != performance2.getFuelEfficiency()) {
                differences.add(new Difference(ComparisonConstants.FUEL_EFFICIENCY, performance1.getFuelEfficiency() + ComparisonConstants.FUEL_EFFICIENCY_UNIT, performance2.getFuelEfficiency() + ComparisonConstants.FUEL_EFFICIENCY_UNIT));
            }
        } catch (Exception e) {
            logger.error("Error comparing performance: {}", e.getMessage());
        }

        return new CarComparisonResult(ComparisonConstants.PERFORMANCE, performance1.toString(), performance2.toString(), differences);
    }

    private CarComparisonResult compareSpecifications(List<Specification> specs1, List<Specification> specs2) {
        List<Difference> differences = new ArrayList<>();

        try {
            for (Specification spec1 : specs1) {
                for (Specification spec2 : specs2) {
                    if (spec1.getType().equals(spec2.getType()) && !spec1.getValue().equals(spec2.getValue())) {
                        differences.add(new Difference(String.format(ComparisonConstants.DIFFERENT_SPECIFICATIONS, spec1.getType()), spec1.getValue(), spec2.getValue()));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error comparing specifications: {}", e.getMessage());
        }

        return new CarComparisonResult(ComparisonConstants.SPECIFICATIONS, specs1.toString(), specs2.toString(), differences);
    }

    private CarComparisonResult compareFeatures(List<Feature> features1, List<Feature> features2) {
        List<Difference> differences = new ArrayList<>();

        try {
            for (Feature feature1 : features1) {
                boolean found = features2.stream().anyMatch(feature2 -> feature2.getId().equals(feature1.getId()));
                if (!found) {
                    differences.add(new Difference(ComparisonConstants.FEATURE_MISSING_IN_CAR_2, feature1.getName(), ""));
                }
            }

            for (Feature feature2 : features2) {
                boolean found = features1.stream().anyMatch(feature1 -> feature1.getId().equals(feature2.getId()));
                if (!found) {
                    differences.add(new Difference(ComparisonConstants.FEATURE_MISSING_IN_CAR_1, "", feature2.getName()));
                }
            }
        } catch (Exception e) {
            logger.error("Error comparing features: {}", e.getMessage());
        }

        return new CarComparisonResult(com.intuit.car.comparison.util.ComparisonConstants.FEATURES, "", "", differences);
    }
}
