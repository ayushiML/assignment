package com.intuit.car.comparison.util;

import com.intuit.car.comparison.CarSampleDataUtil;
import com.intuit.car.comparison.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CarComparisonHelperTest {

    private Car car1;
    private Car car2;
    private Car car3;

    @BeforeEach
    void setUp() {
        car1 = CarSampleDataUtil.getCar("1");
        car2 = CarSampleDataUtil.getCar("2");

        // A car with different attributes
        car3 = new Car();
        car3.setMake("Honda");
        car3.setModel("Accord");
        car3.setYear(2019);
        car3.setTrim("LX");
        car3.setBodyType("Sedan");
        car3.setPrice(28000);
    }

    @Test
    void testCalculateSimilarityScore_SimilarCars() {
        int score = CarComparisonHelper.calculateSimilarityScore(car1, car2);

        // Verify score based on similarities

        assertEquals(23, score);
    }

    @Test
    void testCalculateSimilarityScore_NoCommonFeaturesOrSpecifications() {

        int score = CarComparisonHelper.calculateSimilarityScore(car1, car2);


        assertEquals(23, score);
    }

    @Test
    void testCalculateSimilarityScore_OnlyOneCarHasFeaturesAndSpecifications() {
        // Set features/specifications only for car1, none for car2
        car2.setFeatures(new ArrayList<>());
        car2.setSpecifications(new ArrayList<>());

        int score = CarComparisonHelper.calculateSimilarityScore(car1, car2);

        assertEquals(20, score);
    }

    @Test
    void testCalculateSimilarityScore_SameCar() {
        int score = CarComparisonHelper.calculateSimilarityScore(car1, car1);

        // Maximum possible score, as all attributes match
        int expectedScore = 5  // Make
                + 5  // Model
                + 5  // Year
                + 5  // Trim
                + 5  // BodyType
                + 5  // Price within 2000 range
                + car1.getFeatures().size() // All features match
                + car1.getSpecifications().size() // All specifications match
                + 3  // Same zeroToSixty in Performance
                + 3  // Same topSpeed in Performance
                + 3; // Same fuelEfficiency in Performance

        assertEquals(expectedScore, score);
    }
}
