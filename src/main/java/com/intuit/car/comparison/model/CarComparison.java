package com.intuit.car.comparison.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Data
@Document(collection = "car_comparisons")
public class CarComparison {
    @Id
    private String id;

    @DBRef
    private Car car1;

    @DBRef
    private Car car2;

    private List<String> comparedFeatures;
    private List<String> differences;

}

