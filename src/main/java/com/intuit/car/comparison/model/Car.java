package com.intuit.car.comparison.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;


@Document(collection = "cars")
@Data
public class Car {
    @Id
    private String id;
    private String make;
    private String model;
    private int year;
    private String trim;
    private String bodyType;
    private double price;

    @DBRef
    private List<Feature> features;

    @DBRef
    private List<Specification> specifications;

    @DBRef
    private Performance performance;

}

