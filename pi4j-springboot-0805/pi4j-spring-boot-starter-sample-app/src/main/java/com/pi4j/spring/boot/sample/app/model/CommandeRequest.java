package com.pi4j.spring.boot.sample.app.model;



public class CommandeRequest {

    private String bon_livraison;
    private String date_request;
    private String private_key;

    public String getBon_livraison() {
        return bon_livraison;
    }
    public void setBon_livraison(String bon_livraison) {
        this.bon_livraison = bon_livraison;
    }
    public String getDate_request() {
        return date_request;
    }
    public void setDate_request(String date_request) {
        this.date_request = date_request;
    }
    public String getPrivate_key() {
        return private_key;
    }
    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }
    
    
    
}
