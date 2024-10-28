package com.intuit.car.comparison.service.impl;
import com.intuit.car.comparison.CarSampleDataUtil;
import com.intuit.car.comparison.exception.CarApplicationException;
import com.intuit.car.comparison.model.*;
import com.intuit.car.comparison.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarComparisonServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarComparisonServiceImpl carComparisonService;

    private Car car1;
    private Car car2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        car1 = CarSampleDataUtil.getCar("1");
        car2 = CarSampleDataUtil.getCar("2");


        when(carRepository.findById("1")).thenReturn(Optional.of(car1));
        when(carRepository.findById("2")).thenReturn(Optional.of(car2));
    }

    @Test
    void testCompareCars_DifferentTrimAndPrice() throws CarApplicationException {
        List<CarComparisonResult> results = carComparisonService.compareCars("1", "2");

        // Expect two results for different trim and price
        assertEquals(4, results.size());

        CarComparisonResult trimComparison = results.stream()
                .filter(result -> result.getField().equals("Trim"))
                .findFirst()
                .orElse(null);

        assertNotNull(trimComparison);
        assertEquals("XSE", trimComparison.getCar1Value());
        assertEquals("LE", trimComparison.getCar2Value());

        CarComparisonResult priceComparison = results.stream()
                .filter(result -> result.getField().equals("Price"))
                .findFirst()
                .orElse(null);

        assertNotNull(priceComparison);
        assertEquals("29000.0", priceComparison.getCar1Value());
        assertEquals("27000.0", priceComparison.getCar2Value());
    }

    @Test
    void testCompareCars_PerformanceDifference() throws CarApplicationException {
        List<CarComparisonResult> results = carComparisonService.compareCars("1", "2");

        CarComparisonResult performanceComparison = results.stream()
                .filter(result -> result.getField().equals("Performance"))
                .findFirst()
                .orElse(null);

        assertNotNull(performanceComparison);
        assertEquals(3, performanceComparison.getDifferences().size());
    }

    @Test
    void testCompareCars_FeatureDifference() throws CarApplicationException {
        List<CarComparisonResult> results = carComparisonService.compareCars("1", "2");

        CarComparisonResult featureComparison = results.stream()
                .filter(result -> result.getField().equals("Features"))
                .findFirst()
                .orElse(null);

        assertNotNull(featureComparison);
        assertEquals(1, featureComparison.getDifferences().size());
        assertEquals("Feature missing in Car 2", featureComparison.getDifferences().get(0).getDescription());
        assertEquals("Sunroof", featureComparison.getDifferences().get(0).getCar1Detail());
    }

//    @Test
//    void testCompareCars_ExceptionWhenCarNotFound() {
//
//        lenient().when(carRepository.findById("1")).thenReturn(Optional.of(car1)); // Optional for additional stubbing
//        when(carRepository.findById("3")).thenReturn(Optional.empty()); // Primary missing car
//
//        CarApplicationException exception = assertThrows(
//                CarApplicationException.class,
//                () -> carComparisonService.compareCars("3", "1")
//        );
//
//        assertEquals("Car with ID 3 not found", exception.getMessage());
//    }


    @Test
    void testCompareCars_SpecificationDifference() throws CarApplicationException {
        Specification differentSpec = new Specification();
        differentSpec.setId("s3");
        differentSpec.setType("Engine");
        differentSpec.setValue("2.5L");

        car2.setSpecifications(Collections.singletonList(differentSpec));

        List<CarComparisonResult> results = carComparisonService.compareCars("1", "2");

        CarComparisonResult specComparison = results.stream()
                .filter(result -> result.getField().equals("Specifications"))
                .findFirst()
                .orElse(null);

        assertNotNull(specComparison);
        assertEquals(1, specComparison.getDifferences().size());
        assertEquals("2.0L", specComparison.getDifferences().get(0).getCar1Detail());
        assertEquals("2.5L", specComparison.getDifferences().get(0).getCar2Detail());
    }
}
