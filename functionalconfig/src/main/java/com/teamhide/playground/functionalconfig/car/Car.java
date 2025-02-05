package com.teamhide.playground.functionalconfig.car;

public class Car {
    private Engine engine;
    private Brand brand;
    private int capacity;

    public Car configureEngine(final CarCustomizer<Engine> customizer) {
        this.engine = new Engine();
        customizer.customize(this.engine);
        return this;
    }

    public Car configureBrand(final CarCustomizer<Brand> customizer) {
        this.brand = new Brand();
        customizer.customize(this.brand);
        return this;
    }

    public Car configureCapacity(final int capacity) {
        this.capacity = capacity;
        return this;
    }

    public Engine getEngine() {
        return engine;
    }

    public Brand getBrand() {
        return brand;
    }

    public int getCapacity() {
        return capacity;
    }
}
