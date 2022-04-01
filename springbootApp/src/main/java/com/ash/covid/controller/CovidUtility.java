package com.ash.covid.controller;

import com.ash.covid.model.Country;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utility Class to help processing the Api Request
 */
@Component
public class CovidUtility {
    public static final Logger logger = LoggerFactory.getLogger(CovidUtility.class);
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd";

    /**
     * This method is used to translate the json object to data holders
     * @param jsonObject
     * @param countryMap
     * @return
     */
    public Map<String, Country> translateJson(JSONObject jsonObject, Map<String, Country> countryMap) {
        boolean hasCountryList = true;
        if(countryMap == null) {
            countryMap = new HashMap<String, Country>();
            hasCountryList = false;
        }
        Iterator<String> keys = jsonObject.keys();
        while(keys.hasNext()) {
            Country country = null;
            String parentKey = keys.next();
            if (jsonObject.get(parentKey) instanceof JSONObject) {
                JSONObject firstChild = (JSONObject) jsonObject.get(parentKey);
                if(firstChild.get("All") instanceof JSONObject) {
                    JSONObject countryNode = (JSONObject) firstChild.get("All");
                    if(hasCountryList) {
                        country = countryMap.get((String) countryNode.get("country"));
                    } else {
                        country =new Country();
                    }
                    country.setName((String) countryNode.get("country"));
                    country.setContinent((String) countryNode.get("continent"));
                    country.setPopulation((Integer) countryNode.get("population"));
                    /*
                     Pick the latest available dates, as per the API,
                     we should ge the latest date always in case of timezone difference
                     the variance can be of 1 day
                     */
                    if(countryNode.has("dates")) {
                        if (countryNode.get("dates") instanceof JSONObject) {
                            JSONObject dates = (JSONObject) countryNode.get("dates");
                            try {
                                country.setDeaths((Integer) dates.get(now(0)));
                            } catch (JSONException jsonException) {
                                try {
                                    country.setDeaths((Integer) dates.get(now(-1)));
                                } catch (JSONException jsonException1) {
                                    try {
                                        country.setDeaths((Integer) dates.get(now(-2)));
                                    } catch (JSONException jsonException2) {
                                        logger.error("JSONException : ", jsonException2.getCause());
                                    }
                                }
                            }
                        }
                    }
                    if(countryNode.has("people_vaccinated")) {
                        country.setVaccinated((Integer) countryNode.get("people_vaccinated"));
                    }
                    if(countryNode.has("people_partially_vaccinated")) {
                        country.setPartiallyVaccinated((Integer) countryNode.get("people_partially_vaccinated"));
                    }

                }
            }
            countryMap.put(country.getName(), country);
        }
        return countryMap;
    }

    /**
     * This method returns the current date
     * or current date +/- no of days passed as input parameter
     * @param days
     * @return
     */
    public static String now(int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        if(days != 0) {
            cal.add(Calendar.DATE, days);
        }
        return sdf.format(cal.getTime());
    }

    /**
     * This method calculates the death percent
     * using the total population and returns an
     * array of double values per country
     * @param countryMap
     * @return
     */
    public double[] getDeathPercent(Map<String, Country> countryMap) {
        ArrayList<Double> deathPercentList = new ArrayList<Double>();
        for (Map.Entry<String,Country> entry : countryMap.entrySet()){
            Country country = entry.getValue();
            double deathPercent = calculatePercentage(country.getDeaths(), country.getPopulation());
            deathPercentList.add(deathPercent);
        }
        return deathPercentList.stream().mapToDouble(Double::doubleValue).toArray();
    }

    /**
     * This method calculates the vaccine percent either only fully
     * vaccinated or full along with partially vaccinated based on
     * the input argument using the total population and returns an
     * array of double values per country
     * @param countryMap
     * @param vaccine
     * @return
     */
    public double[] getVaccinePercent(Map<String, Country> countryMap, String vaccine) {
        ArrayList<Double> vaccinePercentList = new ArrayList<Double>();
        for (Map.Entry<String,Country> entry : countryMap.entrySet()){
            Country country = entry.getValue();
            double vaccinePercent = 0d;
            if(!"both".equalsIgnoreCase(vaccine)) {
                vaccinePercent = calculatePercentage(country.getVaccinated(), country.getPopulation());
            } else {
                vaccinePercent = calculatePercentage(country.getVaccinated() + country.getPartiallyVaccinated(), country.getPopulation());
            }
            vaccinePercentList.add(vaccinePercent);
        }
        return vaccinePercentList.stream().mapToDouble(Double::doubleValue).toArray();
    }

    /**
     * This method gets the obtained and total values
     * and return the percentage of it
     * @param obtained
     * @param total
     * @return
     */
    public double calculatePercentage(double obtained, double total) {
        return obtained * 100 / total;
    }
}
