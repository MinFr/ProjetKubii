package com.pi4j.spring.boot.sample.app.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class CsvDataLoader {
    public Map<String, List<ColisService>> loadColis(String path) throws IOException {
        Map<String, List<ColisService>> colisMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(";");
                ColisService c = new ColisService();
                c.id_colis = fields[0];
                c.ean13_product = fields[10];
                c.name_product = fields[11];
                c.qty = Integer.parseInt(fields[13]);
                c.qty_scannee = Integer.parseInt(fields[14]);
                colisMap.computeIfAbsent(c.id_colis, k -> new ArrayList<>()).add(c);
            }
        }
        return colisMap;
    }


    public Map<String, StockService> loadStock(String path) throws IOException {
        Map<String,StockService> stockMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(";");
                StockService s= new StockService();
                s.ean13 = fields[2];
                s.ean13_emp = fields[3];
                s.emp_code = fields[8];
                s.qte = Integer.parseInt(fields[9]);
                stockMap.put(s.ean13, s);
            }
        }
        return stockMap;
    }
}
