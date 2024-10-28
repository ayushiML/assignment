package com.intuit.car.comparison.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Difference {
    private String description;
    private String car1Detail;
    private String car2Detail;
}
