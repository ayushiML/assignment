package com.intuit.car.comparison.scheduler;

import com.intuit.car.comparison.service.impl.CarCacheServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheScheduler {

    @Autowired
    private CarCacheServiceImpl carCacheServiceImpl;

    // Schedule to refresh cache every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void refreshCache() {
        carCacheServiceImpl.refreshCache();
    }
}