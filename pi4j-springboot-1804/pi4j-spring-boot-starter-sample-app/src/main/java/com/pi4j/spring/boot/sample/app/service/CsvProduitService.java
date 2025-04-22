package com.pi4j.spring.boot.sample.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.io.InputStream;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class CsvProduitService {

    private List<Produit> produits = new ArrayList<>();

    @PostConstruct
    public void init() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("Produit.csv");
             Scanner scanner = new Scanner(is)) {
    
            scanner.nextLine(); // skip header
    
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(";");
                if (values.length >= 6) {
                    Produit p = new Produit();
                    p.setEAN(values[0].trim());
                    p.setQuantite(Integer.parseInt(values[1].trim()));
                    p.setPersonne(values[2].trim());
                    p.setLocalisation(values[3].trim());
                    p.setNomProduit(values[4].trim());
                    p.setBoitierAClignoter(values[5].trim());
                    
                    produits.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public Optional<Produit> findByEan(String ean) {
        System.out.println("recherche pour EAN " + ean);
        return produits.stream()
                .filter(p -> p.getEAN().equals(ean))
                .findFirst();
    }
}

