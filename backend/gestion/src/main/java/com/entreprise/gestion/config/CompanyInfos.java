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

    @Value("${quizz.deptquestions}")
    private Integer deptQuestions;

    @Value("${quizz.metierquestions}")
    private Integer metierQuestions;

    @Value("${quizz.generalquestions}")
    private Integer generalQuestions;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
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

    public Integer getDeptQuestions() {
        return deptQuestions;
    }

    public Integer getMetierQuestions() {
        return metierQuestions;
    }

    public Integer getGeneralQuestions() {
        return generalQuestions;
    }


    

    
    


}
