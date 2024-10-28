package com.intuit.car.comparison.service.impl;

import com.intuit.car.comparison.exception.CarApplicationException;
import com.intuit.car.comparison.model.Car;
import com.intuit.car.comparison.model.SimilarCarContext;
import com.intuit.car.comparison.repository.CarRepository;
import com.intuit.car.comparison.util.CarComparisonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimilarCarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private SimilarCarServiceImpl similarCarService;

    private Car car1;
    private Car car2;
    private Car car3;

    @BeforeEach
    void setUp() {
        car1 = new Car();
        car1.setId("1");
        car1.setMake("Toyota");
        car1.setModel("Camry");
        car1.setYear(2020);
        car1.setPrice(30000);

        car2 = new Car();
        car2.setId("2");
        car2.setMake("Honda");
        car2.setModel("Accord");
        car2.setYear(2020);
        car2.setPrice(28000);

        car3 = new Car();
        car3.setId("3");
        car3.setMake("Nissan");
        car3.setModel("Altima");
        car3.setYear(2020);
        car3.setPrice(29000);
    }

    @Test
    void testFindTop10Similar_CarFoundAndReturnsSimilarCars() throws CarApplicationException {
        when(carRepository.findById("1")).thenReturn(Optional.of(car1));
        when(carRepository.findAll()).thenReturn(Arrays.asList(car1, car2, car3));

        // Mock similarity score calculations
        when(CarComparisonHelper.calculateSimilarityScore(car1, car2)).thenReturn(85);
        when(CarComparisonHelper.calculateSimilarityScore(car1, car3)).thenReturn(75);

        List<SimilarCarContext> result = similarCarService.findTop10Similar("1");

        assertEquals(2, result.size());
        assertEquals("2", result.get(0).getCar().getId());
        assertEquals("3", result.get(1).getCar().getId());

        // Verify sorting by similarity score
        assertTrue(result.get(0).getSimilarityScore() > result.get(1).getSimilarityScore());
    }

    @Test
    void testFindTop10Similar_CarNotFound_ThrowsException() {
        when(carRepository.findById("99")).thenReturn(Optional.empty());

        CarApplicationException exception = assertThrows(
                CarApplicationException.class,
                () -> similarCarService.findTop10Similar("99")
        );

        assertEquals("Car not found", exception.getMessage());
    }

    @Test
    void testFindTop10Similar_NoSimilarCars_ReturnsEmptyList() throws CarApplicationException {
        when(carRepository.findById("1")).thenReturn(Optional.of(car1));
        when(carRepository.findAll()).thenReturn(Collections.singletonList(car1));

        List<SimilarCarContext> result = similarCarService.findTop10Similar("1");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindTop10Similar_LimitsToTop10Results() throws CarApplicationException {
        when(carRepository.findById("1")).thenReturn(Optional.of(car1));

        List<Car> allCars = Arrays.asList(car1, car2, car3); // Add more dummy cars if needed
        when(carRepository.findAll()).thenReturn(allCars);

        allCars.forEach(car -> when(CarComparisonHelper.calculateSimilarityScore(car1, car)).thenReturn(50));

        List<SimilarCarContext> result = similarCarService.findTop10Similar("1");

        assertEquals(Math.min(10, allCars.size() - 1), result.size()); // limit to 10 or fewer if fewer cars exist
    }

    @Test
    void testFindTop10Similar_CacheBehavior() throws CarApplicationException {
        when(carRepository.findById("1")).thenReturn(Optional.of(car1));
        when(carRepository.findAll()).thenReturn(Arrays.asList(car1, car2, car3));
        when(CarComparisonHelper.calculateSimilarityScore(car1, car2)).thenReturn(85);
        when(CarComparisonHelper.calculateSimilarityScore(car1, car3)).thenReturn(75);

        // Call the service twice and check if cache is used
        similarCarService.findTop10Similar("1");
        similarCarService.findTop10Similar("1");

        verify(carRepository, times(1)).findById("1");
        verify(carRepository, times(1)).findAll();
    }
}
