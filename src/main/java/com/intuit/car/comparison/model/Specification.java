package com.intuit.car.comparison.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "specifications")
public class Specification {
    @Id
    private String id;
    private String type; // e.g., "Engine", "Transmission", "Dimensions"
    private String value;
    private String unit; // e.g., "L", "HP", "mm"

}

