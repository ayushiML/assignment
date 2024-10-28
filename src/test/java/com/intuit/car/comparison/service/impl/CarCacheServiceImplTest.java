package com.intuit.car.comparison.service.impl;

import com.intuit.car.comparison.exception.CarApplicationException;
import com.intuit.car.comparison.exception.CarValidationException;
import com.intuit.car.comparison.model.Car;
import com.intuit.car.comparison.model.Feature;
import com.intuit.car.comparison.model.Performance;
import com.intuit.car.comparison.model.Specification;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarCacheServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarCacheServiceImpl carCacheService;

    private Car car1;
    private Car car2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample features
        Feature heatedSeats = new Feature();
        heatedSeats.setId("f1");
        heatedSeats.setName("Heated Seats");

        Feature rearViewCamera = new Feature();
        rearViewCamera.setId("f2");
        rearViewCamera.setName("Rear View Camera");

        // Sample specifications
        Specification engineSpec = new Specification();
        engineSpec.setId("s1");
        engineSpec.setType("Engine");
        engineSpec.setValue("2.0L Turbocharged");
        engineSpec.setUnit("L");

        Specification transmissionSpec = new Specification();
        transmissionSpec.setId("s2");
        transmissionSpec.setType("Transmission");
        transmissionSpec.setValue("8-speed automatic");
        transmissionSpec.setUnit("speed");

        // Sample performance
        Performance performance1 = new Performance();
        performance1.setId("p1");
        performance1.setZeroToSixty(6.2);
        performance1.setTopSpeed(145);
        performance1.setFuelEfficiency(24);

        // Sample cars
        car1 = new Car();
        car1.setId("1");
        car1.setMake("Toyota");
        car1.setModel("Camry");
        car1.setYear(2023);
        car1.setTrim("XSE");
        car1.setBodyType("Sedan");
        car1.setPrice(29000);
        car1.setFeatures(Arrays.asList(heatedSeats, rearViewCamera));
        car1.setSpecifications(Arrays.asList(engineSpec, transmissionSpec));
        car1.setPerformance(performance1);

        car2 = new Car();
        car2.setId("2");
        car2.setMake("Honda");
        car2.setModel("Accord");
        car2.setYear(2022);
        car2.setTrim("Sport");
        car2.setBodyType("Sedan");
        car2.setPrice(30000);
        car2.setFeatures(Collections.singletonList(rearViewCamera));
        car2.setSpecifications(Collections.singletonList(engineSpec));
        car2.setPerformance(performance1);

        // Initialize cache before each test
        when(carRepository.findAll()).thenReturn(Arrays.asList(car1, car2));
        carCacheService.refreshCache();
    }

    @Test
    void testSearchCarsByKeyword_Make() {
        // Test searching by make "Toyota"
        List<Car> result = carCacheService.searchCarsByKeyword("Toyota");
        assertEquals(1, result.size());
        assertEquals("Toyota", result.get(0).getMake());
    }

    @Test
    void testSearchCarsByKeyword_Model() {
        // Test searching by model "Accord"
        List<Car> result = carCacheService.searchCarsByKeyword("Accord");
        assertEquals(1, result.size());
        assertEquals("Accord", result.get(0).getModel());
    }

    @Test
    void testSearchCarsByKeyword_Year() {
        // Test searching by year "2023"
        List<Car> result = carCacheService.searchCarsByKeyword("2023");
        assertEquals(1, result.size());
        assertEquals(2023, result.get(0).getYear());
    }

    @Test
    void testSearchCarsByKeyword_BodyType() {
        // Test searching by body type "Sedan"
        List<Car> result = carCacheService.searchCarsByKeyword("Sedan");
        assertEquals(2, result.size());
    }

    @Test
    void testSearchCarsByKeyword_EmptyKeyword() {
        // Test with empty keyword, expecting CarValidationException
        Exception exception = assertThrows(CarValidationException.class, () -> carCacheService.searchCarsByKeyword(""));
        assertEquals("Search keyword cannot be null or empty", exception.getMessage());
    }

    @Test
    void testSearchCarsByKeyword_InvalidYear() {
        // Test with a non-numeric keyword, expecting no results but no error
        List<Car> result = carCacheService.searchCarsByKeyword("NonNumeric");
        assertTrue(result.isEmpty());
    }

    @Test
    void testRefreshCache_Success() {
        // Clear repository, refresh cache, and verify cache is empty
        when(carRepository.findAll()).thenReturn(Collections.emptyList());
        carCacheService.refreshCache();

        assertTrue(carCacheService.searchCarsByKeyword("Toyota").isEmpty());
        assertTrue(carCacheService.searchCarsByKeyword("Camry").isEmpty());
    }

    @Test
    void testRefreshCache_ExceptionHandling() {
        // Simulate exception in repository and ensure CarApplicationException is thrown
        when(carRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        CarApplicationException exception = assertThrows(CarApplicationException.class, carCacheService::refreshCache);
        assertEquals("Error refreshing cache", exception.getMessage());
    }
}
