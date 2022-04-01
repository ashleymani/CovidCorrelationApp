package com.ash.covid.controller;

import com.ash.covid.model.Country;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.GET;
import java.text.DecimalFormat;
import java.util.*;

@CrossOrigin(maxAge = 3600)
@RestController
public class CovidApiController {

    public static final String baseURL = "https://covid-api.mmediagroup.fr/v1";
    public static final Logger logger = LoggerFactory.getLogger(CovidApiController.class);
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CovidUtility covidUtility;

    @CrossOrigin
    @GET
    @RequestMapping("/computeCorrelation")
    public String computeCorrelation(
            @RequestParam(name = "continent", defaultValue = "Europe") String continent,
            @RequestParam(name = "vaccinetype", defaultValue = "both") String vaccine) {

        if(logger.isDebugEnabled()) {
            logger.debug("Inside computeCorrelation");
        }
        //API call to get the death data for a continent
        String response = restTemplate.getForObject(baseURL+"/history?continent="+continent+"&status=deaths", String.class);
        JSONObject jsonObject = new JSONObject(response);
        Map<String, Country> countryMap = covidUtility.translateJson(jsonObject, null);

        //API call to get the vaccinated data for a continent
        response = restTemplate.getForObject(baseURL+"/vaccines?continent="+continent, String.class);
        jsonObject = new JSONObject(response);
        countryMap = covidUtility.translateJson(jsonObject, countryMap);

        //Calculate the percentage of death and vaccinated
        double[] deathArray = covidUtility.getDeathPercent(countryMap);
        double[] vaccineArray = covidUtility.getVaccinePercent(countryMap,vaccine);

        //Calculate Correlation Coefficient
        final PearsonsCorrelation correlation = new PearsonsCorrelation();
        double corr = correlation.correlation(vaccineArray,deathArray);

        if(logger.isDebugEnabled()) {
            logger.debug("Continent : " + continent);
            logger.debug("Vaccine : " + vaccine);
            logger.debug("deathArray : " + Arrays.toString(deathArray));
            logger.debug("vaccineArray : " + Arrays.toString(vaccineArray));
            logger.debug("correlation : " + corr);
            logger.debug("Exit computeCorrelation");
        }

        return decimalFormat.format(corr);
    }

}
