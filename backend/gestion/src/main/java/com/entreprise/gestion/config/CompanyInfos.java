package com.entreprise.gestion.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class CompanyInfos {
    
    @Value("${company.name}")
    private String name;

    @Value("${company.address}")
    private String address;

    @Value("${company.city}")
    private String city;
    
    @Value("${company.openingtime}")
    private Integer openingTime;

    @Value("${company.closingtime}")
    private Integer closingTime;

    @Value("${company.workdaysperweek}")
    private Integer workDaysPerWeek;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Integer getOpeningTime() {
        return openingTime;
    }

    public Integer getClosingTime() {
        return closingTime;
    }

    public Integer getWorkDaysPerWeek() {
        return workDaysPerWeek;
    }

    public String getCity() {
        return city;
    }
    


}
