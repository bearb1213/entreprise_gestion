package com.entreprise.gestion.rh.dto;

import lombok.Data;

import java.util.Map;

@Data
public class EvaluationResultDTO {
    private CandidatDTO candidat;
    private Map<String, Double> scores;
}