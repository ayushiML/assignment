package com.intuit.car.comparison.service.impl;

import com.intuit.car.comparison.exception.CarApplicationException;
import com.intuit.car.comparison.exception.CarValidationException;
import com.intuit.car.comparison.model.Car;
import com.intuit.car.comparison.repository.CarRepository;
import com.intuit.car.comparison.service.CarCacheService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CarCacheServiceImpl implements CarCacheService {

    @Autowired
    private CarRepository carRepository;

    private Map<String, List<Car>> makeCache = new ConcurrentHashMap<>();
    private Map<String, List<Car>> modelCache = new ConcurrentHashMap<>();
    private Map<Integer, List<Car>> yearCache = new ConcurrentHashMap<>();
    private Map<String, List<Car>> bodyTypeCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        try {
            log.info("Initializing CarCacheService...");
            refreshCache();
            log.info("CarCacheService initialized successfully.");
        } catch (Exception e) {
            log.error("Failed to initialize CarCacheService", e);
            throw new CarApplicationException("Error initializing cache service");
        }
    }

    @Override
    public List<Car> searchCarsByKeyword(String key) {
        if (key == null || key.isEmpty()) {
            throw new CarValidationException("Search keyword cannot be null or empty");
        }

        String keyword = key.toLowerCase();
        List<Car> result = new ArrayList<>();

        try {
            if (makeCache.containsKey(keyword)) {
                result.addAll(makeCache.get(keyword));
                log.debug("Found cars in makeCache for keyword '{}'", keyword);
            }

            if (modelCache.containsKey(keyword)) {
                result.addAll(modelCache.get(keyword));
                log.debug("Found cars in modelCache for keyword '{}'", keyword);
            }

            try {
                int year = Integer.parseInt(keyword);
                if (yearCache.containsKey(year)) {
                    result.addAll(yearCache.get(year));
                    log.debug("Found cars in yearCache for year '{}'", year);
                }
            } catch (NumberFormatException e) {
                log.debug("Keyword '{}' is not a valid year", keyword);
            }

            if (bodyTypeCache.containsKey(keyword)) {
                result.addAll(bodyTypeCache.get(keyword));
                log.debug("Found cars in bodyTypeCache for keyword '{}'", keyword);
            }

            log.info("Search completed for keyword '{}'. Found {} cars.", keyword, result.size());
            return result.stream().distinct().collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error occurred while searching cars with keyword '{}'", keyword, e);
            throw new CarApplicationException("An error occurred during search");
        }
    }

    public void refreshCache() {
        log.info("Refreshing car cache...");
        try {
            List<Car> carList = carRepository.findAll();
            makeCache.clear();
            modelCache.clear();
            yearCache.clear();
            bodyTypeCache.clear();

            for (Car car : carList) {
                makeCache.computeIfAbsent(car.getMake().toLowerCase(), k -> new ArrayList<>()).add(car);
                modelCache.computeIfAbsent(car.getModel().toLowerCase(), k -> new ArrayList<>()).add(car);
                yearCache.computeIfAbsent(car.getYear(), k -> new ArrayList<>()).add(car);
                bodyTypeCache.computeIfAbsent(car.getBodyType().toLowerCase(), k -> new ArrayList<>()).add(car);
            }

            log.info("Cache refresh completed successfully. Total cars cached: {}", carList.size());
        } catch (Exception e) {
            log.error("Failed to refresh car cache", e);
            throw new CarApplicationException("Error refreshing cache");
        }
    }
}
