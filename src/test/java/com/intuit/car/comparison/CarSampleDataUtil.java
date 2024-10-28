package com.intuit.car.comparison;

import com.intuit.car.comparison.model.Car;
import com.intuit.car.comparison.model.Feature;
import com.intuit.car.comparison.model.Performance;
import com.intuit.car.comparison.model.Specification;

import java.util.Arrays;
import java.util.Collections;

public class CarSampleDataUtil {
    public static Car getCar(String id){
        Feature feature1 = new Feature();
        feature1.setId("f1");
        feature1.setName("Heated Seats");

        Feature feature2 = new Feature();
        feature2.setId("f2");
        feature2.setName("Sunroof");

        Specification spec1 = new Specification();
        spec1.setId("s1");
        spec1.setType("Engine");
        spec1.setValue("2.0L");
        spec1.setUnit("L");

        Specification spec2 = new Specification();
        spec2.setId("s2");
        spec2.setType("Transmission");
        spec2.setValue("8-speed");
        spec2.setUnit("speed");

        Performance performance1 = new Performance();
        performance1.setId("p1");
        performance1.setZeroToSixty(6.5);
        performance1.setTopSpeed(240);
        performance1.setFuelEfficiency(22);

        Performance performance2 = new Performance();
        performance2.setId("p2");
        performance2.setZeroToSixty(6.8);
        performance2.setTopSpeed(230);
        performance2.setFuelEfficiency(20);

        Car car1 = new Car();
        car1.setId("1");
        car1.setMake("Toyota");
        car1.setModel("Camry");
        car1.setYear(2023);
        car1.setTrim("XSE");
        car1.setBodyType("Sedan");
        car1.setPrice(29000);
        car1.setFeatures(Arrays.asList(feature1, feature2));
        car1.setSpecifications(Arrays.asList(spec1, spec2));
        car1.setPerformance(performance1);

        Car car2 = new Car();
        car2.setId("2");
        car2.setMake("Toyota");
        car2.setModel("Camry");
        car2.setYear(2023);
        car2.setTrim("LE");
        car2.setBodyType("Sedan");
        car2.setPrice(27000);
        car2.setFeatures(Collections.singletonList(feature1));
        car2.setSpecifications(Arrays.asList(spec1, spec2));
        car2.setPerformance(performance2);
        if(id.equals("1")){
            return car1;
        }else{
            return car2;
        }
    }
}
