package com.pi4j.spring.boot.sample.app.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ScanRequest {

    private String ean13;

    public String getEan13() {
        return ean13;
    }

    public void setEan13(String ean13) {
        this.ean13 = ean13;
    }

}
