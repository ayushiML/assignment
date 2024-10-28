package com.intuit.car.comparison.model;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CarComparisonResult {
    private String field;
    private String car1Value;
    private String car2Value;
    private List<Difference> differences;
}


