package com.ash.covid.model;

import java.io.Serializable;
import java.util.List;

public class Continent implements Serializable {
    List<Country> countries = null;
    String continentName = null;

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public String getContinentName() {
        return continentName;
    }

    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }
}
