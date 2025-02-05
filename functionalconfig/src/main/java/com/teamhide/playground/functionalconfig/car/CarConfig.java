package com.teamhide.playground.functionalconfig.car;

public class CarConfig {
    public static void main(String[] args) {
        final Car car = new Car();
        car.configureEngine(engine -> engine.madeBy("BMW"))
                .configureBrand(brand -> brand.setType("HYUNDAI"))
                .configureCapacity(4);

        System.out.println("car = " + car.getBrand());
        System.out.println("car = " + car.getEngine());
        System.out.println("car = " + car.getCapacity());
    }
}
