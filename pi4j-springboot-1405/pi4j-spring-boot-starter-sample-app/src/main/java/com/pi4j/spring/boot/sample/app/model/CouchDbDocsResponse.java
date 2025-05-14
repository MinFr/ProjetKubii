package com.pi4j.spring.boot.sample.app.model;

import java.util.List;

public class CouchDbDocsResponse {

    private List<Row> rows;


    public List<Row> getRows()
    {
        return rows;
    }


    public static class Row {
        
    private Commande doc;
    public Commande getDoc() {
        return doc;
    }
}
}
