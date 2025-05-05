package com.pi4j.spring.boot.sample.app.service;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@Document(collection = "colis")
public class ColisService {

    @Id
    private String idColis;


    private String client;
    private String etatColis;
    private String dateCreation;
    private String idProduct;
    private String ean13Product;
    private String nameProduct;
    private int qty;
    private int qteScannee;
    private String empCode;
    public String getIdColis() {
        return idColis;
    }
    public void setIdColis(String idColis) {
        this.idColis = idColis;
    }
    public String getClient() {
        return client;
    }
    public void setClient(String client) {
        this.client = client;
    }
    public String getEtatColis() {
        return etatColis;
    }
    public void setEtatColis(String etatColis) {
        this.etatColis = etatColis;
    }
    public String getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }
    public String getIdProduct() {
        return idProduct;
    }
    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }
    public String getEan13Product() {
        return ean13Product;
    }
    public void setEan13Product(String ean13Product) {
        this.ean13Product = ean13Product;
    }
    public String getNameProduct() {
        return nameProduct;
    }
    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }
    public int getQty() {
        return qty;
    }
    public void setQty(int qty) {
        this.qty = qty;
    }
    public int getQteScannee() {
        return qteScannee;
    }
    public void setQteScannee(int qteScannee) {
        this.qteScannee = qteScannee;
    }
    public String getEmpCode() {
        return empCode;
    }
    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

   // private String identifiantCagette;
   // private String idControleur;
   // private String dateDuControle;


}

