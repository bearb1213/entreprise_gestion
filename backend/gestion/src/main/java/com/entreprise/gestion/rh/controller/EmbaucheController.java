package com.entreprise.gestion.rh.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entreprise.gestion.rh.service.EmbaucheService;

@RestController
@RequestMapping("/embauche")
public class EmbaucheController {
    
    @Autowired 
    private EmbaucheService embaucheService ;

    @GetMapping("/{id_candidature}")
    public Map<String,Object> embaucher(@PathVariable("id_candidature") Integer idCandidature)
    {

        Map<String,Object> response = new HashMap<>();
        //normalement tokony misy date tonga avy any amle miantso anle API koa eto
        try {
            embaucheService.embaucherEmploye(idCandidature, LocalDate.now());
            response.put("status", "success");
            response.put("message", "Candidature embauchée avec succès");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            e.printStackTrace();
        }
        return response;
    }
}
