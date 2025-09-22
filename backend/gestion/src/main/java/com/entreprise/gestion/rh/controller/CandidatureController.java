package com.entreprise.gestion.rh.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entreprise.gestion.rh.model.Candidature;
import com.entreprise.gestion.rh.service.CandidatureService;

@RestController
@RequestMapping("/candidature/")
public class CandidatureController {

    @Autowired
    private CandidatureService candidatureService;

    @GetMapping({"/infos/{id}","/infos/{id}/"})
    public Map<String,Object> getInfos(@PathVariable("id") Integer id) {
        Map<String,Object> infos = new HashMap<>();
        try {
            Candidature candidature = candidatureService.findCandidatureById(id);
            infos.put("id_metier",candidature.getBesoin().getMetier().getId());
            infos.put("id_dept",candidature.getBesoin().getDepartement().getId());
        } catch (Exception e) {
            infos.put("error", "Candidature introuvable");
            e.printStackTrace();
        }
        return infos;
    }
}
