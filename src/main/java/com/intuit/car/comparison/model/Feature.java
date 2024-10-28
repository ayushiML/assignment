package com.intuit.car.comparison.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "features")
public class Feature {
    @Id
    private String id;
    private String name;
    private String description;
    private String category;

}

