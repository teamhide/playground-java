package com.teamhide.playground.functionalconfig.car;

public class Engine {
    private String manufacturingCompany;

    public Engine madeBy(final String company) {
        this.manufacturingCompany = company;
        return this;
    }
}
