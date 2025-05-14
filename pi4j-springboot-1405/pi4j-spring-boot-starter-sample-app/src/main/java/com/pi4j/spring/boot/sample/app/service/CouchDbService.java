package com.pi4j.spring.boot.sample.app.service;

import com.pi4j.spring.boot.sample.app.model.ColisArticle;
import com.pi4j.spring.boot.sample.app.model.Commande;
import com.pi4j.spring.boot.sample.app.model.CouchDbDocsResponse;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class CouchDbService {

    @Value("${couchdb.host}")
    private String couchDbUrl;

    @Value("${couchdb.db}")
    private String dbName;

    @Value("${couchdb.user}")
    private String username;

    @Value("${couchdb.password}")
    private String password;

    private final RestTemplate restTemplate = new RestTemplate();


    public Commande saveCommande(Commande commande) {
        String url = couchDbUrl + "/" + dbName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(username, password);

        HttpEntity<Commande> entity = new HttpEntity<>(commande, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println(" Commande enregistrée dans CouchDB");
            return commande;
        } else {
            throw new RuntimeException("Erreur enregistrement CouchDB: " + response.getBody());
        }
    }

    
    @SuppressWarnings("null")
    public boolean updateArticleScanStatus(String ean13) {

        String url = couchDbUrl + "/" + dbName + "/_all_docs?include_docs=true";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(username, password);
    
        HttpEntity<String> entity = new HttpEntity<>(headers);
    
        try {
            ResponseEntity<CouchDbDocsResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                CouchDbDocsResponse.class
            );
    
    
            if (response.getBody() == null) return false;

    
            for (CouchDbDocsResponse.Row row : response.getBody().getRows()) {
                Commande commande = row.getDoc();
                List<ColisArticle> articles = commande.getCommande_colis_articles();
    
                for (ColisArticle article : articles) {
                    System.out.println("=======comparer EAN " + article.getEan13_product());
                    if (article.getEan13_product().equals(ean13)) {
                        System.out.println("Bon Article ==========");
                        article.setQte_scannee(article.getQte_scannee() + 1);
    
    
                        if (article.getQte_scannee() >= article.getQty()) {
                            article.setEtat_colis("complet");
                        }
    
                        
                        saveCommande(commande);
    
                        return true; 
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    } 
    
}

