package com.pi4j.spring.boot.sample.app.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ColisArticle {

    private String id_colis;
    private String etat_colis;
    private String emp_code;
    private String id_product;
    private String ean13_product;
    private String name_product;
    private int qty;
    private int qte_scannee;
    
    public String getId_colis() {
        return id_colis;
    }
    public void setId_colis(String id_colis) {
        this.id_colis = id_colis;
    }
    public String getEtat_colis() {
        return etat_colis;
    }
    public void setEtat_colis(String etat_colis) {
        this.etat_colis = etat_colis;
    }
    public String getEmp_code() {
        return emp_code;
    }
    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }
    public String getId_product() {
        return id_product;
    }
    public void setId_product(String id_product) {
        this.id_product = id_product;
    }
    public String getEan13_product() {
        return ean13_product;
    }
    public void setEan13_product(String ean13_product) {
        this.ean13_product = ean13_product;
    }
    public String getName_product() {
        return name_product;
    }
    public void setName_product(String name_product) {
        this.name_product = name_product;
    }
    public int getQty() {
        return qty;
    }
    public void setQty(int qty) {
        this.qty = qty;
    }
    public int getQte_scannee() {
        return qte_scannee;
    }
    public void setQte_scannee(int qte_scannee) {
        this.qte_scannee = qte_scannee;
    }


}
