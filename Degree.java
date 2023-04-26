package com.example.uwindsorcoursefinder;

import kotlin.collections.AbstractMutableList;

public class Degree {

    String name;
    Year[] years = new Year[4];

    public Degree(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Year[] getYears() {
        return years;
    }

    public void setYears(Year[] years) {
        this.years = years;
    }

    public void setYear(Integer index, Year year){
        years[index] = year;
    }

}
