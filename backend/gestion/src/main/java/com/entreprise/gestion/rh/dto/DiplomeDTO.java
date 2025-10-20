package com.entreprise.gestion.rh.dto;

public class DiplomeDTO {
    private Integer id;
    private String libelle;
    
    public DiplomeDTO() {}
    
    public DiplomeDTO(Integer id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }
    
    // Getters et setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
}
