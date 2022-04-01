package com.ash.covid.model;

import java.io.Serializable;

public class Country implements Serializable {

    private String continent = null;
    private String name = null;
    private Integer population = 0;
    private Integer deaths = 0;
    private Integer vaccinated = 0;
    private Integer partiallyVaccinated = 0;

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(Integer vaccinated) {
        this.vaccinated = vaccinated;
    }

    public Integer getPartiallyVaccinated() {
        return partiallyVaccinated;
    }

    public void setPartiallyVaccinated(Integer partiallyVaccinated) {
        this.partiallyVaccinated = partiallyVaccinated;
    }
}
