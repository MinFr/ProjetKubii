package com.pi4j.spring.boot.sample.app.service;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StockService {

    String ean13;//eau produit
    String ean13_emp;// ean de l'emplacement
    String emp_code;//code de l'emplacement
    int qte;//quanite de produit 
    
    public String getEan13() {
        return ean13;
    }
    public void setEan13(String ean13) {
        this.ean13 = ean13;
    }
    public String getEan13_emp() {
        return ean13_emp;
    }
    public void setEan13_emp(String ean13_emp) {
        this.ean13_emp = ean13_emp;
    }
    public String getEmp_code() {
        return emp_code;
    }
    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }
    public int getQte() {
        return qte;
    }
    public void setQte(int qte) {
        this.qte = qte;
    }

}
