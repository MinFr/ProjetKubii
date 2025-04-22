package com.pi4j.spring.boot.sample.app.service;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Produit {

    String EAN;//codebarre
    int quantite;
    String personne;
    String localisation;//emplacement du produit
    String boitierAClignoter;//
    String nomProduit;
    
    public String getEAN() {
        return EAN;
    }
    public void setEAN(String eAN) {
        EAN = eAN;
    }
    public int getQuantite() {
        return quantite;
    }
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    public String getPersonne() {
        return personne;
    }
    public void setPersonne(String personne) {
        this.personne = personne;
    }
    public String getLocalisation() {
        return localisation;
    }
    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }
    public String getBoitierAClignoter() {
        return boitierAClignoter;
    }
    public void setBoitierAClignoter(String boitierAClignoter) {
        this.boitierAClignoter = boitierAClignoter;
    }
    public String getNomProduit() {
        return nomProduit;
    }
    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }
    
    

}
