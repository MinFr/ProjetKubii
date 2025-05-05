
package com.pi4j.spring.boot.sample.app.service;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ColisService{

    String id_colis;
    String ean13_product; 
    String name_product;
    int qty;
    int qty_scannee;
    String pin_led;//panier LED

    public String getId_colis() {
        return id_colis;
    }
    public void setId_colis(String id_colis) {
        this.id_colis = id_colis;
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
    public int getQty_scannee() {
        return qty_scannee;
    }
    public void setQty_scannee(int qty_scannee) {
        this.qty_scannee = qty_scannee;
    }
    public String getPin_led() {
        return pin_led;
    }
    public void setPin_led(String pin_led) {
        this.pin_led = pin_led;
    }
     

}
