package com.pi4j.spring.boot.sample.app.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class BonlivraisonRequest {

    private String bon_livraison;
    private String date_validation_cagette_termine;
    
    public String getBon_livraison() {
        return bon_livraison;
    }
    public void setBon_livraison(String bon_livraison) {
        this.bon_livraison = bon_livraison;
    }
    public String getDate_validation_cagette_termine() {
        return date_validation_cagette_termine;
    }
    public void setDate_validation_cagette_termine(String date_validation_cagette_termine) {
        this.date_validation_cagette_termine = date_validation_cagette_termine;
    }
  
}
