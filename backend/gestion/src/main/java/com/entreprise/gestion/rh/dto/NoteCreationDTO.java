package com.entreprise.gestion.rh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteCreationDTO {
    private Double note;
    private Integer candidatureId;
    private Integer evaluationId;
}