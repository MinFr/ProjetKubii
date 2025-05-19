package com.pi4j.spring.boot.sample.app.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class BonlivraisonRequest {

    private String bon_livraison;
    private String date_validation_cagette_termine;
  
}
