package com.intuit.car.comparison.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "performance")
public class Performance {
    @Id
    private String id;
    private double zeroToSixty; // in seconds
    private double topSpeed;    // in km/h or mph
    private double fuelEfficiency; // e.g., miles per gallon

}

