package com.intuit.car.comparison.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimilarCarContext {
    private Car car;
    private int similarityScore;
}
